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
import com.iot.smarthome.dto.UserNotificationSettings;
import com.iot.smarthome.security.RequestingUserOrAdminAllowed;
import com.iot.smarthome.service.UserAccountService;
import com.iot.smarthome.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.iot.smarthome.controller.Constants.*;

/**
 * Exposes REST APIs for management and CRUD operations on {@link com.iot.smarthome.entity.UserEntity}
 */
@RestController
public class UserController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserNotificationService userNotificationService;

    /**
     * List all registered users
     *
     * @return response containing all registered users
     */
    @GetMapping(USERS)
    public ResponseEntity<List<UserDetails>> listUsers() {
        // TODO: sorting, paging and filtering to be implemented
        return ResponseEntity.ok(userAccountService.listUsers());
    }

    /**
     * Handles user registration request
     *
     * @param user new user details
     * @return response with location of the newly created resource and the resource itself
     */
    @PostMapping(USERS)
    public ResponseEntity<UserDetails> registerUser(@RequestBody CreateUserRequest user) {
        final UserDetails userDetails = userAccountService.registerUser(user);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{userId}")
                .buildAndExpand(userDetails.getUuid())
                .toUri();

        return ResponseEntity.created(location).body(userDetails);
    }

    /**
     * Retrieve details about a user
     *
     * @param userId user's UUID
     * @return user
     */
    @RequestingUserOrAdminAllowed
    @GetMapping(USER_BY_ID)
    public ResponseEntity<UserDetails> getUser(@PathVariable String userId) {
        return ResponseEntity.ok().body(userAccountService.findByUuid(userId));
    }

    @RequestingUserOrAdminAllowed
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
    @RequestingUserOrAdminAllowed
    @DeleteMapping(USER_BY_ID)
    public ResponseEntity<Void> removeUser(@PathVariable String userId, @RequestParam boolean softDelete) {
        // if ROLE_ADMIN -> immediately remove, or else -> delete/deactivate with(out?) confirmation
        userAccountService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Confirm/activate newly registered user accounts
     *
     * @return
     */
    @PostMapping(USER_ACTIVATION)
    public ResponseEntity<Object> activateUserAccount() {
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
    public ResponseEntity<Object> recoverUserAccount(
            @RequestParam(value = "recovery-id", required = false) String recoveryId) {
        // if recoveryId provided -> finish account recovery | otherwise initiate recovery
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestingUserOrAdminAllowed
    @GetMapping(USER_NOTIFICATION_SETTINGS)
    public ResponseEntity<List<UserNotificationSettings>> getNotificationSettings(@PathVariable String userId) {
        List<UserNotificationSettings> settings = userNotificationService.getUserNotificationSettings(userId);
        return ResponseEntity.ok(settings);
    }

    @RequestingUserOrAdminAllowed
    @PutMapping(USER_NOTIFICATION_SETTINGS)
    public ResponseEntity<UserNotificationSettings> updateNotificationSettings(
            @PathVariable String userId,
            @RequestBody UserNotificationSettings settings) {
        final UserNotificationSettings updated = userNotificationService.updateUserSettings(userId, settings);
        return ResponseEntity.ok(updated);
    }
}
