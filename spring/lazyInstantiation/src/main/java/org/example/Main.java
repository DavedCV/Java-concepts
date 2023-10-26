package org.example;

import org.example.configuration.ProjectConfiguration;
import org.example.services.CommentService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        System.out.println("Before retrieving the CommentService");
        //  At this line, where Spring needs to provide a
        //  reference to the CommentService bean,
        //  Spring also creates the instance
        var service = context.getBean(CommentService.class);
        System.out.println("After retrieving the CommentService");
    }
}