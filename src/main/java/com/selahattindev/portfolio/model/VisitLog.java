package com.selahattindev.portfolio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Entity
@Builder
@Table(name = "visit_logs")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VisitLog extends BaseModel {
    private String ipAddress;
    private String userAgent;
    private String endpoint;
}