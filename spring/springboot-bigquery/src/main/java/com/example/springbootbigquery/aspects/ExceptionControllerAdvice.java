package com.example.springbootbigquery.aspects;

import com.example.springbootbigquery.exceptions.BigQueryManualException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(BigQueryManualException.class)
    public ResponseEntity<String> handleBigQueryCustomException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

}
