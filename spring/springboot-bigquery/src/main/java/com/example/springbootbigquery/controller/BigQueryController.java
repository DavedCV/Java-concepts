package com.example.springbootbigquery.controller;

import com.example.springbootbigquery.exceptions.BigQueryManualException;
import com.example.springbootbigquery.service.BigQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BigQueryController {

    private final BigQueryService bigQueryService;

    @Autowired
    public BigQueryController(BigQueryService bigQueryService) {
        this.bigQueryService = bigQueryService;
    }

    @GetMapping("/country/all")
    public ResponseEntity<List<String>> searchCountryOptions() throws BigQueryManualException {
        List<String> countries = bigQueryService.queryCountries();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(countries);
    }
}
