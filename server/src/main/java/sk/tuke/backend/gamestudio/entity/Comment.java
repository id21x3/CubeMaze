package sk.tuke.backend.gamestudio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(
                name = "Comment.getCommentsForGame",
                query = "SELECT c FROM Comment c WHERE c.game = :game"),
        @NamedQuery(
                name = "Comment.getLastCommentFromPlayer",
                query = "SELECT c FROM Comment c WHERE c.player = :player ORDER BY c.commentedOn DESC"
        ),
        @NamedQuery(
                name = "Comment.reset",
                query = "DELETE FROM Comment")
})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;

    private String player;
    private String game;
    private String comment;
    private Date commentedOn;

    public Comment(String player, String game, String comment, Date commentedOn) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }
}
