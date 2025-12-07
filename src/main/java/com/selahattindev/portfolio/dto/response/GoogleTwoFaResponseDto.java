package com.selahattindev.portfolio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleTwoFaResponseDto {
    private String secretKey;
    private String qrCodeUrl;
}