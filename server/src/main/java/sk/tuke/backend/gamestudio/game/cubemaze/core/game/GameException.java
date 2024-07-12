package sk.tuke.backend.gamestudio.game.cubemaze.core.game;

public class GameException extends RuntimeException{
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
