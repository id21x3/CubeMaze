package sk.tuke.backend.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sk.tuke.backend.gamestudio.entity.Comment;
import sk.tuke.backend.gamestudio.entity.Rating;
import sk.tuke.backend.gamestudio.entity.Score;
import sk.tuke.backend.gamestudio.service.comment.CommentService;
import sk.tuke.backend.gamestudio.service.rating.RatingService;
import sk.tuke.backend.gamestudio.service.score.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/secured/api")
public class QueryController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/score/{game}")
    public ResponseEntity<List<Score>> getTopScores(@PathVariable String game, Authentication authentication) {
        String playerName = authentication.getName();
        List<Score> topScores = scoreService.getTopScores(game);
        return ResponseEntity.ok(topScores);
    }

    @GetMapping("/score/maxPoints")
    public ResponseEntity<Score> getMaxPointsForPlayer(Authentication authentication) {
        String playerName = authentication.getName();
        Score score = scoreService.getMaxPointsForPlayer(playerName);
        return ResponseEntity.ok(score);
    }

    @PostMapping("/score")
    public ResponseEntity<Void> addScore(@RequestBody Score score, Authentication authentication) {
        String playerName = authentication.getName();
        score.setPlayer(playerName);
        scoreService.addScore(score);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rating/{game}")
    public ResponseEntity<Integer> getAverageRating(@PathVariable String game) {
        int averageRating = ratingService.getAverageRating(game);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/rating/{game}/{player}")
    public ResponseEntity<Integer> getRating(@PathVariable String game, @PathVariable String player, Authentication authentication) {
        String currentPlayer = authentication.getName();
        if (!currentPlayer.equals(player)) {
            return ResponseEntity.badRequest().build();
        }
        int rating = ratingService.getRating(game, player);
        return ResponseEntity.ok(rating);
    }

    @PostMapping("/rating")
    public ResponseEntity<Void> setRating(@RequestBody Rating rating, Authentication authentication) {
        String playerName = authentication.getName();
        rating.setPlayer(playerName);
        ratingService.setRating(rating);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comment/{game}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String game) {
        List<Comment> comments = commentService.getComments(game);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/comment/lastComment")
    public ResponseEntity<Comment> getLastCommentFromPlayer(Authentication authentication) {
        String playerName = authentication.getName();
        Comment lastComment = commentService.getLastCommentFromPlayer(playerName);
        return ResponseEntity.ok(lastComment);
    }

    @PostMapping("/comment")
    public ResponseEntity<Void> addComment(@RequestBody Comment comment, Authentication authentication) {
        String playerName = authentication.getName();
        comment.setPlayer(playerName);
        commentService.addComment(comment);
        return ResponseEntity.ok().build();
    }
}