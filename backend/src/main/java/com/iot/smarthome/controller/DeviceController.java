package com.iot.smarthome.controller;

import com.iot.smarthome.dto.DeviceDetails;
import com.iot.smarthome.dto.DeviceDetailsUpdateRequest;
import com.iot.smarthome.dto.DeviceRegistrationRequest;
import com.iot.smarthome.service.DeviceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.iot.smarthome.controller.ApiConstants.DEVICES;
import static com.iot.smarthome.controller.ApiConstants.DEVICE_BY_ID;

/**
 * REST APIs for managing device details
 */
@RestController
public class DeviceController {

    @Autowired
    private DeviceManagementService deviceManagementService;

    @GetMapping(DEVICES)
    public ResponseEntity<List<DeviceDetails>> listDevices() {
        return ResponseEntity.ok().body(deviceManagementService.listAllDevices());
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
        return ResponseEntity.ok().body(deviceManagementService.getDevice(deviceId));
    }

    @PutMapping(DEVICE_BY_ID)
    public ResponseEntity<DeviceDetails> updateDevice(@PathVariable String deviceId,
                                                      @RequestBody DeviceDetailsUpdateRequest updateRequest) {
        return ResponseEntity.ok(deviceManagementService.updateDevice(deviceId, updateRequest));
    }

    @DeleteMapping(DEVICE_BY_ID)
    public ResponseEntity<Void> removeDevice(@PathVariable String deviceId) {
        deviceManagementService.removeDevice(deviceId);
        return ResponseEntity.noContent().build();
    }
}