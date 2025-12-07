package com.selahattindev.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.selahattindev.portfolio.model.VisitLog;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {
    // TODO: İlerde "Bugünkü ziyaretçiler" gibi istatistikler için buraya Query
    // yazacağız.
}
