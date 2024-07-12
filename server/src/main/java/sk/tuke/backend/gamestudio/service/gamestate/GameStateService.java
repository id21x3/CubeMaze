package sk.tuke.backend.gamestudio.service.gamestate;

import sk.tuke.backend.gamestudio.entity.GameState;

public interface GameStateService {
    void saveGameState(GameState gameState) throws GameStateException;
    GameState getGameStates(String playerName, String gameName) throws GameStateException;
    void deleteGameStates(String playerName, String gameName) throws GameStateException;
}
