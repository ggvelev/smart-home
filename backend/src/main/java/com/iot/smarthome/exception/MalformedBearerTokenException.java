/***********************************
 Copyright (c) Georgi Velev 2021.
 **********************************/
package com.iot.smarthome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MalformedBearerTokenException extends AuthenticationException {

    public MalformedBearerTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MalformedBearerTokenException(String msg) {
        super(msg);
    }
}
