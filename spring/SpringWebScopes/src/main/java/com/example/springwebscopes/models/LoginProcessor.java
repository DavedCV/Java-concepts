package com.example.springwebscopes.models;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
// We use the @RequestScope annotation to change the
// bean’s scope to request scope. This way, Spring creates
// a new instance of the class for every HTTP request.
@RequestScope
public class LoginProcessor {
    private String username;
    private String password;

    // The bean defines a method for
    // implementing the login logic.
    public boolean login() {
        return "natalie".equals(username) && "password".equals(password);
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
