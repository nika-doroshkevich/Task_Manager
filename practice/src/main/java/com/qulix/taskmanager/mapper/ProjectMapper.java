package com.qulix.taskmanager.mapper;

import com.qulix.taskmanager.dto.ProjectDto;
import com.qulix.taskmanager.model.Project;

import static com.qulix.taskmanager.enums.ProjectStatus.ACTIVE;

public class ProjectMapper {

    public static Project toEntity(ProjectDto projectDto) {
        return Project.builder()
                .name(projectDto.getName())
                .shortName(projectDto.getShortName())
                .description(projectDto.getDescription())
                .status(ACTIVE)
                .build();
    }

    public static ProjectDto toDto(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .shortName(project.getShortName())
                .description(project.getDescription())
                .build();
    }

    public static void updateProjectDto(Project project, ProjectDto projectDtoToUpdate) {
        projectDtoToUpdate.setId(project.getId());
        projectDtoToUpdate.setName(project.getName());
        projectDtoToUpdate.setShortName(project.getShortName());
        projectDtoToUpdate.setDescription(project.getDescription());
    }

    public static void updateProject(ProjectDto projectDto, Project projectToUpdate) {
        projectToUpdate.setName(projectDto.getName());
        projectToUpdate.setShortName(projectDto.getShortName());
        projectToUpdate.setDescription(projectDto.getDescription());
    }
}
