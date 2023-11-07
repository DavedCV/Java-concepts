package com.example.restservices.controllers;

import com.example.restservices.models.Country;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Marking the class as a REST controller to add a bean
// in the Spring context and also inform the dispatcher
// servlet not to look for a view when this method returns
@RestController
public class CountryController {

    @GetMapping("/france")
    public Country france() {
        Country c = Country.of("france", 67);

        // Returning an instance of type Country
        return c;
    }

    @GetMapping("/all")
    public List<Country> all() {
        Country c1 = Country.of("France", 67);
        Country c2 = Country.of("Spain", 47);

        // Returns a collection in the HTTP response body
        return List.of(c1, c2);
    }

    @GetMapping("/colombia")
    public ResponseEntity<Country> colombia() {
        Country c = Country.of("Colombia",  51);

        /*
        * - Changes the HTTP response status 202 Accepted
        * - Adds three custom headers to the response
        * - Sets the response body
        * */
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .header("continent", "Latin America")
                .header("capital", "Bogota")
                .header("favorite_food", "bandeja paisa")
                .body(c);
    }
}
