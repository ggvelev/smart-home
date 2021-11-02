package com.iot.smarthome.controller;

import com.iot.smarthome.dto.DeviceState;
import com.iot.smarthome.service.DeviceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.iot.smarthome.controller.ApiConstants.DEVICE_STATE_HISTORY;

/**
 * REST APIs for managing device state/telemetry details
 */
@RestController
public class DeviceTelemetryController {

    @Autowired
    private DeviceDataService deviceDataService;

    @GetMapping(DEVICE_STATE_HISTORY)
    public ResponseEntity<List<DeviceState>> fetchStateHistory(@PathVariable String deviceId,
                                                               @PathVariable String propertyId,
                                                               @RequestParam(defaultValue = "24") Long forLastHours) {
        return ResponseEntity.ok(deviceDataService.getStateHistory(deviceId, propertyId, forLastHours));
    }

    @DeleteMapping(DEVICE_STATE_HISTORY)
    public ResponseEntity<Void> deleteRecords(@PathVariable String deviceId, @PathVariable String propertyId) {
        deviceDataService.deleteHistory(deviceId, propertyId);
        return ResponseEntity.noContent().build();
    }
}
