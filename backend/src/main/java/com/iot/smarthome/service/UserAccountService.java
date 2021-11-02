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

import com.iot.smarthome.dto.CreateUserRequest;
import com.iot.smarthome.dto.UserDetails;
import com.iot.smarthome.entity.UserEntity;
import com.iot.smarthome.exception.UserNotFoundException;
import com.iot.smarthome.repository.UserAuthoritiesRepository;
import com.iot.smarthome.repository.UserRepository;
import com.iot.smarthome.validation.UserDetailsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.iot.smarthome.security.SecurityConstants.ROLE_USER;

/**
 * Service for managing and handling {@link UserEntity} related operations
 */
@Service
public class UserAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthoritiesRepository authoritiesRepository;

    @Autowired
    private UserDetailsValidator userDetailsValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers new user
     *
     * @param request user registration request
     * @return details for the newly registered user
     */
    @Transactional
    public UserDetails registerUser(CreateUserRequest request) {
        userDetailsValidator.validateUserRegistrationRequest(request);

        final UserEntity newUser = new UserEntity();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setEnabled(true); // TODO: activation via email confirmation link
        newUser.addAuthority(authoritiesRepository.getByAuthority(ROLE_USER));

        final UserEntity registered = userRepository.save(newUser);
        return toUserDetails(registered);
    }

    /**
     * Disables user's account
     *
     * @param userUuid user's UUID
     */
    @Transactional
    public void disableUser(String userUuid) {
        final UserEntity user = byUuid(userUuid);
        user.setEnabled(false);
        userRepository.save(user);
    }

    /**
     * Deletes user's account
     *
     * @param userUuid user's UUID
     */
    @Transactional
    public void deleteUser(String userUuid) {
        userRepository.deleteByUuid(UUID.fromString(userUuid));
    }

    /**
     * Retrieves a {@link UserEntity} by a given username
     *
     * @param username username
     * @return {@link UserDetails} representing the user found
     */
    @Transactional(readOnly = true)
    public UserDetails findByUsername(String username) {
        final UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("username", username));

        return toUserDetails(user);
    }

    /**
     * Retrieves a {@link UserEntity} by a given UUID
     *
     * @param userUuid user's UUID
     * @return {@link UserDetails} representing the user found
     */
    @Transactional(readOnly = true)
    public UserDetails findByUuid(String userUuid) {
        final UserEntity userEntity = byUuid(userUuid);
        return toUserDetails(userEntity);
    }

    @Transactional(readOnly = true)
    public UserDetails findByEmail(String email) {
        final UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email", email));

        return toUserDetails(userEntity);
    }

    /**
     * Fetch a page of users, optionally sorted by fields
     *
     * @param pageable a {@link Pageable} request
     * @return page of user details
     */
    @Transactional(readOnly = true)
    public Page<UserDetails> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserAccountService::toUserDetails);
    }

    /**
     * @return list of all registered users
     */
    @Transactional(readOnly = true)
    public List<UserDetails> listUsers() {
        return listUsers(Pageable.unpaged()).get().collect(Collectors.toUnmodifiableList());
    }

    private UserEntity byUuid(String userUuid) {
        return userRepository.findByUuid(UUID.fromString(userUuid)).orElseThrow(() -> {
            throw new UserNotFoundException("uuid", userUuid);
        });
    }

    private static UserDetails toUserDetails(UserEntity entity) {
        return new UserDetails(
                entity.getUuid(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getAuthoritiesAsSet()
        );
    }
}
