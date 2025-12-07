package com.selahattindev.portfolio.dto.request;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ProjectRequestDto {
    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String title;
    private String description;
    private String techStack;
    private String githubUrl;
    private String liveUrl;
    private int viewCount;
}
