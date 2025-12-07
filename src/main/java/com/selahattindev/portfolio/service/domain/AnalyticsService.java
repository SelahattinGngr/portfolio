package com.selahattindev.portfolio.service.domain;

import com.selahattindev.portfolio.model.Project;
import com.selahattindev.portfolio.model.VisitLog;
import com.selahattindev.portfolio.repository.ProjectRepository;
import com.selahattindev.portfolio.repository.VisitLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ProjectRepository projectRepository;
    private final VisitLogRepository visitLogRepository;

    // 1. Proje Görüntülenme Artır (Redis kullanmıyoruz şimdilik, DB update yeterli)
    // TODO: İleride performans sorunları yaşarsak Redis'e taşıyabiliriz.
    @Transactional
    public void incrementProjectView(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Proje bulunamadı"));
        project.setViewCount(project.getViewCount() + 1);
        projectRepository.save(project);
    }

    // 2. Site Ziyareti Kaydet
    public void logVisit(String ip, String userAgent, String endpoint) {
        VisitLog log = VisitLog.builder()
                .ipAddress(ip)
                .userAgent(userAgent)
                .endpoint(endpoint)
                .build();
        visitLogRepository.save(log);
    }

    // 3. İstatistikleri Getir (Admin Dashboard İçin)
    public long getTotalVisits() {
        return visitLogRepository.count();
    }

    // TODO: Buraya "Bugünkü ziyaretçiler", "En çok girilen endpointler" gibi
    // metodlar eklenecek
}