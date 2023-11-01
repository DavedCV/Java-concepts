package com.example.simpledynamicview.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

// The @Controller stereotype annotation marks
// this class as Spring MVC controller and adds a
// bean of this type to the Spring context.
@Controller
public class MainController {

    // We assign the controller’s action
    // to an HTTP request path.
    @RequestMapping("/home")
    // The action method defines a parameter of type Model that stores the data the
    // controller sends to the view.
    public String home(Model page) {
        // We add the data we want the controller sends to the view
        page.addAttribute("username", "David");
        page.addAttribute("color", "blue");

        // The controller’s action returns the view
        // to be rendered into the HTTP response.
        return "home.html";
    }
}
