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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iot.smarthome.entity.DevicePropertyType;
import com.iot.smarthome.entity.DevicePropertyValueType;

import java.util.UUID;

public class DeviceProperty {

    private final UUID propertyId;
    private final String displayName;
    private final DevicePropertyType name;
    private final DevicePropertyValueType type;
    private final Boolean read;
    private final Boolean write;

    public DeviceProperty(@JsonProperty("propertyId") UUID propertyId,
                          @JsonProperty("displayName") String displayName,
                          @JsonProperty("name") DevicePropertyType name,
                          @JsonProperty("type") DevicePropertyValueType type,
                          @JsonProperty("read") Boolean read,
                          @JsonProperty("write") Boolean write) {
        this.propertyId = propertyId;
        this.displayName = displayName;
        this.name = name;
        this.type = type;
        this.read = read;
        this.write = write;
    }

    public DevicePropertyType getName() {
        return name;
    }

    public DevicePropertyValueType getType() {
        return type;
    }

    public Boolean getRead() {
        return read;
    }

    public Boolean getWrite() {
        return write;
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceProperty property = (DeviceProperty) o;

        if (propertyId != null ? !propertyId.equals(property.propertyId) : property.propertyId != null) {
            return false;
        }
        if (displayName != null ? !displayName.equals(property.displayName) : property.displayName != null) {
            return false;
        }
        if (name != property.name) {
            return false;
        }
        if (type != property.type) {
            return false;
        }
        if (read != null ? !read.equals(property.read) : property.read != null) {
            return false;
        }
        return write != null ? write.equals(property.write) : property.write == null;
    }

    @Override
    public int hashCode() {
        int result = propertyId != null ? propertyId.hashCode() : 0;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (read != null ? read.hashCode() : 0);
        result = 31 * result + (write != null ? write.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeviceProperty{" +
                "propertyId=" + propertyId +
                ", displayName='" + displayName + '\'' +
                ", name=" + name +
                ", type=" + type +
                ", read=" + read +
                ", write=" + write +
                '}';
    }
}
