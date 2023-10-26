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

        //  Compares the references for the repository dependency injected by Spring
        boolean b1 = s1.getCommentRepository() == s2.getCommentRepository();

        //  Because the dependency (CommentRepository)
        //  is singleton, both services contain the same
        //  reference, so this line always prints “true.”
        System.out.println(b1);
    }
}