package com.selahattindev.portfolio.service.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate; // DİKKAT
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final StringRedisTemplate redisTemplate;
    private static final String OTP_PREFIX = "otp:";

    public String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void saveOtp(String username, String otp) {
        redisTemplate.opsForValue()
                .set(OTP_PREFIX + username, otp, Duration.ofMinutes(3));
    }

    public boolean validateOtp(String username, String inputOtp) {
        String storedOtp = redisTemplate.opsForValue().get(OTP_PREFIX + username);

        if (storedOtp != null && storedOtp.equals(inputOtp)) {
            redisTemplate.delete(OTP_PREFIX + username);
            return true;
        }
        return false;
    }
}