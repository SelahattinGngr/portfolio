package com.selahattindev.portfolio.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactRequestDto {
    @NotBlank(message = "İsim boş olamaz")
    private String name;

    @Email(message = "Geçerli bir email girin")
    @NotBlank
    private String email;

    @NotBlank(message = "Konu boş olamaz")
    private String subject;

    @NotBlank(message = "Mesaj boş olamaz")
    private String message;
}