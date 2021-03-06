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

package com.iot.smarthome.exception;

import static java.lang.String.format;

public class DevicePropertyNotFoundException extends RuntimeException {

    private final String deviceId;
    private final String field;
    private final String value;

    public DevicePropertyNotFoundException(String deviceId, String field, String value) {
        super(format("Property for device '%s' with %s='%s' was not found", deviceId, field, value));
        this.deviceId = deviceId;
        this.field = field;
        this.value = value;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
