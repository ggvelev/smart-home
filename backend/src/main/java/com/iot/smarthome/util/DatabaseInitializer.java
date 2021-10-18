/***********************************
 Copyright (c) Georgi Velev 2021.
 **********************************/
package com.iot.smarthome.util;

import com.iot.smarthome.entity.AuthorityEntity;
import com.iot.smarthome.entity.UserEntity;
import com.iot.smarthome.repository.DeviceRepository;
import com.iot.smarthome.repository.UserAuthoritiesRepository;
import com.iot.smarthome.repository.UserNotificationSettingsRepository;
import com.iot.smarthome.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   UserAuthoritiesRepository authoritiesRepository,
                                   UserNotificationSettingsRepository notificationSettingsRepository,
                                   DeviceRepository deviceRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
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
            userRepository.save(user);

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
        };
    }

}
