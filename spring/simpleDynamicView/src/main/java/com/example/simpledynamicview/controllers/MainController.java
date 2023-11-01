package com.example.simpledynamicview.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    // To define a path variable, you assign it
    // a name and put it in the path between
    // curly braces.
    @RequestMapping("/home/{color}")
    // You mark the parameter where you want to get the path variable value
    // with the @PathVariable annotation. The name of the parameter must be
    // the same as the name of the variable in the path.
    public String home(Model page, @PathVariable String color) {
        page.addAttribute("username", "David");
        page.addAttribute("color", color);
        return "home.html";
    }
}
