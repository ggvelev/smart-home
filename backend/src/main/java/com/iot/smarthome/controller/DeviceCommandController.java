package com.iot.smarthome.controller;

import com.iot.smarthome.dto.DeviceCommand;
import com.iot.smarthome.exception.DeviceNotFoundException;
import com.iot.smarthome.mqtt.DeviceCommandPublisher;
import com.iot.smarthome.service.DeviceManagementService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.iot.smarthome.controller.ApiConstants.DEVICE_COMMANDS;
import static com.iot.smarthome.controller.ApiConstants.DEVICE_COMMANDS_HISTORY;

/**
 * REST APIs for device command sending and history retrieval
 */
@RestController
public class DeviceCommandController {

    @Autowired
    private DeviceManagementService deviceManagementService;

    @Autowired
    private DeviceCommandPublisher commandPublisher;

    @PostMapping(DEVICE_COMMANDS)
    public ResponseEntity<Void> issueCommand(@PathVariable String deviceId, @RequestBody DeviceCommand command) {
        Objects.requireNonNull(command.getType(), "Command type must not be null or empty.");
        if (StringUtils.isBlank(command.getValue())) {
            throw new IllegalArgumentException("Command value must not be blank");
        }

        if (!deviceManagementService.exists(deviceId)) {
            throw new DeviceNotFoundException("deviceUuid", deviceId);
        }

        commandPublisher.publish(deviceId, command);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping(DEVICE_COMMANDS_HISTORY)
    public ResponseEntity<Object> fetchCommandHistory(@PathVariable String deviceId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping(DEVICE_COMMANDS_HISTORY)
    public ResponseEntity<Void> deleteCommandHistory(@PathVariable String deviceId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
