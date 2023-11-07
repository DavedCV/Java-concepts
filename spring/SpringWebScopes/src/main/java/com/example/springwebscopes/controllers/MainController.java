package com.example.springwebscopes.controllers;

import com.example.springwebscopes.services.LoggedUserManagementService;
import com.example.springwebscopes.services.LoginCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final LoggedUserManagementService loggedUserManagementService;
    private final LoginCountService loginCountService;

    @Autowired
    public MainController(LoggedUserManagementService loggedUserManagementService,
                          LoginCountService loginCountService) {
        this.loggedUserManagementService = loggedUserManagementService;
        this.loginCountService = loginCountService;
    }

    @GetMapping("/main")
    public String home(
            @RequestParam(required = false) String logout,
            Model model
    )
    {
        // If the logout parameter is present, we erase the
        // username from the LoggedUserManagementService bean.
        if (logout != null) {
            loggedUserManagementService.setUsername(null);
        }

        // We take the username value, which should be different than null if someone logged in.
        String username = loggedUserManagementService.getUsername();
        // Gets the count from the application-scoped bean
        int count = loginCountService.getCount();

        // If the user is not logged in, we redirect the user to the login page.
        if (username == null) return "redirect:/";

        // Add variable to the template
        model.addAttribute("username", username);
        // Sends the count value to the view
        model.addAttribute("loginCount", count);

        // If the user is logged in, we return the view for the main page.
        return "main.html";
    }
}
