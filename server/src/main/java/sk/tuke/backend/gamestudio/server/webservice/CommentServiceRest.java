package sk.tuke.backend.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.backend.gamestudio.entity.Comment;
import sk.tuke.backend.gamestudio.service.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{game}")
    public List<Comment> getComments(@PathVariable String game) {
        return commentService.getComments(game);
    }

    @GetMapping("/lastComment/{player}")
    public Comment getLastCommentFromPlayer(@PathVariable String player) {
        return commentService.getLastCommentFromPlayer(player);
    }

    @PostMapping
    public void addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
    }
}