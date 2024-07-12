package sk.tuke.gamestudio.game.cubemaze.core;

public class GameException extends RuntimeException{
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
