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

package com.iot.smarthome.service;

import com.iot.smarthome.dto.JwtAuthentication;
import com.iot.smarthome.dto.UserDetails;
import com.iot.smarthome.dto.UsernamePasswordAuthenticationRequest;
import com.iot.smarthome.entity.UserEntity;
import com.iot.smarthome.exception.UserNotFoundException;
import com.iot.smarthome.repository.UserRepository;
import com.iot.smarthome.security.jwt.JwtTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user authentication (via {@link UsernamePasswordAuthenticationRequest}s)
 */
@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenGenerator jwtGenerator;

    /**
     * Authenticates a user with the given username and password credentials
     *
     * @param auth credentials
     * @return Authentication response containing encoded and signed JWT with additional authenticated user details
     */
    public JwtAuthentication authenticate(UsernamePasswordAuthenticationRequest auth) {
        final UserEntity user = userRepository.findByUsername(auth.getUsername())
                .orElseThrow(() -> new UserNotFoundException("username", auth.getUsername()));

        if (!passwordEncoder.matches(auth.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("The provided password is incorrect");
        }

        validateUserAccountStatus(user);

        final String token = jwtGenerator.generateToken(user);

        return new JwtAuthentication(
                token,
                new UserDetails(
                        user.getUuid(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getAuthoritiesAsSet()
                )
        );
    }

    private void validateUserAccountStatus(UserEntity user) {
        if (user.isLocked()) {
            log.debug("Failed to authenticate since user account is locked");
            throw new LockedException("User account is locked");
        }
        if (!user.isEnabled()) {
            log.debug("Failed to authenticate since user account is disabled");
            throw new DisabledException("User is disabled");
        }
        if (user.isExpired()) {
            log.debug("Failed to authenticate since user account is expired");
            throw new AccountExpiredException("User account has expired");
        }
        if (user.isCredentialsExpired()) {
            log.debug("Failed to authenticate since user account credentials have expired");
            throw new CredentialsExpiredException("User credentials have expired");
        }
    }

}
