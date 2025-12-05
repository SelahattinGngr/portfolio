package com.selahattindev.portfolio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.ProjectRequestDto;
import com.selahattindev.portfolio.mapper.ProjectMapper;
import com.selahattindev.portfolio.model.Project;
import com.selahattindev.portfolio.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper mapper;

    public List<ProjectRequestDto> getAllProjects() {
        return mapper.toDtoList(projectRepository.findAll());
    }

    public ProjectRequestDto createProject(ProjectRequestDto projectDTO) {
        Project project = mapper.toEntity(projectDTO);
        Project savedProject = projectRepository.save(project);
        return mapper.toDTO(savedProject);
    }

    public ProjectRequestDto updateProject(Long id, ProjectRequestDto dto) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        existing.setTitle(dto.getTitle())
                .setDescription(dto.getDescription())
                .setTechStack(dto.getTechStack())
                .setGithubUrl(dto.getGithubUrl())
                .setLiveUrl(dto.getLiveUrl());
        Project updated = projectRepository.save(existing);
        return mapper.toDTO(updated);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public ProjectRequestDto getProjectById(Long id) {
        return mapper.toDTO(projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id)));
    }
}
