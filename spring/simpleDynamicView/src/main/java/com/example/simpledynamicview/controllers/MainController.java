package com.example.simpledynamicview.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {


    @RequestMapping("/home")
    // We define a new parameter for the controller’s action method and annotate it with @RequestParam.
    public String home(Model page, @RequestParam(required = false) String color) {
        page.addAttribute("username", "David");

        if (color != null) page.addAttribute("color", color);
        else page.addAttribute("color", "blue");

        return "home.html";
    }
}
