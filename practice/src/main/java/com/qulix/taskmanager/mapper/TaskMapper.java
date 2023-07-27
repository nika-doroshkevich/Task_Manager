package com.qulix.taskmanager.mapper;

import com.qulix.taskmanager.dto.TaskDto;
import com.qulix.taskmanager.model.Task;

import static com.qulix.taskmanager.enums.TaskStatus.NOT_STARTED;

public class TaskMapper {

    public static Task toEntity(TaskDto taskDto) {
        return Task.builder()
                .name(taskDto.getName())
                .hours(taskDto.getHours())
                .startDate(taskDto.getStartDate())
                .finishDate(taskDto.getFinishDate())
                .status(NOT_STARTED)
                .build();
    }

    public static TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .hours(task.getHours())
                .startDate(task.getStartDate())
                .finishDate(task.getFinishDate())
                .status(task.getStatus())
                .build();
    }

    public static void updateTaskDto(Task task, TaskDto taskDtoToUpdate) {
        taskDtoToUpdate.setId(task.getId());
        taskDtoToUpdate.setName(task.getName());
        taskDtoToUpdate.setHours(task.getHours());
        taskDtoToUpdate.setStartDate(task.getStartDate());
        taskDtoToUpdate.setFinishDate(task.getFinishDate());
        taskDtoToUpdate.setStatus(task.getStatus());
    }

    public static void updateTask(TaskDto taskDto, Task taskToUpdate) {
        taskToUpdate.setName(taskDto.getName());
        taskToUpdate.setHours(taskDto.getHours());
        taskToUpdate.setStartDate(taskDto.getStartDate());
        taskToUpdate.setFinishDate(taskDto.getFinishDate());
    }
}
