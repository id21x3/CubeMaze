package sk.tuke.backend.gamestudio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@NamedQueries({
    @NamedQuery(
            name = "Score.getTopScores",
            query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.points DESC"
    ),
    @NamedQuery(
            name = "Score.resetScores",
            query = "DELETE FROM Score"),
    @NamedQuery(
            name = "Score.getMaxPointsForPlayer",
            query = "SELECT s FROM Score s WHERE s.player = :player ORDER BY s.points DESC"
    )
})
public class Score implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;

    private String player;
    private String game;
    private int points;
    private Date playedOn;

    public Score(String player, String game, int points, Date playedOn) {
        this.player = player;
        this.game = game;
        this.points = points;
        this.playedOn = playedOn;
    }
}
