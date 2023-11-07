package com.example.restservices.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Instead of repeating the @ResponseBody
// annotation for each method, we replace
// @Controller with @RestController.
@RestController
public class HelloController {

    // We use the @GetMapping annotation to associate the GET
    // HTTP method and a path with the controller’s action.
    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @GetMapping("/ciao")
    public String ciao() {
        return "Ciao!";
    }
}
