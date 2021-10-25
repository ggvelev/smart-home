/***********************************
 Copyright (c) Georgi Velev 2021.
 **********************************/
package com.iot.smarthome.util;

import com.iot.smarthome.entity.*;
import com.iot.smarthome.notification.NotificationType;
import com.iot.smarthome.repository.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

// TODO - move the entire mess to data.sql init script
@Configuration
public class DatabaseInitializer {

    private final UUID dev1Uuid = UUID.fromString("caf703a4-44ab-4ac5-958b-0d1420167764");
    private final UUID dev2Uuid = UUID.fromString("b683e245-deb1-4d1a-b11f-0de80ad517f6");
    private final UUID dev3Uuid = UUID.fromString("7b8c562d-32a7-424c-a9ae-3a8de1c29b78");

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   UserAuthoritiesRepository authoritiesRepository,
                                   UserNotificationSettingsRepository notificationSettingsRepository,
                                   DeviceRepository deviceRepository,
                                   DevicePropertyRepository devicePropertyRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            initializeDeviceRelatedTables(
                    deviceRepository,
                    devicePropertyRepository
            );
            initializeUserRelatedTables(
                    userRepository,
                    authoritiesRepository,
                    passwordEncoder,
                    notificationSettingsRepository,
                    deviceRepository
            );
        };
    }


    private void initializeDeviceRelatedTables(DeviceRepository deviceRepository,
                                               DevicePropertyRepository devicePropertyRepository) {

        DeviceEntity device1 = new DeviceEntity(dev1Uuid);
        device1.setMetadata(
                new DeviceMetadata("building#1", "floor#1", "room#1", "Device1 - a on/off dimmer", "device-1-name",
                                   "manufacturer1", "serialNumber1"
                ));
        device1.setNetworkSettings(new NetworkSettings("device1-mac", "device1-ip", "device1-nwCidr"));
        device1 = deviceRepository.save(device1);

        DeviceEntity device2 = new DeviceEntity(dev2Uuid);
        device2.setMetadata(
                new DeviceMetadata("building#2", "floor#2", "room#2", "Device2 - a sensor device", "device-2-name",
                                   "manufacturer2", "serialNumber2"
                ));
        device2.setNetworkSettings(new NetworkSettings("device2-mac", "device2-ip", "device2-nwCidr"));
        device2 = deviceRepository.save(device2);

        DeviceEntity device3 = new DeviceEntity(dev3Uuid);
        device3.setMetadata(
                new DeviceMetadata("building#3", "floor#3", "room#3", "Device3 - AC relay + light sensor",
                                   "device-3-name",
                                   "manufacturer3", "serialNumber3"
                ));
        device3.setNetworkSettings(new NetworkSettings("device3-mac", "device3-ip", "device3-nwCidr"));
        device3 = deviceRepository.save(device3);

        // Device1 props - a on-off dimmer device
        final DevicePropertyEntity dev1Prop1 = new DevicePropertyEntity();
        dev1Prop1.setDevice(device1);
        dev1Prop1.setDisplayName("dev1Prop1-DisplayName-" + RandomStringUtils.random(5));
        dev1Prop1.setName(DevicePropertyType.DIMMER);
        dev1Prop1.setType(DevicePropertyValueType.PERCENTAGE);
        dev1Prop1.setWrite(Boolean.TRUE);
        dev1Prop1.setRead(Boolean.TRUE);
        devicePropertyRepository.save(dev1Prop1);

        final DevicePropertyEntity dev1Prop2 = new DevicePropertyEntity();
        dev1Prop2.setDevice(device1);
        dev1Prop2.setDisplayName("dev1Prop2-DisplayName-" + RandomStringUtils.random(5));
        dev1Prop2.setName(DevicePropertyType.ON_OFF_RELAY);
        dev1Prop2.setType(DevicePropertyValueType.BINARY);
        dev1Prop2.setWrite(Boolean.TRUE);
        dev1Prop2.setRead(Boolean.TRUE);
        devicePropertyRepository.save(dev1Prop2);

        // Device2 props - a sensor device, we only read from it
        final DevicePropertyEntity dev2Prop1 = new DevicePropertyEntity();
        dev2Prop1.setDevice(device2);
        dev2Prop1.setDisplayName("dev2Prop1-DisplayName-" + RandomStringUtils.random(5));
        dev2Prop1.setName(DevicePropertyType.MEASURE_FLOAT);
        dev2Prop1.setType(DevicePropertyValueType.FLOAT_NUMBER);
        dev2Prop1.setWrite(Boolean.FALSE);
        dev2Prop1.setRead(Boolean.TRUE);
        devicePropertyRepository.save(dev2Prop1);

        // Device3 props - AC relay with light sensor
        final DevicePropertyEntity dev3Prop1 = new DevicePropertyEntity();
        dev3Prop1.setDevice(device3);
        dev3Prop1.setDisplayName("dev3Prop1-DisplayName-" + RandomStringUtils.random(5));
        dev3Prop1.setName(DevicePropertyType.ON_OFF_RELAY);
        dev3Prop1.setType(DevicePropertyValueType.BINARY);
        dev3Prop1.setWrite(Boolean.TRUE);
        dev3Prop1.setRead(Boolean.TRUE);
        devicePropertyRepository.save(dev3Prop1);

        final DevicePropertyEntity dev3Prop2 = new DevicePropertyEntity();
        dev3Prop2.setDevice(device3);
        dev3Prop2.setDisplayName("dev3Prop2-DisplayName-" + RandomStringUtils.random(5));
        dev3Prop2.setName(DevicePropertyType.MEASURE_FLOAT);
        dev3Prop2.setType(DevicePropertyValueType.FLOAT_NUMBER);
        dev3Prop2.setWrite(Boolean.TRUE);
        dev3Prop2.setRead(Boolean.TRUE);
        devicePropertyRepository.save(dev3Prop2);
    }

    private void initializeUserRelatedTables(UserRepository userRepository,
                                             UserAuthoritiesRepository authoritiesRepository,
                                             PasswordEncoder passwordEncoder,
                                             UserNotificationSettingsRepository notificationSettingsRepository,
                                             DeviceRepository deviceRepository) {
        final String pass = passwordEncoder.encode("pass");

        AuthorityEntity roleAdmin = new AuthorityEntity("ROLE_ADMIN");
        AuthorityEntity roleUser = new AuthorityEntity("ROLE_USER");
        AuthorityEntity roleDevice = new AuthorityEntity("ROLE_DEVICE");
        roleAdmin = authoritiesRepository.saveAndFlush(roleAdmin);
        roleUser = authoritiesRepository.saveAndFlush(roleUser);
        roleDevice = authoritiesRepository.saveAndFlush(roleDevice);

        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setEmail("admin@smarthome.com");
        admin.setEnabled(true);
        admin.setPassword(pass);
        admin.addAuthority(roleAdmin);
        userRepository.save(admin);

        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setEmail("user@smarthome.com");
        user.setEnabled(true);
        user.setPassword(pass);
        user.addAuthority(roleUser);
        user = userRepository.save(user);
        initializeUserNotificationSettingsRelatedTables(
                deviceRepository.findByUuid(dev1Uuid).get(),
                user,
                notificationSettingsRepository,
                "#global-notifications",
                NotificationType.SLACK
        );

        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");
        user2.setEmail("user2@smarthome.com");
        user2.setEnabled(true);
        user2.setPassword(pass);
        user2.addAuthority(roleUser);
        user2 = userRepository.save(user2);
        initializeUserNotificationSettingsRelatedTables(
                deviceRepository.findByUuid(dev2Uuid).get(),
                user2,
                notificationSettingsRepository,
                user2.getEmail(),
                NotificationType.EMAIL
        );

        UserEntity device = new UserEntity();
        device.setUsername("device");
        device.setEmail("device@smarthome.com");
        device.setEnabled(true);
        device.setPassword(pass);
        device.addAuthority(roleDevice);
        userRepository.save(device);

        UserEntity lockedUser = new UserEntity();
        lockedUser.setUsername("lockedUser");
        lockedUser.setEmail("lockedUser@smarthome.com");
        lockedUser.setEnabled(true);
        lockedUser.setLocked(true);
        lockedUser.setPassword(pass);
        lockedUser.addAuthority(roleUser);
        userRepository.save(lockedUser);

        UserEntity disabledUser = new UserEntity();
        disabledUser.setUsername("disabledUser");
        disabledUser.setEmail("disabledUser@smarthome.com");
        disabledUser.setEnabled(false);
        disabledUser.setPassword(pass);
        disabledUser.addAuthority(roleUser);
        userRepository.save(disabledUser);

        UserEntity credsExpiredUser = new UserEntity();
        credsExpiredUser.setUsername("credsExpiredUser");
        credsExpiredUser.setEmail("credsExpiredUser@smarthome.com");
        credsExpiredUser.setEnabled(true);
        credsExpiredUser.setCredentialsExpired(true);
        credsExpiredUser.setPassword(pass);
        credsExpiredUser.addAuthority(roleUser);
        userRepository.save(credsExpiredUser);

        UserEntity expiredUser = new UserEntity();
        expiredUser.setUsername("expiredUser");
        expiredUser.setEmail("expiredUser@smarthome.com");
        expiredUser.setEnabled(true);
        expiredUser.setCredentialsExpired(true);
        expiredUser.setPassword(pass);
        expiredUser.addAuthority(roleUser);
        userRepository.save(expiredUser);
    }

    private void initializeUserNotificationSettingsRelatedTables(
            DeviceEntity device,
            UserEntity user,
            UserNotificationSettingsRepository notificationSettingsRepository,
            String dest,
            NotificationType type) {

        final UserNotificationSettingsEntity ns = new UserNotificationSettingsEntity();
        ns.setUser(user);
        ns.setNotificationType(type);
        ns.setNotificationDestination(dest);
        ns.setDevice(device);
        ns.setEnabled(true);

        notificationSettingsRepository.save(ns);
    }

}
