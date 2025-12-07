package com.selahattindev.portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.selahattindev.portfolio.model.ContactMessage;

public interface ContactRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findByIsReadFalse();
}