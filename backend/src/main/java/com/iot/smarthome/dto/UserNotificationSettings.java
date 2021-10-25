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

import java.util.List;

public class UserNotificationSettings {

    private final String deviceId;
    private final List<NotificationSetting> notificationSettings;

    public UserNotificationSettings(
            @JsonProperty("deviceId") String deviceId,
            @JsonProperty("notificationSettings") List<NotificationSetting> notificationSettings) {
        this.deviceId = deviceId;
        this.notificationSettings = notificationSettings;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public List<NotificationSetting> getNotificationSettings() {
        return notificationSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserNotificationSettings that = (UserNotificationSettings) o;

        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) {
            return false;
        }
        return notificationSettings != null ? notificationSettings.equals(that.notificationSettings) :
               that.notificationSettings == null;
    }

    @Override
    public int hashCode() {
        int result = deviceId != null ? deviceId.hashCode() : 0;
        result = 31 * result + (notificationSettings != null ? notificationSettings.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserNotificationSettings{" +
                "deviceId='" + deviceId + '\'' +
                ", notificationSettings=" + notificationSettings +
                '}';
    }
}
