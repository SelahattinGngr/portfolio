package com.selahattindev.portfolio.utils;

public enum Roles {
    ROLE_ADMIN,
    ROLE_USER;

    @Override
    public String toString() {
        return this.name();
    }
}
