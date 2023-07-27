package com.qulix.taskmanager.controller;

import com.qulix.taskmanager.dto.TaskDto;
import com.qulix.taskmanager.service.TaskService;
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
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/task-list")
    public List<TaskDto> getAllNotDeletedTasks() {
        return taskService.getAllNotDeletedTasks();
    }

    @PostMapping(value = "/add/{projectId}", consumes = "application/json")
    public TaskDto add(@RequestBody TaskDto taskDto, @PathVariable Integer projectId) {
        return taskService.addTask(taskDto, projectId);
    }

    @PutMapping(value = "/update", consumes = "application/json")
    public TaskDto update(@RequestBody TaskDto taskDto) {
        return taskService.updateTask(taskDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/delete/{id}")
    public void delete(@PathVariable Integer id) {
        taskService.deleteTask(id);
    }
}
