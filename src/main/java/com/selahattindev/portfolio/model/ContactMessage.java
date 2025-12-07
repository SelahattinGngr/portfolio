package com.selahattindev.portfolio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Entity
@Builder
@Table(name = "contact_messages")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(name = "is_read")
    @Builder.Default
    private boolean isRead = false;
}