package com.iot.smarthome.controller;

import com.iot.smarthome.dto.DeviceConfiguration;
import com.iot.smarthome.service.DeviceConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.iot.smarthome.controller.ApiConstants.DEVICE_CONFIGURATIONS;
import static com.iot.smarthome.controller.ApiConstants.DEVICE_CONFIGURATION_BY_ID;

/**
 * REST APIs for managing device configuration details
 */
@RestController
public class DeviceConfigurationController {

    @Autowired
    private DeviceConfigurationService deviceConfigService;

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
    public ResponseEntity<Object> getDeviceConfiguration(@PathVariable String deviceId,
                                                         @PathVariable String configurationId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping(DEVICE_CONFIGURATION_BY_ID)
    public ResponseEntity<Object> updateDeviceConfiguration(@PathVariable String deviceId,
                                                            @PathVariable String configurationId,
                                                            @RequestBody DeviceConfiguration deviceConfiguration) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping(DEVICE_CONFIGURATION_BY_ID)
    public ResponseEntity<Object> deleteDeviceConfiguration(@PathVariable String deviceId,
                                                            @PathVariable String configurationId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}