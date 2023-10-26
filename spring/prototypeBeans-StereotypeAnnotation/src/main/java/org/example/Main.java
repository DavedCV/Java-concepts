package org.example;

import org.example.configuration.ProjectConfiguration;
import org.example.services.CommentService;
import org.example.services.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        var s1 = context.getBean(CommentService.class);
        var s2 = context.getBean(UserService.class);

        //  Compares the references for the injected
        //  CommentRepository instances. Because
        //  CommentRepository is a prototype bean,
        //  the result of the comparison is always false.
        boolean b1 = s1.getCommentRepository() == s2.getCommentRepository();

        System.out.println(b1);
    }
}