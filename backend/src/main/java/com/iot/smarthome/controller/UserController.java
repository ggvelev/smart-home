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
import com.iot.smarthome.dto.UserAccountRecoveryAction;
import com.iot.smarthome.dto.UserDetails;
import com.iot.smarthome.dto.UserNotificationSettings;
import com.iot.smarthome.service.UserAccountService;
import com.iot.smarthome.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static com.iot.smarthome.controller.ApiConstants.*;

/**
 * Exposes REST APIs for management and CRUD operations on {@link com.iot.smarthome.entity.UserEntity}, {@link
 * com.iot.smarthome.entity.UserNotificationSettingsEntity}.
 */
@RestController
public class UserController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserNotificationService userNotificationService;

    /**
     * List all registered users.
     *
     * @return response containing all registered users
     */
    @GetMapping(USERS)
    public ResponseEntity<List<UserDetails>> listUsers() {
        // TODO: sorting, paging and filtering to be implemented
        return ResponseEntity.ok(userAccountService.listUsers());
    }

    /**
     * Handles user registration request.
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
     * Retrieve details about a user.
     *
     * @param userId user's UUID
     * @return user
     */
    @GetMapping(USER_BY_ID)
    public ResponseEntity<UserDetails> getUser(@PathVariable String userId) {
        return ResponseEntity.ok().body(userAccountService.findByUuid(userId));
    }

    @PutMapping(USER_BY_ID)
    public ResponseEntity<Object> updateUserDetails(@PathVariable String userId,
                                                    @RequestBody Object updateUserDetails) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Delete user account.
     *
     * @param userId user's UUID
     * @return {@link HttpStatus#NO_CONTENT}
     */
    @DeleteMapping(USER_BY_ID)
    public ResponseEntity<Void> removeUser(@PathVariable String userId,
                                           @RequestParam(defaultValue = "false") boolean softDelete) {
        if (softDelete) {
            userAccountService.disableUser(userId);
        } else {
            userAccountService.deleteUser(userId);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Recover user account (e.g. forgotten password).
     * <p>
     * If <code>recovery-id</code> query parameter is present, the service will attempt to initiate the recovery process
     * by sending account recovery URL to the provided e-mail address.
     *
     * @param action      account recovery action - {@link UserAccountRecoveryAction#init} or {@link
     *                    UserAccountRecoveryAction#finish}
     * @param recoveryId  unique account recovery identifier
     * @param userEmail   user email who is attempting recovery and where the recovery URL will be sent
     * @param newPassword new password for the user
     * @return {@link HttpStatus#ACCEPTED} or {@link HttpStatus#OK}
     */
    @PostMapping(USER_RECOVERY)
    public ResponseEntity<Object> recoverUserAccount(
            @RequestParam UserAccountRecoveryAction action,
            @RequestParam(required = false) String recoveryId,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String newPassword) {
        // Initiate recovery with provided valid user email:
        if (action == UserAccountRecoveryAction.init) {
            // Send email with unique link (or unique code) to use for recovery finalization:
            // userAccountService.initiateRecovery(userEmail);
            // return ResponseEntity.accepted().build();
        }

        // Complete recovery by providing the unique recoveryId and new password:
        if (action == UserAccountRecoveryAction.finish) {
            // userAccountService.completeRecovery(recoveryId, newPassword);
            // return ResponseEntity.ok().build();
        }

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Fetch all notifications settings for given user.
     *
     * @param userId user UUID
     * @return List of {@link UserNotificationSettings}
     */
    @GetMapping(USER_NOTIFICATION_SETTINGS)
    public ResponseEntity<List<UserNotificationSettings>> getNotificationSettings(@PathVariable String userId) {
        return ResponseEntity.ok(userNotificationService.getUserNotificationSettings(userId));
    }

    /**
     * TODO - "notifications" to be totally refactored, just wiring things up in order to bring demonstrable MVP!
     * Fetch all notifications settings for given user, related to specific device
     *
     * @param userId   user UUID
     * @param deviceId device UUID
     * @return List of {@link UserNotificationSettings}
     */
    @GetMapping(USER_DEVICE_NOTIFICATION_SETTINGS)
    public ResponseEntity<UserNotificationSettings> getDeviceNotificationSettings(@PathVariable String userId,
                                                                                  @PathVariable String deviceId) {
        return ResponseEntity.ok(userNotificationService.getUserNotificationSettings(userId).stream()
                                         .filter(s -> s.getDeviceId().equals(deviceId))
                                         .findAny()
                                         .orElse(new UserNotificationSettings(deviceId, Collections.emptyList())));
    }

    /**
     * API operation for updating user notification settings for events of interest (currently limited to all events
     * related to the device specified in {@link UserNotificationSettings#getDeviceId()}.
     *
     * @param userId   user UUID
     * @param settings notification settings to update
     * @return the updated settings
     */
    @PutMapping(USER_NOTIFICATION_SETTINGS)
    public ResponseEntity<UserNotificationSettings> updateNotificationSettings(
            @PathVariable String userId,
            @RequestBody UserNotificationSettings settings) {
        return ResponseEntity.ok(userNotificationService.updateUserSettings(userId, settings));
    }
}
