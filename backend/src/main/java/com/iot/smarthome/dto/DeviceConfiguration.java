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
import com.iot.smarthome.entity.NetworkSettings;

public class DeviceConfiguration {

    private NetworkSettings networkSettings;

    private final String ipAddress;
    private final String macAddress;
    private final String gatewayIpAddress;
    private final String gatewaySubnetMask;
    private final String dnsIp;

    @JsonCreator
    public DeviceConfiguration(@JsonProperty("ipAddress") String ipAddress,
                               @JsonProperty("macAddress") String macAddress,
                               @JsonProperty("gatewayIpAddress") String gatewayIpAddress,
                               @JsonProperty("gatewaySubnetMask") String gatewaySubnetMask,
                               @JsonProperty("dnsIp") String dnsIp) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.gatewayIpAddress = gatewayIpAddress;
        this.gatewaySubnetMask = gatewaySubnetMask;
        this.dnsIp = dnsIp;
    }

    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }

    public void setNetworkSettings(NetworkSettings networkSettings) {
        this.networkSettings = networkSettings;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getGatewayIpAddress() {
        return gatewayIpAddress;
    }

    public String getGatewaySubnetMask() {
        return gatewaySubnetMask;
    }

    public String getDnsIp() {
        return dnsIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceConfiguration that = (DeviceConfiguration) o;

        if (networkSettings != null ? !networkSettings.equals(that.networkSettings) : that.networkSettings != null) {
            return false;
        }
        if (ipAddress != null ? !ipAddress.equals(that.ipAddress) : that.ipAddress != null) {
            return false;
        }
        if (macAddress != null ? !macAddress.equals(that.macAddress) : that.macAddress != null) {
            return false;
        }
        if (gatewayIpAddress != null ? !gatewayIpAddress.equals(that.gatewayIpAddress) :
            that.gatewayIpAddress != null) {
            return false;
        }
        if (gatewaySubnetMask != null ? !gatewaySubnetMask.equals(that.gatewaySubnetMask) :
            that.gatewaySubnetMask != null) {
            return false;
        }
        return dnsIp != null ? dnsIp.equals(that.dnsIp) : that.dnsIp == null;
    }

    @Override
    public int hashCode() {
        int result = networkSettings != null ? networkSettings.hashCode() : 0;
        result = 31 * result + (ipAddress != null ? ipAddress.hashCode() : 0);
        result = 31 * result + (macAddress != null ? macAddress.hashCode() : 0);
        result = 31 * result + (gatewayIpAddress != null ? gatewayIpAddress.hashCode() : 0);
        result = 31 * result + (gatewaySubnetMask != null ? gatewaySubnetMask.hashCode() : 0);
        result = 31 * result + (dnsIp != null ? dnsIp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeviceConfiguration{" +
                "networkSettings=" + networkSettings +
                ", ipAddress='" + ipAddress + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", gatewayIpAddress='" + gatewayIpAddress + '\'' +
                ", gatewaySubnetMask='" + gatewaySubnetMask + '\'' +
                ", dnsIp='" + dnsIp + '\'' +
                '}';
    }
}
