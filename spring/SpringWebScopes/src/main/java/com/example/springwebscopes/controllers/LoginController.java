package com.example.springwebscopes.controllers;

import com.example.springwebscopes.models.LoginProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// We use the @Controller stereotype annotation
// to define the class as a Spring MVC controller.
@Controller
public class LoginController {

    private final LoginProcessor loginProcessor;

    @Autowired
    public LoginController(LoginProcessor loginProcessor) {
        this.loginProcessor = loginProcessor;
    }

    // We map the controller’s action to the
    // root ("/ ") path of the application.
    @GetMapping("/")
    public String loginGet() {

        // We return the view name we
        // want to be rendered by the app.
        return "login.html";
    }

    // We are mapping the controller’s action to
    // the HTTP POST request of the login page
    @PostMapping("/")
    public String loginPost(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    )
    {
        loginProcessor.setUsername(username);
        loginProcessor.setPassword(password);
        boolean loggedIn = loginProcessor.login();

        // When the user successfully authenticates,
        // the app redirects them to the main page.
        if (loggedIn) return "redirect:/main";

        model.addAttribute("message", "Login Failed!");
        return "login.html";
    }
}
