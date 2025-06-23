package com.shongon.restaurant_backend.controller;

import com.shongon.restaurant_backend.domain.dto.ErrorDTO;
import com.shongon.restaurant_backend.exception.BaseException;
import com.shongon.restaurant_backend.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("Caught MethodArgumentNotValidException: {}",e.getMessage());

        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();

        return new ResponseEntity<>(errorDTO,HttpStatus.BAD_REQUEST);
    }

    // Handle storage-related exceptions
    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorDTO> handleStorageException(StorageException e){
        log.error("Caught StorageException: {}",e.getMessage());

        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unable to save or retrieve resources at this time")
                .build();

        return new ResponseEntity<>(errorDTO,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle our base application exception
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorDTO> handleBaseException(BaseException e){
        log.error("Caught BaseException: {}",e.getMessage());

        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .build();

        return new ResponseEntity<>(errorDTO,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Catch-all for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception e){
        log.error("Caught unexpected exceptions: {}",e.getMessage());

        ErrorDTO errorDTO = ErrorDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .build();

        return new ResponseEntity<>(errorDTO,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
