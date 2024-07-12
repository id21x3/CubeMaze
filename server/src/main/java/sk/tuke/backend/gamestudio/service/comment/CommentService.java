package sk.tuke.backend.gamestudio.service.comment;


import sk.tuke.backend.gamestudio.entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment) throws CommentException;
    List<Comment> getComments(String game) throws CommentException;
    Comment getLastCommentFromPlayer(String player) throws CommentException;
    void reset() throws CommentException;
}
