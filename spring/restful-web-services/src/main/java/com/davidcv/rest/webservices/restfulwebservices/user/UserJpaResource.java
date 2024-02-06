package com.davidcv.rest.webservices.restfulwebservices.user;

import com.davidcv.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.davidcv.rest.webservices.restfulwebservices.jpa.UserRepository;
import com.davidcv.rest.webservices.restfulwebservices.post.Post;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jpa")
public class UserJpaResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    public UserJpaResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // GET /users
    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    // GET /users/{id}
    // - add link to: http://localhost:8080/users
    // - EntityModel: wraps the domain object and add link to it
    // - WebMvcLinkBuilder: builder to ease build of Link instances pointing to Spring MVC controllers
    @GetMapping("/users/{id}")
    public EntityModel<User> getUser(@PathVariable long id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) throw new UserNotFoundException("id: " + id);

        EntityModel<User> entityModel = EntityModel.of(userById.get());
        WebMvcLinkBuilder linkBuilder =
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linkBuilder.withRel("allUsers"));

        return entityModel;
    }

    // POST /users
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

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
        userRepository.deleteById(id);
    }

    // GET /users/{id}/posts
    @GetMapping("/users/{id}/posts")
    public List<Post> getAllPostByUser(@PathVariable long id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) throw new UserNotFoundException("id: " + id);

        return userById.get().getPosts();
    }

    // POST /users/{id}/posts|
    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPostByUserId(@PathVariable long id, @Valid Post post) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) throw new UserNotFoundException("id: " + id);

        post.setUser(userById.get());
        postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{id}")
                                                  .buildAndExpand(post.getId())
                                                  .toUri();

        return ResponseEntity.created(location).build();
    }

}
