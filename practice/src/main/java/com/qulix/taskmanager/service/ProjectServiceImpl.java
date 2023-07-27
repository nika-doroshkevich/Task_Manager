package com.qulix.taskmanager.service;

import com.qulix.taskmanager.dto.ProjectDto;
import com.qulix.taskmanager.dto.TaskDto;
import com.qulix.taskmanager.enums.ProjectStatus;
import com.qulix.taskmanager.enums.TaskStatus;
import com.qulix.taskmanager.exception.EntityValidationException;
import com.qulix.taskmanager.exception.RestRequestException;
import com.qulix.taskmanager.mapper.EmployeeMapper;
import com.qulix.taskmanager.mapper.ProjectMapper;
import com.qulix.taskmanager.mapper.TaskMapper;
import com.qulix.taskmanager.model.Project;
import com.qulix.taskmanager.model.Task;
import com.qulix.taskmanager.repository.ProjectRepository;
import com.qulix.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    public final ProjectRepository projectRepository;
    public final TaskRepository taskRepository;
    public final TaskService taskService;

    @Override
    public List<ProjectDto> getAllActiveProjects() {
        List<Project> projects = projectRepository.findByStatusIsNot(ProjectStatus.DELETED);
        List<ProjectDto> projectDtoList = new ArrayList<>();
        for (Project project : projects) {
            ProjectDto newProjectDto = new ProjectDto();
            ProjectMapper.updateProjectDto(project, newProjectDto);
            projectDtoList.add(newProjectDto);
        }
        return projectDtoList;
    }

    @Override
    public List<TaskDto> getAllNotDeletedTasksForProject(Integer projectId) {
        validateForDeletion(projectId);

        Project project = getById(projectId);
        List<Task> tasks = project.getTasks();
        return tasks.stream()
                .filter(task -> task.getStatus() != TaskStatus.DELETED)
                .map(task -> {
                    TaskDto newTaskDto = new TaskDto();
                    TaskMapper.updateTaskDto(task, newTaskDto);
                    newTaskDto.setTaskPerformers(EmployeeMapper.toDtoList(task.getEmployees()));
                    return newTaskDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto getProject(Integer id) {
        validateForDeletion(id);

        Project project = getById(id);
        return ProjectMapper.toDto(project);
    }

    @Override
    public ProjectDto addProject(ProjectDto projectDto) {
        validate(projectDto);
        Project project = ProjectMapper.toEntity(projectDto);
        Project savedProject = projectRepository.save(project);
        return ProjectMapper.toDto(savedProject);
    }

    @Override
    public ProjectDto updateProject(ProjectDto projectDto) {
        validateForDeletion(projectDto.getId());
        validate(projectDto);

        Project foundProject = getById(projectDto.getId());
        ProjectMapper.updateProject(projectDto, foundProject);
        Project project = projectRepository.save(foundProject);
        ProjectDto newProjectDto = new ProjectDto();
        ProjectMapper.updateProjectDto(project, newProjectDto);
        return newProjectDto;
    }

    @Override
    public void deleteProject(Integer id) {
        validateForDeletion(id);

        Project project = getById(id);
        project.setStatus(ProjectStatus.DELETED);

        for (Task task : project.getTasks()) {
            taskService.deleteTask(task.getId());
        }

        projectRepository.save(project);
    }

    private Project getById(Integer id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RestRequestException("Project with id " + id + " not found"));
    }

    private void validate(ProjectDto projectDto) {
        String name = projectDto.getName();
        String shortName = projectDto.getShortName();
        if (projectRepository.findByNameAndStatusIsNot(name, ProjectStatus.DELETED) != null) {
            throw new EntityValidationException("Project with name " + name + " already exists");
        }
        if (projectRepository.findByShortNameAndStatusIsNot(shortName, ProjectStatus.DELETED) != null) {
            throw new EntityValidationException("Project with short name " + shortName + " already exists");
        }
    }

    private void validateForDeletion(Integer id) {
        Project project = getById(id);
        if (project.getStatus() == ProjectStatus.DELETED) {
            throw new EntityValidationException("Project has deleted");
        }
    }
}
