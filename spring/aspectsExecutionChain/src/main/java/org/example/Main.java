package org.example;

import org.example.configuration.ProjectConfiguration;
import org.example.models.Comment;
import org.example.services.CommentService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfiguration.class);

        Comment comment = new Comment();
        comment.setText("Demo comment");
        comment.setAuthor("David");

        CommentService commentService = context.getBean(CommentService.class);
        commentService.publishComment(comment);
    }
}