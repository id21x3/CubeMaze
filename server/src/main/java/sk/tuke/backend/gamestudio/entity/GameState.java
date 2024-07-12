package sk.tuke.backend.gamestudio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "GameState.getGameStatesByPlayerAndGame",
                query = "SELECT gs FROM GameState gs WHERE gs.player = :player AND gs.game = :game"),
        @NamedQuery(name = "GameState.deleteGameStatesByPlayerAndGame",
                query = "DELETE FROM GameState gs WHERE gs.player = :player AND gs.game = :game")
})
public class GameState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;

    private String player;
    private String game;
    @Column(length = 1000)
    private String jsonState;
    private Date exitOn;

    public GameState(String player, String game, String jsonState, Date exitOn) {
        this.player = player;
        this.game = game;
        this.jsonState = jsonState;
        this.exitOn = exitOn;
    }
}

