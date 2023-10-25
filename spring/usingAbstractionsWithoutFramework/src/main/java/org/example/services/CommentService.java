package org.example.services;

import org.example.models.Comment;
import org.example.proxies.CommentNotificationProxy;
import org.example.repositories.CommentRepository;

public class CommentService {

    // We define the two dependencies as attributes of the class.
    private final CommentRepository commentRepository;
    private final CommentNotificationProxy commentNotificationProxy;


    // We provide the dependencies when the object is built through the parameters of the constructor.
    public CommentService(CommentRepository commentRepository, CommentNotificationProxy commentNotificationProxy) {
        this.commentRepository = commentRepository;
        this.commentNotificationProxy = commentNotificationProxy;
    }

    // We implement the use case that delegates the “store comment” and “send notification” responsibilities to the
    // dependencies.
    public void publishComment(Comment comment) {
        commentRepository.storeComment(comment);
        commentNotificationProxy.sendComment(comment);
    }
}
