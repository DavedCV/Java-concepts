package org.example;

import org.example.configuration.ProjectConfiguration;
import org.example.models.Comment;
import org.example.services.CommentService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.logging.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        var c = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        var service = c.getBean(CommentService.class);

        //  Creates a Comment instance
        //  to give as a parameter to the
        //  publishComment() method
        Comment comment = new Comment();
        comment.setText("Demo comment");
        comment.setAuthor("Natasha");

        // Calls the publishComment() method
        String value = service.publishComment(comment);

        // Prints the value returned by the publishComment() method
        logger.info(value);
    }
}