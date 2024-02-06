package com.davidcv.rest.webservices.restfulwebservices.jpa;

import com.davidcv.rest.webservices.restfulwebservices.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
