package sk.tuke.gamestudio.game.cubemaze.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public void resetMovesCount(){
        this.movesCount += 100;
    }
}

