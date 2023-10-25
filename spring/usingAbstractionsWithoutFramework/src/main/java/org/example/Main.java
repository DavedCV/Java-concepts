package org.example;

import org.example.models.Comment;
import org.example.proxies.CommentNotificationProxy;
import org.example.proxies.EmailCommentNotificationProxy;
import org.example.repositories.CommentRepository;
import org.example.repositories.DBCommentRepository;
import org.example.services.CommentService;

public class Main {
    public static void main(String[] args) {

        // Creates the instance for the dependencies
        CommentRepository commentRepository = new DBCommentRepository();
        CommentNotificationProxy commentNotificationProxy = new EmailCommentNotificationProxy();

        // Creates the instance of the service and providing the dependencies
        CommentService commentService = new CommentService(commentRepository, commentNotificationProxy);

        // Creates an instance of comment to send as a
        //parameter to the publish comment use case
        Comment comment = new Comment();
        comment.setAuthor("David");
        comment.setText("Demo comment");

        // Calls the use case
        commentService.publishComment(comment);
    }
}