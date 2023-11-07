package com.example.springwebscopes.models;

import com.example.springwebscopes.services.LoggedUserManagementService;
import com.example.springwebscopes.services.LoginCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
// We use the @RequestScope annotation to change the
// bean’s scope to request scope. This way, Spring creates
// a new instance of the class for every HTTP request.
@RequestScope
public class LoginProcessor {

    private final LoggedUserManagementService loggedUserManagementService;
    private final LoginCountService loginCountService;
    private String username;
    private String password;

    // We auto-wire the LoggedUserManagementService bean
    @Autowired
    public LoginProcessor(LoggedUserManagementService loggedUserManagementService, LoginCountService loginCountService) {
        this.loggedUserManagementService = loggedUserManagementService;
        this.loginCountService = loginCountService;
    }

    // The bean defines a method for
    // implementing the login logic.
    public boolean login() {
        loginCountService.increment();

        boolean loginResult = false;

        if ("natalie".equals(username) && "password".equals(password)) {
            loginResult = true;

            // We store the username on the LoggedUserManagementService bean.
            loggedUserManagementService.setUsername(username);
        }

        return loginResult;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
