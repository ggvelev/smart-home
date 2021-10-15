/***********************************
 Copyright (c) Georgi Velev 2021.
 **********************************/
package com.iot.smarthome.exception;

import org.springframework.security.core.AuthenticationException;

public class MalformedBearerTokenException extends AuthenticationException {

    public MalformedBearerTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MalformedBearerTokenException(String msg) {
        super(msg);
    }
}
