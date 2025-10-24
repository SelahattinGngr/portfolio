package com.selahattindev.portfolio.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "selahattindev.jwt")
public class JwtProperties {

    private String refreshSecretKey = "";
    private String accessSecretKey = "";

    public String getRefreshSecretKey() {
        return refreshSecretKey;
    }

    public void setRefreshSecretKey(String refreshSecretKey) {
        this.refreshSecretKey = refreshSecretKey;
    }

    public String getAccessSecretKey() {
        return accessSecretKey;
    }

    public void setAccessSecretKey(String accessSecretKey) {
        this.accessSecretKey = accessSecretKey;
    }

}
