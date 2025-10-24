package com.selahattindev.portfolio.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Entity
@Table(name = "projects")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseModel {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT", length = 2000)
    private String description;

    @Column(name = "tech_stack", nullable = false)
    private String techStack;

    @Column(name = "github_url", nullable = true)
    private String githubUrl;

    @Column(name = "live_url", nullable = true)
    private String liveUrl;

    public Project() {
        this.title = "";
        this.description = "";
        this.techStack = "";
        this.githubUrl = "";
        this.liveUrl = "";
    }

    @PrePersist
    public void prePersist() {
        Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }
}
