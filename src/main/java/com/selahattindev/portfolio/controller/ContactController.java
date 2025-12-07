package com.selahattindev.portfolio.controller;

import com.selahattindev.portfolio.dto.request.ContactRequestDto;
import com.selahattindev.portfolio.model.ContactMessage;
import com.selahattindev.portfolio.response.ApiResponse;
import com.selahattindev.portfolio.service.domain.ContactService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> sendMessage(@RequestBody @Valid ContactRequestDto dto) {
        contactService.sendMessage(dto);
        return ResponseEntity.ok(ApiResponse.success("Mesajınız iletildi."));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ContactMessage>>> getAllMessages() {
        return ResponseEntity.ok(ApiResponse.success("Mesajlar getirildi", contactService.getAllMessages()));
    }

    // mark-read ve delete endpointlerini de sen eklersin, tembellik yapma.
}