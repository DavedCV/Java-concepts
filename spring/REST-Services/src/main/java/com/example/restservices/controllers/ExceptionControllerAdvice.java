package com.example.restservices.controllers;

import com.example.restservices.exceptions.NotEnoughMoneyException;
import com.example.restservices.models.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// We use the @RestControllerAdvice
// annotation to mark the class as a
// REST controller advice.
@RestControllerAdvice
public class ExceptionControllerAdvice {

    // We use the @ExceptionHandler method to associate
    // an exception with the logic the method implements.
    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<ErrorDetails> exceptionNotEnoughMoneyHandler(NotEnoughMoneyException e) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDetails);
    }
}
