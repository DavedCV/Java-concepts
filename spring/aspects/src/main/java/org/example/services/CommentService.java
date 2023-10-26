package org.example.services;

import org.example.models.Comment;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class CommentService {

    // To log a message in the app’s console every time
    // someone calls the use case, we use a logger object.
    private Logger logger = Logger.getLogger(CommentService.class.getName());

    //  This method defines
    //  the use case for our
    //  demonstration.
    public void publishComment(Comment comment) {
        logger.info("Publishing comment: " + comment.getText());
    }
}
