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

package com.iot.smarthome.validation;

import com.iot.smarthome.dto.DeviceDetailsUpdateRequest;
import com.iot.smarthome.dto.DeviceRegistrationRequest;
import com.iot.smarthome.entity.DeviceMetadata;
import com.iot.smarthome.entity.NetworkSettings;
import com.iot.smarthome.exception.*;
import com.iot.smarthome.repository.DeviceRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Validates properties related to {@link com.iot.smarthome.entity.DeviceEntity}
 */
@Component
public class DeviceDetailsValidator {

    private static final Logger log = LoggerFactory.getLogger(DeviceDetailsValidator.class);

    @Autowired
    private DeviceRepository deviceRepository;

    private static final Pattern IPV4_ADDRESS_PATTERN = Pattern.compile(
            "^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])$");

    private static final Pattern MAC_ADDRESS_PATTERN = Pattern.compile(
            "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");

    public void validateUpdateRequest(String deviceId, DeviceDetailsUpdateRequest updateRequest) {
        if (updateRequest.getDeviceUuid() != null) {
            log.error("Unsupported operation", new NotImplementedException("Updating device UUID is not implemented"));
        }

        validateMetadata(updateRequest.getDeviceMetadata());
        validateNetworkSettings(updateRequest.getNetworkSettings());
    }

    public void validateRegistrationRequest(DeviceRegistrationRequest request) {
        if (deviceRepository.existsByUuid(request.getDeviceUuid())) {
            throw new DuplicateDeviceException("deviceUuid", request.getDeviceUuid().toString());
        }

        validateMetadata(request.getMetadata());
        validateNetworkSettings(request.getNetworkSettings());
    }

    private void validateMetadata(DeviceMetadata deviceMetadata) {
        if (deviceMetadata.getName() == null || deviceMetadata.getName().isBlank()) {
            throw new InvalidDeviceMetadata("name", deviceMetadata.getName());
        }

        if (deviceMetadata.getDescription() == null || deviceMetadata.getDescription().isBlank()) {
            throw new InvalidDeviceMetadata("description", deviceMetadata.getDescription());
        }
    }

    private void validateNetworkSettings(NetworkSettings networkSettings) {
        if (!isValidIPv4Address(networkSettings.getIpAddress())) {
            throw new InvalidIpAddressException(networkSettings.getIpAddress());
        }

        if (!isValidMacAddress(networkSettings.getMacAddress())) {
            throw new InvalidMacAddressException(networkSettings.getMacAddress());
        }

        if (!isValidCidr(networkSettings.getNetworkCidr())) {
            throw new InvalidNetworkCidrException(networkSettings.getNetworkCidr());
        }
    }

    // TODO extract to a NetworkDetailsValidator
    private boolean isValidCidr(String networkCidr) {
        return networkCidr != null && !networkCidr.isBlank()
                && networkCidr.split("/").length == 2
                && IPV4_ADDRESS_PATTERN.matcher(networkCidr.split("/")[0]).matches()
                && Integer.parseInt(networkCidr.split("/")[1]) >= 0
                && Integer.parseInt(networkCidr.split("/")[1]) <= 32;
    }

    // TODO extract to a NetworkDetailsValidator
    private boolean isValidMacAddress(String macAddress) {
        return macAddress != null && !macAddress.isBlank() && MAC_ADDRESS_PATTERN.matcher(macAddress).matches();
    }

    // TODO extract to a NetworkDetailsValidator
    private boolean isValidIPv4Address(String ipAddress) {
        return ipAddress != null && !ipAddress.isBlank() && IPV4_ADDRESS_PATTERN.matcher(ipAddress).matches();
    }
}
