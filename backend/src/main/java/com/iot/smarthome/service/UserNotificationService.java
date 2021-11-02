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

import com.iot.smarthome.dto.DeviceState;
import com.iot.smarthome.dto.NotificationSetting;
import com.iot.smarthome.dto.UserNotificationSettings;
import com.iot.smarthome.entity.UserNotificationSettingsEntity;
import com.iot.smarthome.exception.DeviceNotFoundException;
import com.iot.smarthome.exception.UserNotFoundException;
import com.iot.smarthome.notification.EmailNotificationSender;
import com.iot.smarthome.notification.SlackNotificationSender;
import com.iot.smarthome.repository.DeviceRepository;
import com.iot.smarthome.repository.UserNotificationSettingsRepository;
import com.iot.smarthome.repository.UserRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * Service for managing user notification settings/preferences for events of user interest
 */
@Service
public class UserNotificationService {

    private static final Logger log = LoggerFactory.getLogger(UserNotificationService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserNotificationSettingsRepository notificationSettingsRepo;

    @Autowired
    private EmailNotificationSender emailNotificationSender;

    @Autowired
    private SlackNotificationSender slackNotificationSender;

    /**
     * Notify users for device state change
     *
     * @param deviceId    device UUID
     * @param deviceState state change
     */
    @Transactional(readOnly = true)
    public void sendNotification(String deviceId, DeviceState deviceState) {
        final List<UserNotificationSettingsEntity> preferences = notificationSettingsRepo
                .findAllByDeviceUuidAndEnabledTrue(UUID.fromString(deviceId));

        for (UserNotificationSettingsEntity uns : preferences) {
            switch (uns.getNotificationType()) {
                case EMAIL:
                    emailNotificationSender.send(
                            uns.getNotificationDestination(),
                            "Notification update for device " + deviceId,
                            deviceState.toString()
                    );
                    break;
                case SLACK:
                    slackNotificationSender.send(uns.getNotificationDestination(), deviceState.toString());
                    break;
                default:
                    throw new NotImplementedException("Unexpected value: " + uns.getNotificationType());
            }
        }
    }

    /**
     * List all event notification settings for a user
     *
     * @param userId user's uuid
     * @return {@link UserNotificationSettings}
     */
    @Transactional(readOnly = true)
    public List<UserNotificationSettings> getUserNotificationSettings(String userId) {
        log.info("Listing notification settings for user with UUID '{}'", userId);
        final List<UserNotificationSettingsEntity> settings = notificationSettingsRepo
                .findAllByUserUuid(UUID.fromString(userId));

        return settings.stream()
                .collect(groupingBy(ns -> ns.getDevice().getUuid()))
                .entrySet()
                .stream()
                .map((entry -> new UserNotificationSettings(
                        entry.getKey().toString(),
                        entry.getValue().stream()
                                .map(UserNotificationService::toNotificationSetting)
                                .collect(Collectors.toUnmodifiableList())
                )))
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Update user's notification settings related to specific device
     *
     * @param userId   user UUID
     * @param settings {@link UserNotificationSettings}
     * @return updated {@link UserNotificationSettings}
     */
    @Transactional
    public UserNotificationSettings updateUserSettings(String userId, UserNotificationSettings settings) {
        // TODO: add validation // notificationSettingsValidator.validateUpdateRequest(userId, settings);
        final List<UserNotificationSettingsEntity> existingSettings = notificationSettingsRepo.
                findAllByUserUuidAndDeviceUuid(UUID.fromString(userId), UUID.fromString(settings.getDeviceId()));

        final List<NotificationSetting> updated = settings.getNotificationSettings().stream()
                .map(ns -> updateSettings(userId, settings.getDeviceId(), existingSettings, ns))
                .map(UserNotificationService::toNotificationSetting)
                .collect(Collectors.toUnmodifiableList());

        return new UserNotificationSettings(settings.getDeviceId(), updated);
    }

    /**
     * Updates {@link UserNotificationSettingsEntity}
     *
     * @param userId           user UUID
     * @param deviceId         device UUID
     * @param existingSettings list of existing settings to update
     * @param newSetting       the new settings
     * @return updated {@link UserNotificationSettingsEntity}
     */
    private UserNotificationSettingsEntity updateSettings(String userId,
                                                          String deviceId,
                                                          List<UserNotificationSettingsEntity> existingSettings,
                                                          NotificationSetting newSetting) {
        final Optional<UserNotificationSettingsEntity> sameTypeExisting = existingSettings.stream()
                .filter(es -> es.getNotificationType() == newSetting.getType())
                .findAny();

        // If newSetting of given type matches existing setting, then update the existing one:
        if (sameTypeExisting.isPresent()) {
            log.info("Updating existing notification setting: [{}]", newSetting);
            final UserNotificationSettingsEntity entity = sameTypeExisting.get();
            entity.setEnabled(newSetting.isEnabled());
            entity.setNotificationDestination(newSetting.getDestination());
            return notificationSettingsRepo.save(entity);
        }

        // Otherwise, create new record for the given userId, deviceId and notification setting:
        log.info("Creating new notification setting: [{}]", newSetting);
        return notificationSettingsRepo.save(toNotificationSettingsEntity(userId, deviceId, newSetting));
    }

    private UserNotificationSettingsEntity toNotificationSettingsEntity(String userId,
                                                                        String deviceId,
                                                                        NotificationSetting ns) {
        final UserNotificationSettingsEntity entity = new UserNotificationSettingsEntity();

        entity.setDevice(deviceRepository.findByUuid(UUID.fromString(deviceId))
                                 .orElseThrow(() -> new DeviceNotFoundException("deviceUuid", deviceId)));

        entity.setUser(userRepository.findByUuid(UUID.fromString(userId))
                               .orElseThrow(() -> new UserNotFoundException("userUuid", userId)));

        entity.setEnabled(ns.isEnabled());
        entity.setNotificationType(ns.getType());
        entity.setNotificationDestination(ns.getDestination());

        return entity;
    }

    private static NotificationSetting toNotificationSetting(UserNotificationSettingsEntity settings) {
        return new NotificationSetting(
                settings.getNotificationType(),
                settings.getNotificationDestination(),
                settings.isEnabled()
        );
    }

}