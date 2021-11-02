package com.iot.smarthome.controller;

import com.iot.smarthome.dto.DeviceProperty;
import com.iot.smarthome.entity.DevicePropertyType;
import com.iot.smarthome.entity.DevicePropertyValueType;
import com.iot.smarthome.service.DeviceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.iot.smarthome.controller.ApiConstants.*;

/**
 * REST APIs for managing device properties
 */
@RestController
public class DevicePropertiesController {

    @Autowired
    private DeviceManagementService deviceManagementService;

    @GetMapping(DEVICE_PROPERTY_TYPES)
    public ResponseEntity<Map<String, Set<String>>> listPropertyTypes() {
        return ResponseEntity.ok(Map.of(
                "propertyTypes",
                Arrays.stream(DevicePropertyType.values()).map(Enum::name).collect(Collectors.toUnmodifiableSet()),
                "valueTypes",
                Arrays.stream(DevicePropertyValueType.values()).map(Enum::name).collect(Collectors.toUnmodifiableSet())
        ));
    }

    @GetMapping(DEVICE_PROPERTIES)
    public ResponseEntity<List<DeviceProperty>> listDeviceProperties(@PathVariable String deviceId) {
        List<DeviceProperty> props = deviceManagementService.listDeviceProperties(deviceId);
        return ResponseEntity.ok(props);
    }

    @PostMapping(DEVICE_PROPERTIES)
    public ResponseEntity<DeviceProperty> addDeviceProperty(@PathVariable String deviceId,
                                                            @RequestBody DeviceProperty deviceProperty) {
        DeviceProperty property = deviceManagementService.addDeviceProperty(deviceId, deviceProperty);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{propertyId}")
                .buildAndExpand(property.getPropertyId())
                .toUri();

        return ResponseEntity.created(location).body(property);
    }

    @GetMapping(DEVICE_PROPERTY_BY_ID)
    public ResponseEntity<DeviceProperty> getDeviceProperty(@PathVariable String deviceId,
                                                            @PathVariable String propertyId) {
        DeviceProperty property = deviceManagementService.getDeviceProperty(deviceId, propertyId);
        return ResponseEntity.ok(property);
    }

    @PutMapping(DEVICE_PROPERTY_BY_ID)
    public ResponseEntity<DeviceProperty> updateDeviceProperty(@PathVariable String deviceId,
                                                               @PathVariable String propertyId,
                                                               @RequestBody DeviceProperty property) {
        DeviceProperty prop = deviceManagementService.updateDeviceProperty(deviceId, propertyId, property);
        return ResponseEntity.ok(prop);
    }

    @DeleteMapping(DEVICE_PROPERTY_BY_ID)
    public ResponseEntity<Void> deleteProperty(@PathVariable String deviceId,
                                               @PathVariable String propertyId) {
        deviceManagementService.deleteDeviceProperty(deviceId, propertyId);
        return ResponseEntity.noContent().build();
    }
}