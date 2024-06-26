package com.davidcv.rest.webservices.restfulwebservices.user;

import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    // - add link to: http://localhost:8080/users
    // - EntityModel: wraps the domain object and add link to it
    // - WebMvcLinkBuilder: builder to ease build of Link instances pointing to Spring MVC controllers
    @GetMapping("/users/{id}")
    public EntityModel<User> getUser(@PathVariable long id) {
        User userById = userDaoService.findUserById(id);

        if (userById == null) throw new UserNotFoundException("id: " + id);

        EntityModel<User> entityModel = EntityModel.of(userById);
        WebMvcLinkBuilder linkBuilder =
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linkBuilder.withRel("allUsers"));

        return entityModel;
    }

    // POST /users
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userDaoService.saveUser(user);

        /*
         * This line constructs the URI for the newly created user resource. It uses ServletUriComponentsBuilder to
         * build the URI based on the current request URI (fromCurrentRequest()), appends the path "/{id}" to
         * it (where {id} will be replaced with the actual ID of the saved user), and finally converts it to a URI
         * object.
         * */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{id}")
                                                  .buildAndExpand(savedUser.getId())
                                                  .toUri();
        /*
         * - This line returns a ResponseEntity with status code 201 (CREATED) along with the location header
         * pointing to the URI of the newly created user resource.
         *
         * - ResponseEntity.created(location) is a builder method that sets the location header to the specified URI,
         * and
         * build() builds the ResponseEntity object.
         * */
        return ResponseEntity.created(location).build();
    }

    // DELETE /users/{id}
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable long id) {
        userDaoService.deleteUserById(id);
    }

}
