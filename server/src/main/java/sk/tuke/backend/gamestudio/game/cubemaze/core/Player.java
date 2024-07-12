package sk.tuke.backend.gamestudio.game.cubemaze.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Player {
    private String name;
    private int movesCount;

    public Player(String name) {
        this.name = name;
        this.movesCount = 100;
    }

    public void decrementMovesCount(){
        this.movesCount--;
    }

    public void updateMovesCount(){
        this.movesCount += 100;
    }
}

