package com.selahattindev.portfolio.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selahattindev.portfolio.dto.ProjectRequestDto;
import com.selahattindev.portfolio.service.ProjectService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public List<ProjectRequestDto> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectRequestDto getProjectById(Long id) {
        return projectService.getProjectById(id);
    }

    @PostMapping
    public ProjectRequestDto createProject(@RequestBody ProjectRequestDto dto) {
        return projectService.createProject(dto);
    }

    @PutMapping("/{id}")
    public ProjectRequestDto updateProject(Long id, @RequestBody ProjectRequestDto dto) {
        return projectService.updateProject(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(Long id) {
        projectService.deleteProject(id);
    }

}
