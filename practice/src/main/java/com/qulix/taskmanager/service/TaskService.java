package com.qulix.taskmanager.service;

import com.qulix.taskmanager.dto.TaskDto;

import java.util.List;

public interface TaskService {

    List<TaskDto> getAllNotDeletedTasks();

    TaskDto addTask(TaskDto taskDto, Integer projectId);

    TaskDto updateTask(TaskDto taskDto);

    void deleteTask(Integer id);
}
