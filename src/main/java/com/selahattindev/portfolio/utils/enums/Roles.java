package com.selahattindev.portfolio.utils.enums;

public enum Roles {
    ROLE_ADMIN,
    ROLE_USER;

    @Override
    public String toString() {
        return this.name();
    }
}
