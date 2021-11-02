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
import com.iot.smarthome.entity.DevicePropertyType;
import com.iot.smarthome.entity.DevicePropertyValueType;

import java.time.Instant;

public class DeviceState {

    private final String value;
    private final DevicePropertyType type;
    private final DevicePropertyValueType valueType;
    private final Instant time;

    @JsonCreator
    public DeviceState(@JsonProperty("value") String value,
                       @JsonProperty("type") DevicePropertyType type,
                       @JsonProperty("valueType") DevicePropertyValueType valueType,
                       @JsonProperty("time") Instant time) {
        this.value = value;
        this.type = type;
        this.valueType = valueType;
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public DevicePropertyType getType() {
        return type;
    }

    public DevicePropertyValueType getValueType() {
        return valueType;
    }

    public Instant getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "DeviceState{" +
                "value='" + value + '\'' +
                ", type=" + type +
                ", valueType=" + valueType +
                ", time=" + time +
                '}';
    }
}
