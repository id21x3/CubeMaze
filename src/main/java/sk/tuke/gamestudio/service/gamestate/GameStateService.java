package sk.tuke.gamestudio.service.gamestate;

import sk.tuke.gamestudio.entity.GameState;

import java.util.List;

public interface GameStateService {
    void saveGameState(GameState gameState) throws GameStateException;
    GameState getGameStates(String playerName, String gameName) throws GameStateException;
    void deleteGameStates(String playerName, String gameName) throws GameStateException;
}
