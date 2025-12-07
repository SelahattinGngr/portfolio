package com.selahattindev.portfolio.service.infra;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwoFactorService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public String generateNewSecret() {
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public String generateQrCodeImageUri(String secret, String username) {
        GoogleAuthenticatorKey key = new GoogleAuthenticatorKey.Builder(secret).build();

        return GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("selahattin.dev", username, key);
    }

    public boolean isOtpValid(String secret, int code) {
        return gAuth.authorize(secret, code);
    }
}