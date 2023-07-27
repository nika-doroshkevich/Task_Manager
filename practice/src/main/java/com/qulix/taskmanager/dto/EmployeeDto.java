package com.qulix.taskmanager.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Integer id;

    @NotEmpty(message = "Please, enter last name")
    @Size(min = 1, max = 100, message = "Last name length must be between 1 and 100")
    private String lastName;

    @NotEmpty(message = "Please, enter first name")
    @Size(min = 1, max = 100, message = "First name length must be between 1 and 100")
    private String firstName;

    @Size(max = 100, message = "Patronymic length mustn't be more than 100")
    private String patronymic;

    @NotEmpty(message = "Please, enter post")
    @Size(min = 1, max = 100, message = "Post length must be between 1 and 100")
    private String post;
}
