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

public class DeviceDetailsUpdateRequest {

    /**
     * Changing a device's UUID does not sound like a good idea generally, but there might be a scenario in which we
     * want to replace a given physical device with a new one and hence new UUID (let's say the old device got broken)
     * but we wouldn't like to lose all associated data to this particular device. In such scenario the easiest thing
     * (but in the same way a bit 'workaround-ish') to do is to update that device's UUID.
     * <p>
     * Alternative (and maybe better (but in what terms?)) approach would be to re-associate all entities linked with
     * the 'broken' device to a new device entity. This is to be implemented in future and for now we forbid updating
     * device's UUID.
     */
    private final UUID deviceUuid;
    private final DeviceMetadata deviceMetadata;
    private final NetworkSettings networkSettings;

    @JsonCreator
    public DeviceDetailsUpdateRequest(@JsonProperty("deviceUuid") UUID deviceUuid,
                                      @JsonProperty("deviceMetadata") DeviceMetadata deviceMetadata,
                                      @JsonProperty("networkSettings") NetworkSettings networkSettings) {
        this.deviceUuid = deviceUuid;
        this.deviceMetadata = deviceMetadata;
        this.networkSettings = networkSettings;
    }

    public UUID getDeviceUuid() {
        return deviceUuid;
    }

    public DeviceMetadata getDeviceMetadata() {
        return deviceMetadata;
    }

    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceDetailsUpdateRequest that = (DeviceDetailsUpdateRequest) o;

        if (deviceUuid != null ? !deviceUuid.equals(that.deviceUuid) : that.deviceUuid != null) {
            return false;
        }
        if (deviceMetadata != null ? !deviceMetadata.equals(that.deviceMetadata) : that.deviceMetadata != null) {
            return false;
        }
        return networkSettings != null ? networkSettings.equals(that.networkSettings) : that.networkSettings == null;
    }

    @Override
    public int hashCode() {
        int result = deviceUuid != null ? deviceUuid.hashCode() : 0;
        result = 31 * result + (deviceMetadata != null ? deviceMetadata.hashCode() : 0);
        result = 31 * result + (networkSettings != null ? networkSettings.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeviceDetailsUpdateRequest{" +
                "deviceUuid=" + deviceUuid +
                ", deviceMetadata=" + deviceMetadata +
                ", networkSettings=" + networkSettings +
                '}';
    }
}
