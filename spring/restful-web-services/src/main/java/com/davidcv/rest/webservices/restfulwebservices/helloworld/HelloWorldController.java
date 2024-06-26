package com.davidcv.rest.webservices.restfulwebservices.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("helloWorld")
    public String helloWorld() {
        return "Hello World!";
    }

    @GetMapping("helloWorldBean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean();
    }

    @GetMapping("helloWorld/pathVariable/{name}")
    public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
        return new HelloWorldBean(name);
    }
}
