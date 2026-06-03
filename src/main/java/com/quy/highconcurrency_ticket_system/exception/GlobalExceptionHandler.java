package com.quy.highconcurrency_ticket_system.exception;

import com.quy.highconcurrency_ticket_system.dto.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->{
            errors.put(error.getField(), error.getDefaultMessage());
        });
        APIResponse<Map<String, String>> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(), "Validation failed", null, errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> DuplicateResourceExceptionHandler(DuplicateResourceException ex){
        String error = ex.getFieldName() +  ": " + ex.getFieldValue();
        APIResponse<Object> response = new APIResponse<>(HttpStatus.CONFLICT.value(), ex.getMessage(), null, error);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> ResourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        String error = ex.getFieldName() +  ": " + ex.getFieldValue();
        APIResponse<Object> response = new APIResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null, error);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> IllegalArgumentExceptionHandler(IllegalArgumentException ex){
        APIResponse<Object> response = new APIResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
