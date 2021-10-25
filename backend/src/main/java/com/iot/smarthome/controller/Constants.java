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

package com.iot.smarthome.controller;

/**
 * Class to hold mainly definitions of request path URIs
 */
public class Constants {

    private Constants() {
        // pass
    }

    public static final String[] OPEN_API_DOCS = {
            "/api-docs/**", "/api-docs.yaml",
            "/api-explorer/**", "/api-explorer.html",
            "/swagger-ui/**", "/swagger-ui.html"
    };

    public static final String API_BASE = "/api";

    public static final String AUTHENTICATION = API_BASE + "/authentication";

    public static final String DEVICES = API_BASE + "/devices";
    public static final String DEVICE_BY_ID = DEVICES + "/{deviceId}";

    public static final String DEVICE_METADATA = DEVICE_BY_ID + "/metadata";

    public static final String DEVICE_NETWORK_SETTINGS = DEVICE_BY_ID + "/network-settings";

    public static final String DEVICE_PROPERTIES = DEVICE_BY_ID + "/properties";
    public static final String DEVICE_PROPERTY_BY_ID = DEVICE_PROPERTIES + "/{propertyId}";

    public static final String DEVICE_CONFIGURATIONS = DEVICE_BY_ID + "/configurations";
    public static final String DEVICE_CONFIGURATION_BY_ID = DEVICE_CONFIGURATIONS + "/{configurationId}";

    // TODO: these might get removed since actual commands will be described in DeviceProperty type
    public static final String DEVICE_COMMANDS = DEVICE_BY_ID + "/commands";
    public static final String DEVICE_COMMANDS_HISTORY = DEVICE_COMMANDS + "/history";

    public static final String USERS = API_BASE + "/users";
    public static final String USER_BY_ID = USERS + "/{userId}";
    public static final String USER_ACTIVATION = USERS + "/activation";
    public static final String USER_RECOVERY = USERS + "/recovery"; // TODO /recover?action=init and ?action=finish

    public static final String USER_NOTIFICATION_SETTINGS = USER_BY_ID + "/notification-settings";

}
