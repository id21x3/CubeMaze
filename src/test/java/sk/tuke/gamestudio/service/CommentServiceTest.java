package sk.tuke.gamestudio.service;

import org.junit.Test;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.comment.CommentServiceJDBC;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommentServiceTest {

    private CommentService createService() {
        return new CommentServiceJDBC();
    }

    @Test
    public void testReset() {
        CommentService service = createService();
        service.reset();
        assertEquals(0, service.getComments("mines").size());
    }

    @Test
    public void testAddComment() {
        CommentService service = createService();
        service.reset();
        Date date = new Date();
        service.addComment(new Comment("Jaro", "mines", "This game is awesome!", date));

        List<Comment> comments = service.getComments("mines");

        assertEquals(1, comments.size());

        assertEquals("Jaro", comments.get(0).getPlayer());
        assertEquals("mines", comments.get(0).getGame());
        assertEquals("This game is awesome!", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());
    }

}
