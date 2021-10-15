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

/**
 * Represents a successful authentication response  based on {@link UsernamePasswordAuthenticationRequest} encapsulating
 * a serialized JWT token, {@link UserDetails}
 */
public class JwtAuthentication {

    /**
     * Serialized JWT token
     */
    private final String token;

    /**
     * Authenticated principal's details
     */
    private final UserDetails userDetails;

    @JsonCreator
    public JwtAuthentication(@JsonProperty("token") String token,
                             @JsonProperty("userDetails") UserDetails userDetails) {
        this.token = token;
        this.userDetails = userDetails;
    }

    public String getToken() {
        return token;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JwtAuthentication that = (JwtAuthentication) o;

        if (!token.equals(that.token)) {
            return false;
        }
        return userDetails.equals(that.userDetails);
    }

    @Override
    public int hashCode() {
        int result = token.hashCode();
        result = 31 * result + userDetails.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "JwtAuthentication{" +
                "token='" + token.substring(0, 5) + token.substring(token.length() - 5) + '\'' +
                ", userDetails=" + userDetails +
                '}';
    }
}
