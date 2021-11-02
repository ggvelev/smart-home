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

package com.iot.smarthome.service;

import com.iot.smarthome.dto.DeviceProperty;
import com.iot.smarthome.dto.DeviceState;
import com.iot.smarthome.repository.InfluxDbRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DeviceDataService {

    private static final Logger log = LoggerFactory.getLogger(DeviceDataService.class);

    @Autowired
    private InfluxDbRepository influxDbRepo;

    @Autowired
    private DeviceManagementService managementService;

    @Autowired
    private UserNotificationService userNotificationService;

    /**
     * Stores device state update in influxDB and sends notifications to users (if subscribed).
     *
     * @param deviceId    device UUID
     * @param deviceState device state change
     */
    public void storeDeviceStateUpdate(String deviceId, DeviceState deviceState) {
        if (!managementService.exists(deviceId)) {
            log.error("No device with UUID '{}' exists! Skipping device state update.", deviceId);
            return;
        }

        // hacky, remove when devices get taught to send it themselves
        if (deviceState.getTime() == null) {
            deviceState.setTime(Instant.now());
        }

        influxDbRepo.writeDeviceState(deviceId, deviceState);
        userNotificationService.sendNotification(deviceId, deviceState);
    }

    /**
     * Retrieves historical data for device's state represented by a device property (with type and valueType), for the
     * last 'recentHours' hours
     *
     * @param deviceId    device UUID
     * @param propertyId  property UUID
     * @param recentHours the relative to 'now' duration of time in hours
     * @return device states for the last 'recentHours' hours
     */
    public List<DeviceState> getStateHistory(String deviceId, String propertyId, Long recentHours) {
        final DeviceProperty deviceProperty = managementService.getDeviceProperty(deviceId, propertyId);

        return influxDbRepo.readDeviceStates(
                deviceId, deviceProperty.getName(), deviceProperty.getType(), recentHours
        );
    }

    /**
     * Delete device state history records related to the given property
     *
     * @param deviceId   device UUID
     * @param propertyId property UUID
     */
    public void deleteHistory(String deviceId, String propertyId) {
        final DeviceProperty deviceProperty = managementService.getDeviceProperty(deviceId, propertyId);
        influxDbRepo.deleteStateRecords(deviceId, deviceProperty.getName(), deviceProperty.getType());
    }
}
