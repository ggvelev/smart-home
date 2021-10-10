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

import com.iot.smarthome.dto.CreateUserRequest;
import com.iot.smarthome.dto.UserDetails;
import com.iot.smarthome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.iot.smarthome.controller.Constants.*;
import static com.iot.smarthome.security.SecurityConstants.ROLE_ADMIN;

/**
 * Exposes REST APIs for management and CRUD operations on {@link com.iot.smarthome.entity.UserEntity}
 */
//@Validated
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * List all registered users
     *
     * @return response containing all registered users
     */
    @RolesAllowed(ROLE_ADMIN)
    @GetMapping(USERS)
    public ResponseEntity<List<UserDetails>> listUsers() {
        // TODO: sorting, paging and filtering to be added
        return ResponseEntity.ok(userService.listUsers());
    }

    /**
     * Handles user registration request
     *
     * @param user new user details
     * @return response with location of the newly created resource
     */
    @PostMapping(USERS)
    public ResponseEntity<Void> registerUser(@Valid @RequestBody CreateUserRequest user) {
        final UserDetails userDetails = userService.registerUser(user);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{userId}")
                .buildAndExpand(userDetails.uuid)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Retrieve details about a user
     *
     * @param userId user's UUID
     * @return
     */
    @RolesAllowed(ROLE_ADMIN)
    @GetMapping(USER_BY_ID)
    public ResponseEntity<UserDetails> getUser(@PathVariable String userId) {
        return ResponseEntity.ok().body(userService.findByUuid(UUID.fromString(userId)));
    }

    @PutMapping(USER_BY_ID)
    public ResponseEntity<Object> updateUserDetails(@PathVariable String userId,
                                                    @RequestBody Object updateUserDetails) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Remove user registration
     *
     * @param userId
     * @return
     */
    @DeleteMapping(USER_BY_ID)
    public ResponseEntity<Object> removeUser(@PathVariable String userId, @RequestParam boolean softDelete) {
        // if ROLE_ADMIN -> immediately remove, or else -> delete/deactivate with(out?) confirmation
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Confirm/activate newly registered user accounts
     *
     * @return
     */
    @PostMapping(USER_ACTIVATION)
    public ResponseEntity<Object> activateUser(@PathVariable String userId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Recover user account (e.g. forgotten password). If <code>recovery-id</code> query parameter is present, the
     * service will attempt to complete the recovery process.
     *
     * @param recoveryId
     * @return
     */
    @PostMapping(USER_RECOVERY)
    public ResponseEntity<Object> recoverUser(@PathVariable String userId,
                                              @RequestParam(value = "recovery-id", required = false)
                                                      String recoveryId) {
        // if recoveryId provided -> finish account recovery | otherwise initiate recovery
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
