package sk.tuke.backend.gamestudio.service.score;

public class ScoreException extends RuntimeException {
    public ScoreException(String problemInsertingScore, Throwable cause) {
        super(cause);
    }
}
