package com.qulix.taskmanager.dto;

import com.qulix.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Integer id;

    @NotEmpty(message = "Please, enter name")
    @Size(min = 1, max = 200, message = "Name length must be between 1 and 200")
    private String name;

    private Integer hours;
    private LocalDate startDate;
    private LocalDate finishDate;
    private List<EmployeeDto> taskPerformers;

    @NotNull(message = "Status can't be null")
    private TaskStatus status;
}
