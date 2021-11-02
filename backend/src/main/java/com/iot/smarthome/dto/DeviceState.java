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

import java.time.Instant;

public class DeviceState {

    @JsonProperty("value")
    private String value;

    @JsonProperty("type")
    private DevicePropertyType type;

    @JsonProperty("valueType")
    private DevicePropertyValueType valueType;

    @JsonProperty("time")
    private Instant time;

    public DeviceState(String value, DevicePropertyType type, DevicePropertyValueType valueType, Instant time) {
        this.value = value;
        this.type = type;
        this.valueType = valueType;
        this.time = time;
    }

    public DeviceState() {
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

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(DevicePropertyType type) {
        this.type = type;
    }

    public void setValueType(DevicePropertyValueType valueType) {
        this.valueType = valueType;
    }

    public void setTime(Instant time) {
        this.time = time;
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
