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

import javax.validation.constraints.Email;
import java.util.Set;
import java.util.UUID;

/**
 * Represents details about a user
 */
public class UserDetails {

    @JsonProperty
    private final UUID uuid;

    @JsonProperty
    private final String username;

    @JsonProperty
    @Email
    private final String email;

    @JsonProperty
    private final Set<String> authorities;

    @JsonCreator
    public UserDetails(UUID uuid, String username, String email, Set<String> authorities) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserDetails that = (UserDetails) o;

        if (!uuid.equals(that.uuid)) {
            return false;
        }
        if (!username.equals(that.username)) {
            return false;
        }
        if (!email.equals(that.email)) {
            return false;
        }
        return authorities.equals(that.authorities);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + authorities.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}