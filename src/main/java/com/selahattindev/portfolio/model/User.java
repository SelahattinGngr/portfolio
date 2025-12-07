package com.selahattindev.portfolio.model;

import com.selahattindev.portfolio.utils.enums.Roles;
import com.selahattindev.portfolio.utils.enums.TwoFaType;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Entity
@Builder
@Table(name = "users")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Email
    @Column(name = "email", nullable = true, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "roles", nullable = false)
    private String roles;

    @Enumerated(EnumType.STRING)
    @Column(name = "two_fa_type", nullable = false)
    private TwoFaType twoFaType;

    @Column(name = "two_fa_secret")
    private String twoFaSecret;

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        this.twoFaType = TwoFaType.NONE;
        if (this.roles == null) {
            this.roles = Roles.ROLE_USER.name();
        }
    }
}