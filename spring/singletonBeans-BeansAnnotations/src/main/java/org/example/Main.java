package org.example;

import org.example.configuration.ProjectConfiguration;
import org.example.services.CommentService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        var cs1 = context.getBean("commentService", CommentService.class);
        var cs2 = context.getBean("commentService", CommentService.class);

        // The singleton bean scope always return the same instance
        boolean b1 = cs1 == cs2;

        System.out.println(b1);
    }
}