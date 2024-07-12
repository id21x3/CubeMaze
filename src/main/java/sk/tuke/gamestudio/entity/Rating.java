package sk.tuke.gamestudio.entity;

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
    @NamedQuery(name = "Rating.getAverageRating",
            query = "SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game"),
    @NamedQuery(name = "Rating.getRating",
            query = "SELECT r.rating FROM Rating r WHERE r.player = :player AND r.game = :game"),
    @NamedQuery(name = "Rating.getRatingByPlayerAndGame",
                query = "SELECT r FROM Rating r WHERE r.player = :player AND r.game = :game"),
    @NamedQuery(name = "Rating.reset",
            query = "DELETE FROM Rating")

})
public class Rating implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;

    private String player;
    private String game;
    private int rating;
    private Date ratedOn;

    public Rating(String player, String game, int rating, Date ratedOn) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }
}
