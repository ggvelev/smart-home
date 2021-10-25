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

import com.iot.smarthome.dto.JwtAuthentication;
import com.iot.smarthome.dto.UserDetails;
import com.iot.smarthome.dto.UsernamePasswordAuthenticationRequest;
import com.iot.smarthome.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.iot.smarthome.controller.Constants.AUTHENTICATION;

/**
 * Authentication endpoints
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * @param authentication currently authenticated user, if any
     * @return {@link UserDetails} of the currently authenticated user. Otherwise {@link HttpStatus#UNAUTHORIZED} or
     * {@link HttpStatus#FORBIDDEN}
     */
    @GetMapping(AUTHENTICATION)
    public ResponseEntity<UserDetails> get(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return authentication.getPrincipal() != null ?
               ResponseEntity.ok((UserDetails) authentication.getPrincipal()) :
               ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Authenticates a user against provided username and password credentials
     *
     * @param request {@link UsernamePasswordAuthenticationRequest}
     * @return {@link JwtAuthentication}
     */
    @PostMapping(AUTHENTICATION)
    public ResponseEntity<JwtAuthentication> authenticate(@RequestBody UsernamePasswordAuthenticationRequest request) {
        JwtAuthentication auth = authenticationService.authenticate(request);
        return ResponseEntity.ok().body(auth);
    }
}
