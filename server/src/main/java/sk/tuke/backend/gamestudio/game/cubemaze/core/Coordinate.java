package sk.tuke.backend.gamestudio.game.cubemaze.core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Coordinate {
    private int X;
    private int Y;

    public Coordinate(){
        this(0,0);
    }

    public Coordinate(int X, int Y){
        this.X = X;
        this.Y = Y;
    }

    public void reset() {
        setX(0);
        setY(0);
    }

    public void setCoordinate(int x, int y) {
        setX(x);
        setY(y);
    }

    public  void increaseX(int increase){
        this.X += increase;
    }

    public  void increaseY(int increase){
        this.Y += increase;
    }
}
