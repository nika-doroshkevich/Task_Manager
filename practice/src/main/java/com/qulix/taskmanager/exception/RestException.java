package com.qulix.taskmanager.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RestException {

    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime localDateTime;
}
