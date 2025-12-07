package com.selahattindev.portfolio.service.infra;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Portfolio Giriş Kodu");
            message.setText(content);

            mailSender.send(message);
            log.info("Mail başarıyla gönderildi: {}", toEmail);
        } catch (Exception e) {
            log.error("Mail gönderilemedi: {}", e.getMessage());
            // Retry mekanizması buraya eklenebilir (İleride)
        }
    }
}