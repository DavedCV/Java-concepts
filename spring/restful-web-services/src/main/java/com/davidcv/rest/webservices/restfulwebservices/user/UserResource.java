package com.davidcv.rest.webservices.restfulwebservices.user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserResource {

    private UserDaoService userDaoService;

    public UserResource(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    // GET /users
    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userDaoService.findAll();
    }

    // GET /users/{id}
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) {
        return userDaoService.findUserById(id);
    }

    // POST /users
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userDaoService.saveUser(user);
    }
}
