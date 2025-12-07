package com.selahattindev.portfolio.service.domain;

import com.selahattindev.portfolio.dto.request.ContactRequestDto;
import com.selahattindev.portfolio.model.ContactMessage;
import com.selahattindev.portfolio.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public void sendMessage(ContactRequestDto dto) {
        ContactMessage message = ContactMessage.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .subject(dto.getSubject()) // dto'da getter/setter (lombok @Data) var varsayıyorum
                .message(dto.getMessage())
                .isRead(false)
                .build();
        contactRepository.save(message);
    }

    // Admin: Tüm mesajları gör
    public List<ContactMessage> getAllMessages() {
        return contactRepository.findAll();
    }

    // Admin: Mesajı okundu işaretle
    public void markAsRead(Long id) {
        ContactMessage msg = contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Mesaj bulunamadı"));
        msg.setRead(true);
        contactRepository.save(msg);
    }

    // Admin: Mesajı sil
    public void deleteMessage(Long id) {
        contactRepository.deleteById(id);
    }
}