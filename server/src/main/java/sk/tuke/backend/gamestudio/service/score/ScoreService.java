package sk.tuke.backend.gamestudio.service.score;

import sk.tuke.backend.gamestudio.entity.Score;

import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;
    List<Score> getTopScores(String game) throws ScoreException;
    Score getMaxPointsForPlayer(String player) throws ScoreException;
    void reset() throws ScoreException;
}
