package com.hottabych04.app.controller;

import com.hottabych04.app.exception.EntityNotFoundException;
import com.hottabych04.app.exception.TaskManagerRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(TaskManagerRuntimeException.class)
    public ProblemDetail handleTaskManagerException(TaskManagerRuntimeException e){
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                e.getMessage()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleNotFoundException(EntityNotFoundException e){
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage() + " Details: " + e.getEntityDetail()
        );
    }
}
