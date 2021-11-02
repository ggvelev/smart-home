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

import com.iot.smarthome.dto.DeviceDetails;
import com.iot.smarthome.dto.DeviceDetailsUpdateRequest;
import com.iot.smarthome.dto.DeviceProperty;
import com.iot.smarthome.dto.DeviceRegistrationRequest;
import com.iot.smarthome.entity.DeviceEntity;
import com.iot.smarthome.entity.DevicePropertyEntity;
import com.iot.smarthome.exception.DeviceNotFoundException;
import com.iot.smarthome.exception.DevicePropertyNotFoundException;
import com.iot.smarthome.repository.DevicePropertyRepository;
import com.iot.smarthome.repository.DeviceRepository;
import com.iot.smarthome.validation.DeviceDetailsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.stream.Collectors.toUnmodifiableList;

/**
 * Service for managing information for devices, mainly CRUD operations over {@link
 * com.iot.smarthome.entity.DeviceEntity}
 */
@Service
public class DeviceManagementService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DevicePropertyRepository devicePropertyRepository;

    @Autowired
    private DeviceDetailsValidator deviceDetailsValidator;

    @Autowired
    private DeviceDataService deviceDataService;

    /**
     * Retrieves details for all available devices stored in DB
     *
     * @return List of {@link DeviceDetails}
     */
    @Transactional(readOnly = true)
    public List<DeviceDetails> listAllDevices() {
        return deviceRepository.findAll().stream()
                .map(DeviceManagementService::toDeviceDetails)
                .collect(toUnmodifiableList());
    }

    /**
     * Fetch details for a given device by UUID
     *
     * @param deviceId device UUID
     * @return {@link DeviceDetails} describing the device
     */
    @Transactional(readOnly = true)
    public DeviceDetails getDevice(String deviceId) {
        return deviceRepository.findByUuid(UUID.fromString(deviceId))
                .map(DeviceManagementService::toDeviceDetails)
                .orElseThrow(() -> new DeviceNotFoundException("deviceUuid", deviceId));
    }

    /**
     * Add new device to DB
     *
     * @param request device details to register
     * @return details for the newly created device
     */
    @Transactional
    public DeviceDetails addDevice(DeviceRegistrationRequest request) {
        deviceDetailsValidator.validateRegistrationRequest(request);

        DeviceEntity device = new DeviceEntity(request.getDeviceUuid());
        device.setMetadata(request.getMetadata());
        device.setNetworkSettings(request.getNetworkSettings());
        device = deviceRepository.save(device);

        return toDeviceDetails(device);
    }

    /**
     * Update details for existing device
     *
     * @param deviceId      device to update
     * @param updateRequest details to update
     * @return updated device details
     */
    @Transactional
    public DeviceDetails updateDevice(String deviceId, DeviceDetailsUpdateRequest updateRequest) {
        deviceDetailsValidator.validateUpdateRequest(deviceId, updateRequest);

        DeviceEntity device = deviceRepository.findByUuid(UUID.fromString(deviceId))
                .orElseThrow(() -> new DeviceNotFoundException("deviceUuid", deviceId));

        device.setMetadata(updateRequest.getDeviceMetadata());
        device.setNetworkSettings(updateRequest.getNetworkSettings());
        device = deviceRepository.save(device);

        return toDeviceDetails(device);
    }

    /**
     * Deletes all associated information regarding device
     *
     * @param deviceId device to delete by UUID
     */
    @Transactional
    public void removeDevice(String deviceId) {
        if (!exists(deviceId)) {
            throw new DeviceNotFoundException("deviceUuid", deviceId);
        }
        deviceRepository.deleteByUuid(UUID.fromString(deviceId));
    }

    @Transactional(readOnly = true)
    public boolean exists(String deviceId) {
        return deviceRepository.existsByUuid(UUID.fromString(deviceId));
    }

    /**
     * List properties related to specific device
     *
     * @param deviceId device UUID to search properties for
     * @return all device properties
     */
    @Transactional(readOnly = true)
    public List<DeviceProperty> listDeviceProperties(String deviceId) {
        return devicePropertyRepository.findAllByDeviceUuid(UUID.fromString(deviceId))
                .map(ps -> ps.stream().map(DeviceManagementService::toDeviceProperty).collect(toUnmodifiableList()))
                .orElseThrow(() -> new DeviceNotFoundException("deviceUuid", deviceId));
    }

    /**
     * Get single device property
     *
     * @param deviceId   device UUID
     * @param propertyId property UUID
     * @return device property if found
     */
    @Transactional(readOnly = true)
    public DeviceProperty getDeviceProperty(String deviceId, String propertyId) {
        return devicePropertyRepository.findByDeviceUuidAndUuid(UUID.fromString(deviceId), UUID.fromString(propertyId))
                .map(DeviceManagementService::toDeviceProperty)
                .orElseThrow(() -> new DevicePropertyNotFoundException(deviceId, "propertyUuid", propertyId));
    }

    /**
     * Add new property for the specified device
     *
     * @param deviceId       device UUID
     * @param deviceProperty property to add
     * @return the newly added property
     */
    @Transactional
    public DeviceProperty addDeviceProperty(String deviceId, DeviceProperty deviceProperty) {
        final DeviceEntity device = deviceRepository.findByUuid(UUID.fromString(deviceId))
                .orElseThrow(() -> new DeviceNotFoundException("deviceUuid", deviceId));

        DevicePropertyEntity entity = new DevicePropertyEntity();
        entity.setDevice(device);
        entity.setDisplayName(deviceProperty.getDisplayName());
        entity.setName(deviceProperty.getName());
        entity.setType(deviceProperty.getType());
        entity.setRead(deviceProperty.getRead());
        entity.setWrite(deviceProperty.getWrite());
        entity = devicePropertyRepository.save(entity);

        return toDeviceProperty(entity);
    }

    /**
     * Update details of a given property
     *
     * @param deviceId      owning device UUID
     * @param propertyId    property UUID
     * @param updateRequest details to update
     * @return updated property
     */
    @Transactional
    public DeviceProperty updateDeviceProperty(String deviceId, String propertyId, DeviceProperty updateRequest) {
        DevicePropertyEntity property = devicePropertyRepository
                .findByDeviceUuidAndUuid(UUID.fromString(deviceId), UUID.fromString(propertyId))
                .orElseThrow(() -> new DevicePropertyNotFoundException(deviceId, "propertyUuid", propertyId));

        // TODO - define validations and move them to DeviceDetailsValidator
        property.setRead(Objects.requireNonNull(updateRequest.getRead()));
        property.setWrite(Objects.requireNonNull(updateRequest.getWrite()));
        property.setName(Objects.requireNonNull(updateRequest.getName()));
        property.setType(Objects.requireNonNull(updateRequest.getType()));
        property.setDisplayName(Objects.requireNonNull(updateRequest.getDisplayName()));
        property = devicePropertyRepository.save(property);

        return toDeviceProperty(property);
    }

    /**
     * Delete device property and all state records related to it in InfluxDB
     *
     * @param deviceId   device UUID
     * @param propertyId property UUID
     */
    @Transactional
    public void deleteDeviceProperty(String deviceId, String propertyId) {
        if (!devicePropertyRepository.existsByDeviceUuidAndUuid(
                UUID.fromString(deviceId), UUID.fromString(propertyId))) {
            throw new DevicePropertyNotFoundException(deviceId, "propertyUuid", propertyId);
        }

        devicePropertyRepository.deleteByUuid(UUID.fromString(propertyId));
        deviceDataService.deleteHistory(deviceId, propertyId);
    }

    /**
     * Maps {@link DevicePropertyEntity} to {@link DeviceProperty}
     *
     * @param prop device property entity
     * @return {@link DeviceProperty}
     */
    private static DeviceProperty toDeviceProperty(DevicePropertyEntity prop) {
        return new DeviceProperty(
                prop.getUuid(),
                prop.getDisplayName(),
                prop.getName(),
                prop.getType(),
                prop.getRead(),
                prop.getWrite()
        );
    }

    /**
     * Converts a {@link DeviceEntity}  to {@link DeviceDetails}
     *
     * @param device device
     * @return {@link DeviceDetails} representing the input device entity
     */
    private static DeviceDetails toDeviceDetails(DeviceEntity device) {
        return new DeviceDetails(
                device.getUuid(),
                device.getMetadata(),
                device.getNetworkSettings()
        );
    }
}
