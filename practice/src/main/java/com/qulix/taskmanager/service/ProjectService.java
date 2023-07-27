package com.qulix.taskmanager.service;

import com.qulix.taskmanager.dto.ProjectDto;
import com.qulix.taskmanager.dto.TaskDto;

import java.util.List;

public interface ProjectService {

    List<ProjectDto> getAllActiveProjects();

    List<TaskDto> getAllNotDeletedTasksForProject(Integer projectId);

    ProjectDto getProject(Integer id);

    ProjectDto addProject(ProjectDto projectDto);

    ProjectDto updateProject(ProjectDto projectDto);

    void deleteProject(Integer id);
}
