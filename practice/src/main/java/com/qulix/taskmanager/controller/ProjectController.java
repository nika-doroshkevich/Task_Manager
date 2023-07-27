package com.qulix.taskmanager.controller;

import com.qulix.taskmanager.dto.ProjectDto;
import com.qulix.taskmanager.dto.TaskDto;
import com.qulix.taskmanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/project-list")
    public List<ProjectDto> getAllActiveProjects() {
        return projectService.getAllActiveProjects();
    }

    @GetMapping("/task-list/{projectId}")
    public List<TaskDto> getAllNotDeletedTasksForProject(@PathVariable Integer projectId) {
        return projectService.getAllNotDeletedTasksForProject(projectId);
    }

    @GetMapping("/{id}")
    public ProjectDto getProject(@PathVariable Integer id) {
        return projectService.getProject(id);
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public ProjectDto add(@RequestBody ProjectDto projectDto) {
        return projectService.addProject(projectDto);
    }

    @PutMapping(value = "/update", consumes = "application/json")
    public ProjectDto update(@RequestBody ProjectDto projectDto) {
        return projectService.updateProject(projectDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/delete/{id}")
    public void delete(@PathVariable Integer id) {
        projectService.deleteProject(id);
    }
}
