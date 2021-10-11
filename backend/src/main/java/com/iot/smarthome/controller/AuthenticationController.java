package com.iot.smarthome.controller;

import com.iot.smarthome.dto.JwtAuthentication;
import com.iot.smarthome.dto.UserDetails;
import com.iot.smarthome.dto.UsernamePasswordAuthenticationRequest;
import com.iot.smarthome.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.iot.smarthome.controller.Constants.AUTHENTICATION;

/**
 * Authentication endpoints
 */
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * @param principal currently authenticated user, if any
     * @return {@link UserDetails} for the currently authenticated user
     */
    @GetMapping(AUTHENTICATION)
    public UserDetails get(Authentication principal) {
        return (UserDetails) principal.getPrincipal();
    }

    /**
     * Authenticates a user against provided username and password credentials
     *
     * @param authRequest {@link UsernamePasswordAuthenticationRequest}
     * @return {@link JwtAuthentication}
     */
    @PostMapping(AUTHENTICATION)
    public ResponseEntity<JwtAuthentication> authenticate(
            @RequestBody UsernamePasswordAuthenticationRequest authRequest) {
        JwtAuthentication auth = authenticationService.authenticate(authRequest);
        return ResponseEntity.ok().body(auth);
        // Alternatively token can be returned in "Authorization" header
    }

}
