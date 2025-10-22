package com.selahattindev.portfolio.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String title;
    private String description;
    private String techStack;
    private String githubUrl;
    private String liveUrl;
}
