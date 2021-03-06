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

package com.iot.smarthome.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iot.smarthome.entity.DeviceMetadata;
import com.iot.smarthome.entity.NetworkSettings;

import java.util.UUID;

public class DeviceDetails {

    private final UUID deviceId;
    private final DeviceMetadata deviceMetadata;
    private final NetworkSettings networkSettings;

    @JsonCreator
    public DeviceDetails(@JsonProperty("deviceId") UUID deviceId,
                         @JsonProperty("deviceMetadata") DeviceMetadata deviceMetadata,
                         @JsonProperty("networkSettings") NetworkSettings networkSettings) {
        this.deviceId = deviceId;
        this.deviceMetadata = deviceMetadata;
        this.networkSettings = networkSettings;

    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public DeviceMetadata getDeviceMetadata() {
        return deviceMetadata;
    }

    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }

}
