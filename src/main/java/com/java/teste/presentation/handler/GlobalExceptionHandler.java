package com.java.teste.presentation.handler;

import com.java.teste.domain.exception.PersonAlreadyExistsException;
import com.java.teste.presentation.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PersonAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePersonAlreadyExists(PersonAlreadyExistsException ex) {
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
