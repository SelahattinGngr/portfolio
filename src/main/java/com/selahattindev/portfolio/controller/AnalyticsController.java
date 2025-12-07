package com.selahattindev.portfolio.controller;

import com.selahattindev.portfolio.response.ApiResponse;
import com.selahattindev.portfolio.service.domain.AnalyticsService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // 1. Site Ziyareti Kaydetme (Frontend'de App.js veya Layout içine koyulacak)
    @PostMapping("/visit")
    public ResponseEntity<ApiResponse<String>> logVisit(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        analyticsService.logVisit(ip, userAgent, "HOME_PAGE");
        return ResponseEntity.ok(ApiResponse.success("Ziyaret kaydedildi"));
    }

    // 2. Proje Görüntülenme Artırma
    @PostMapping("/project/{id}/view")
    public ResponseEntity<ApiResponse<String>> incrementProjectView(@PathVariable Long id) {
        analyticsService.incrementProjectView(id);
        return ResponseEntity.ok(ApiResponse.success("Görüntülenme artırıldı"));
    }
}