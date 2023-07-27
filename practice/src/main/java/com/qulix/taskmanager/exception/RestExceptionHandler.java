package com.qulix.taskmanager.exception;

import com.qulix.taskmanager.service.TaskServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {

    Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @ExceptionHandler(value = {RestRequestException.class, EntityValidationException.class})
    public ResponseEntity<Object> handleRestRequestException(Exception e) {
        logger.error(e.getMessage());
        RestException restException = new RestException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(restException, HttpStatus.BAD_REQUEST);
    }
}
