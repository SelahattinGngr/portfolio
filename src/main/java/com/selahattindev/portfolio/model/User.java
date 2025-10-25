package com.selahattindev.portfolio.model;

import java.sql.Timestamp;

import com.selahattindev.portfolio.utils.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "users")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "roles", nullable = false)
    private String roles;

    @PrePersist
    public void prePersist() {
        if (this.roles == null) {
            this.roles = Roles.USER.name();
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }
}
