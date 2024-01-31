package com.davidcv.springboot.firstwebapp.login;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    public boolean authenticate(String name, String password) {
        return name.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin");
    }
}
