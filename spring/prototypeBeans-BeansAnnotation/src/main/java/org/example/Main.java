package org.example;

import org.example.configuration.ProjectConfiguration;
import org.example.services.CommentService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var c = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        // The two variables cs1 and cs2 contain
        //  references to different instances.
        var cs1 = c.getBean("commentService", CommentService.class);
        var cs2 = c.getBean("commentService", CommentService.class);

        boolean b = cs1 == cs2;

        // This line always prints
        //“false” in the console.
        System.out.println(b);
    }
}