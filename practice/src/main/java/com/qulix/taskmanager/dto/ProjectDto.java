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
public class ProjectDto {

    private Integer id;

    @NotEmpty(message = "Please, enter name")
    @Size(min = 1, max = 100, message = "Name length must be between 1 and 100")
    private String name;

    @NotEmpty(message = "Please, enter short name")
    @Size(min = 1, max = 45, message = "Short name length must be between 1 and 45")
    private String shortName;

    @Size(max = 1000, message = "Description length mustn't be more than 1000")
    private String description;
}
