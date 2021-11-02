/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2021 Georgi Velev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 ******************************************************************************/

package com.iot.smarthome.repository;

import com.influxdb.client.*;
import com.influxdb.client.domain.Bucket;
import com.influxdb.client.domain.DeletePredicateRequest;
import com.influxdb.client.domain.WritePrecision;
import com.iot.smarthome.dto.DeviceState;
import com.iot.smarthome.dto.DeviceStateMeasurement;
import com.iot.smarthome.entity.DevicePropertyType;
import com.iot.smarthome.entity.DevicePropertyValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Expose CRUD operations for InfluxDB
 */
@Repository
public class InfluxDbRepository {

    private static final Logger log = LoggerFactory.getLogger(InfluxDbRepository.class);

    @Autowired
    private InfluxDBClient influxClient;

    @Value("${influx.org}")
    private String organizationName;

    private final Map<String, Bucket> bucketsCache = new ConcurrentHashMap<>();

    private WriteApiBlocking writeApi;
    private BucketsApi bucketsApi;
    private DeleteApi deleteApi;
    private QueryApi queryApi;
    private String orgId;

    @PostConstruct
    private void init() {
        writeApi = influxClient.getWriteApiBlocking();
        bucketsApi = influxClient.getBucketsApi();
        deleteApi = influxClient.getDeleteApi();
        queryApi = influxClient.getQueryApi();

        orgId = influxClient.getOrganizationsApi().findOrganizations().stream()
                .filter(org -> org.getName().equals(organizationName))
                .findAny().get().getId();
    }

    /**
     * Persist a {@link DeviceState} in InfluxDB bucket with name equal to the device's identifier
     *
     * @param deviceId deviceId, also used for bucket name
     * @param state    device state instance
     */
    public void writeDeviceState(String deviceId, DeviceState state) {
        final DeviceStateMeasurement stateRecord = toDeviceStateMeasurement(state);

        getOrCreateBucket(UUID.fromString(deviceId).toString());

        log.info("Persisting device state record in InfluxDB bucket '{}': {}", deviceId, stateRecord);
        writeApi.writeMeasurement(deviceId, orgId, WritePrecision.MS, stateRecord);
    }

    /**
     * Delete a bucket that is backing records for given device
     *
     * @param bucketId same as deviceId
     */
    public void deleteBucket(String bucketId) {
        log.info("Deleting bucket with id='{}'", bucketId);
        bucketsApi.deleteBucket(bucketId);
    }

    private Bucket getOrCreateBucket(String bucketName) {
        return bucketsCache.getOrDefault(bucketName, bucketsCache.computeIfAbsent(bucketName, name -> {
            Bucket bucketByName = bucketsApi.findBucketByName(name);
            return Objects.requireNonNullElseGet(bucketByName, () -> bucketsApi.createBucket(name, orgId));
        }));
    }

    /**
     * List all device state records for the recent 'hoursAgo' hours
     *
     * @param deviceId          device ID == bucket name where records are stored
     * @param propertyType      {@link DevicePropertyType}
     * @param propertyValueType {@link DevicePropertyValueType}
     * @param hoursAgo          relative (to "now") time range of hours
     * @return device states
     */
    public List<DeviceState> readDeviceStates(String deviceId,
                                              DevicePropertyType propertyType,
                                              DevicePropertyValueType propertyValueType,
                                              long hoursAgo) {
        String query = "from(bucket:\"" + getOrCreateBucket(UUID.fromString(deviceId).toString()).getName() + "\")\n" +
                " |> range(start: -" + hoursAgo + "h)\n" +
                " |> filter(fn: (r) => \n" +
                "r._measurement == \"" + DeviceStateMeasurement.MEASUREMENT_NAME + "\" and \n" +
                "r.type == \"" + propertyType.name() + "\" and \n" +
                "r.valueType == \"" + propertyValueType.name() + "\"" +
                ")";

        log.info("Reading state records for device '{}' with property type '{}({})'",
                 deviceId, propertyType, propertyValueType
        );

        return queryApi.query(query, DeviceStateMeasurement.class).stream()
                .map(InfluxDbRepository::toDeviceState)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Delete records for the past 10 years
     *
     * @param bucketName bucket name (i.e. device UUID)
     * @param type       property type
     * @param valueType  property value type
     */
    public void deleteStateRecords(String bucketName, DevicePropertyType type, DevicePropertyValueType valueType) {
        final DeletePredicateRequest predicate = new DeletePredicateRequest()
                .start(OffsetDateTime.now().minusYears(10L))
                .stop(OffsetDateTime.now())
                .predicate("_measurement=\"" + DeviceStateMeasurement.MEASUREMENT_NAME + "\" and " +
                                   "type = \"" + type.name() + "\" and " +
                                   "valueType = \"" + valueType.name() + "\"");

        deleteApi.delete(predicate, bucketName, orgId);
    }

    private static DeviceState toDeviceState(DeviceStateMeasurement measurement) {
        return new DeviceState(
                measurement.getValue(),
                DevicePropertyType.valueOf(measurement.getType()),
                DevicePropertyValueType.valueOf(measurement.getValueType()),
                measurement.getTime().truncatedTo(ChronoUnit.SECONDS)
        );
    }

    private static DeviceStateMeasurement toDeviceStateMeasurement(DeviceState state) {
        return new DeviceStateMeasurement(
                state.getValue(),
                state.getType().name(),
                state.getValueType().name(),
                state.getTime() != null ? state.getTime() : Instant.now()
        );
    }
}
