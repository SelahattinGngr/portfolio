package com.selahattindev.portfolio.utils;

public enum CookieConstants {
    ACCESS_TOKEN("accessToken"),
    REFRESH_TOKEN("refreshToken"),
    DEVICE_ID("deviceId");

    private final String cookieName;

    CookieConstants(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getCookieName() {
        return cookieName;
    }
}
