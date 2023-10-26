package org.example.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
//  The @Lazy annotation tells Spring that it
//  needs to create the bean only when someone
//  refers to the bean for the first time.
@Lazy
public class CommentService {
    public CommentService() {
        System.out.println("CommentService instance created!");
    }
}
