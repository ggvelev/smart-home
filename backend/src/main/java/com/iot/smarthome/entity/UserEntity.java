package com.iot.smarthome.entity;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.iot.smarthome.security.SecurityConstants.ROLE_ADMIN;

/**
 * Representation of a User
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid = UUID.randomUUID();

    @Version
    @Column(name = "opt_lock_version")
    private Integer version;

    @NotBlank
    @Column(name = "username")
    private String username;

    @Email
    @NotBlank
    @Length(max = 64)
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "locked")
    private boolean locked;

    @Column(name = "expired")
    private boolean expired;

    @Column(name = "credentials_expired")
    private boolean credentialsExpired;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<AuthorityEntity> authorities = new HashSet<>();

    public UserEntity() {
        // no-args constructor required by JPA spec
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(AuthorityEntity::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    public String getAuthoritiesAsCommaSeparatedString() {
        return getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public Set<String> getAuthoritiesAsSet() {
        return getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public void setAuthorities(Set<AuthorityEntity> authorities) {
        this.authorities = authorities;
    }

    public boolean hasAnyAuthority(String authority) {
        return getAuthoritiesAsSet().contains(authority);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", version=" + version +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password=[CENSORED]" +
                ", enabled=" + enabled +
                ", locked=" + locked +
                ", expired=" + expired +
                ", credentialsExpired=" + credentialsExpired +
                ", authorities=" + authorities +
                '}';
    }

}