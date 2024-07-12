package sk.tuke.backend.gamestudio.service.score;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.backend.gamestudio.entity.Score;

import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) throws ScoreException {
        entityManager.persist(score);
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        return entityManager.createNamedQuery("Score.getTopScores", Score.class)
                .setParameter("game", game)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public Score getMaxPointsForPlayer(String player) throws ScoreException {
        try {
            return (Score) entityManager.createNamedQuery("Score.getMaxPointsForPlayer")
                    .setParameter("player", player)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new ScoreException("No scores found for player: " + player, e);
        }
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Score.resetScores").executeUpdate();
    }
}
