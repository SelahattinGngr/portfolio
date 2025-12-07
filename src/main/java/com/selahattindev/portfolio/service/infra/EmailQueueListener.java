package com.selahattindev.portfolio.service.infra;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.selahattindev.portfolio.dto.response.EmailMessageDto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailQueueListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailService emailService;

    // Ayrı bir thread havuzu oluşturuyoruz ki ana uygulamayı kilitlemesin
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void startListening() {
        executorService.submit(this::listen);
    }

    private void listen() {
        log.info("Redis Email Kuyruğu dinleniyor...");
        while (true) {
            try {
                // Duration.ofSeconds(5): Liste boşsa 5 saniye bekle (Blockla), sonra tekrar
                // dene. Bu sayede while(true) CPU'yu %100 yapmaz.
                EmailMessageDto emailDto = (EmailMessageDto) redisTemplate.opsForList()
                        .rightPop(RedisQueueService.EMAIL_QUEUE, Duration.ofSeconds(5));

                if (emailDto != null) {
                    log.info("Kuyruktan mail alındı: {}", emailDto.getTo());
                    emailService.sendOtpEmail(emailDto.getTo(), emailDto.getContent());
                }
            } catch (Exception e) {
                log.error("Kuyruk işlenirken hata oluştu: {}", e.getMessage());
                // Hata olursa döngü kırılmasın, biraz bekle devam et
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}