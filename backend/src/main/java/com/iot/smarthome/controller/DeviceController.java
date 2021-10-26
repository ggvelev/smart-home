package com.iot.smarthome.controller;

import com.iot.smarthome.dto.*;
import com.iot.smarthome.service.DeviceConfigurationService;
import com.iot.smarthome.service.DeviceDataService;
import com.iot.smarthome.service.DeviceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.iot.smarthome.controller.ApiConstants.*;

/**
 * REST APIs for managing device details
 */
@RestController
public class DeviceController {

    @Autowired
    private DeviceManagementService deviceManagementService;

    @Autowired
    private DeviceConfigurationService deviceConfigurationService;

    @Autowired
    private DeviceDataService deviceDataService;

    /******************** Devices CRUD APIs ********************/

    @GetMapping(DEVICES)
    public ResponseEntity<List<DeviceDetails>> listDevices() {
        final List<DeviceDetails> deviceDetails = deviceManagementService.listAllDevices();
        return ResponseEntity.ok().body(deviceDetails);
    }

    @PostMapping(DEVICES)
    public ResponseEntity<DeviceDetails> createDevice(@RequestBody DeviceRegistrationRequest request) {
        final DeviceDetails registered = deviceManagementService.addDevice(request);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{deviceId}")
                .buildAndExpand(registered.getDeviceId())
                .toUri();

        return ResponseEntity.created(location).body(registered);
    }

    @GetMapping(DEVICE_BY_ID)
    public ResponseEntity<DeviceDetails> getDevice(@PathVariable String deviceId) {
        DeviceDetails device = deviceManagementService.getDevice(deviceId);
        return ResponseEntity.ok().body(device);
    }

    @PutMapping(DEVICE_BY_ID)
    public ResponseEntity<DeviceDetails> updateDevice(@PathVariable String deviceId,
                                                      @RequestBody DeviceDetailsUpdateRequest updateRequest) {
        final DeviceDetails updateDevice = deviceManagementService.updateDevice(deviceId, updateRequest);
        return ResponseEntity.ok(updateDevice);
    }

    @DeleteMapping(DEVICE_BY_ID)
    public ResponseEntity<Void> removeDevice(@PathVariable String deviceId) {
        deviceManagementService.removeDevice(deviceId);
        return ResponseEntity.noContent().build();
    }

    /******************** Device properties APIs ********************/

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

    /******************** Device configurations APIs ********************/

    @GetMapping(DEVICE_CONFIGURATIONS)
    public ResponseEntity<Object> getDeviceConfigurations(@PathVariable String deviceId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping(DEVICE_CONFIGURATIONS)
    public ResponseEntity<Object> createDeviceConfiguration(@PathVariable String deviceId,
                                                            @RequestBody DeviceConfiguration deviceConfiguration) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(DEVICE_CONFIGURATION_BY_ID)
    public ResponseEntity<Object> getDeviceConfig(@PathVariable String deviceId,
                                                  @PathVariable String configurationId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping(DEVICE_CONFIGURATION_BY_ID)
    public ResponseEntity<Object> updateDeviceConfig(@PathVariable String deviceId,
                                                     @PathVariable String configurationId,
                                                     @RequestBody DeviceConfiguration deviceConfiguration) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping(DEVICE_CONFIGURATION_BY_ID)
    public ResponseEntity<Object> deleteDeviceConfig(@PathVariable String deviceId,
                                                     @PathVariable String configurationId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
