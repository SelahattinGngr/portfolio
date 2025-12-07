package com.selahattindev.portfolio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyTwoFaRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    private String code;
}