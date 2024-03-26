package com.example.plannerentity.global_exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException customException) {

        return ResponseEntity.status(customException.getStatus()).body(
                "Произошла ошибка: " + customException.getMessage() + "\n Статус ошибки: " + customException.getStatus()
        );
    }
}
