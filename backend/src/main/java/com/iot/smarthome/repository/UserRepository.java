package com.iot.smarthome.repository;

import com.iot.smarthome.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUuid(UUID userUuid);

    void deleteByUuid(UUID userUuid);

    boolean existsByUuid(UUID userUuid);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

}
