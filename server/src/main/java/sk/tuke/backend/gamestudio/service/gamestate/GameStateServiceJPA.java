package sk.tuke.backend.gamestudio.service.gamestate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.backend.gamestudio.entity.GameState;

@Transactional
public class GameStateServiceJPA implements GameStateService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveGameState(GameState gameState) throws GameStateException {
        try {
            GameState existingGameState = entityManager.createNamedQuery("GameState.getGameStatesByPlayerAndGame", GameState.class)
                    .setParameter("player", gameState.getPlayer())
                    .setParameter("game", gameState.getGame())
                    .getSingleResult();

            existingGameState.setJsonState(gameState.getJsonState());
            existingGameState.setExitOn(gameState.getExitOn());
            entityManager.merge(existingGameState);
        } catch (NoResultException e) {
            entityManager.persist(gameState);
        } catch (Exception e) {
            throw new GameStateException("Problem saving game state.", e);
        }
    }

    @Override
    public GameState getGameStates(String playerName, String gameName) throws GameStateException {
        try {
            return entityManager.createNamedQuery("GameState.getGameStatesByPlayerAndGame", GameState.class)
                    .setParameter("player", playerName)
                    .setParameter("game", gameName)
                    .getSingleResult();
        }catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new GameStateException("Problem retrieving game states.", e);
        }
    }

    @Override
    public void deleteGameStates(String playerName, String gameName) throws GameStateException {
        try {
            entityManager.createNamedQuery("GameState.deleteGameStatesByPlayerAndGame")
                    .setParameter("player", playerName)
                    .setParameter("game", gameName)
                    .executeUpdate();
        } catch (Exception e) {
            throw new GameStateException("Problem deleting game states.", e);
        }
    }
}
