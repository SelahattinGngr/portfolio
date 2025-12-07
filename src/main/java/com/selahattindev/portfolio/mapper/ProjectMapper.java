package com.selahattindev.portfolio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // Bunu ekle
import org.mapstruct.factory.Mappers;

import com.selahattindev.portfolio.dto.request.ProjectRequestDto;
import com.selahattindev.portfolio.model.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectRequestDto toDTO(Project project);

    @Mapping(target = "viewCount", ignore = true)
    Project toEntity(ProjectRequestDto projectDTO);

    List<ProjectRequestDto> toDtoList(List<Project> projects);
}