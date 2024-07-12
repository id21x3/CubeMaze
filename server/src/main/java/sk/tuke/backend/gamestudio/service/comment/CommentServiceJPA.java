package sk.tuke.backend.gamestudio.service.comment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.backend.gamestudio.entity.Comment;

import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) throws CommentException {
        entityManager.persist(comment);
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        return entityManager.createNamedQuery("Comment.getCommentsForGame", Comment.class)
                .setParameter("game", game)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public Comment getLastCommentFromPlayer(String player) throws CommentException {
        List<Comment> comments = entityManager.createNamedQuery("Comment.getLastCommentFromPlayer", Comment.class)
                .setParameter("player", player)
                .setMaxResults(1)
                .getResultList();
        return comments.isEmpty() ? null : comments.get(0);
    }


    @Override
    public void reset() throws CommentException {
        entityManager.createNamedQuery("Comment.reset").executeUpdate();
    }
}
