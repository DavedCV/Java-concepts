package com.example.springbootintro;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// We annotate the class with the @Controller stereotype annotation.
@Controller
public class MainController {

    // We use the @RequestMapping annotation to
    // associate the action with an HTTP request path.
    @RequestMapping("/home")
    public String home() {

        // We return the HTML document name that contains
        // the details we want the browser to display.
       return "index.html";
    }
}
