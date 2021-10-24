package com.iot.smarthome.repository;

import com.iot.smarthome.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthoritiesRepository extends JpaRepository<AuthorityEntity, Long> {

    Optional<AuthorityEntity> findByAuthority(String authority);

    AuthorityEntity getByAuthority(String authority);
}
