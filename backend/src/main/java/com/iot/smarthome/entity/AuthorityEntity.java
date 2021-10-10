package com.iot.smarthome.entity;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class AuthorityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Column(name = "authority")
    private String authority;

    public AuthorityEntity() {
        // no-args constructor required by JPA spec
    }

    public AuthorityEntity(String authority) {
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "AuthorityEntity{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }
}
