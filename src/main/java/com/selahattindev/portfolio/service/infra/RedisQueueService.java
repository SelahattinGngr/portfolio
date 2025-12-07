package com.selahattindev.portfolio.service.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.response.EmailMessageDto;

@Service
@RequiredArgsConstructor
public class RedisQueueService {

    private final RedisTemplate<String, Object> redisTemplate;
    public static final String EMAIL_QUEUE = "email_queue";

    public void enqueueEmail(EmailMessageDto emailDto) {
        redisTemplate.opsForList().leftPush(EMAIL_QUEUE, emailDto);
    }
}