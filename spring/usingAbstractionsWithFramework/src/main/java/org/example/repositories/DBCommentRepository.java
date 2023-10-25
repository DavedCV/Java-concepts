package org.example.repositories;

import org.example.models.Comment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

// We use @Repository to define this object as a
//component with the responsibility of the repository.
@Repository
public class DBCommentRepository implements CommentRepository {
    @Override
    public void storeComment(Comment comment) {
        System.out.println("Storing comment: " + comment.getText());
    }
}
