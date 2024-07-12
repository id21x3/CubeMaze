package sk.tuke.backend.gamestudio.server.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.backend.gamestudio.entity.GameState;
import sk.tuke.backend.gamestudio.entity.Score;
import sk.tuke.backend.gamestudio.game.cubemaze.core.Player;
import sk.tuke.backend.gamestudio.game.cubemaze.core.game.GameStateInfo;
import sk.tuke.backend.gamestudio.game.cubemaze.core.game.LevelManager;
import sk.tuke.backend.gamestudio.service.gamestate.GameStateService;
import sk.tuke.backend.gamestudio.service.score.ScoreService;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Objects;

@RestController
@RequestMapping("/secured")
public class MainController {

    @Autowired
    private GameStateService gameStateService;
    @Autowired
    private ScoreService scoreService;
    private Player currentPlayer;

    private final LevelManager levelManager = new LevelManager("maps/levels.json");

    @GetMapping("/user")
    public String userAccess(Principal principal) {
        if (principal == null)
            return null;
        return principal.getName();
    }

    @GetMapping("/cubemaze")
    public ResponseEntity<?> startGame(Principal principal) {
        String playerName = principal.getName();
        currentPlayer = new Player(playerName);
        GameState gameState = gameStateService.getGameStates(playerName, "CubeMaze");

        levelManager.loadLevels();
        int totalLevels = levelManager.getLevels().size();
        int lastLevel = 0;

        if (gameState != null) {
            GameStateInfo gameStateInfo = GameStateInfo.fromJsonString(gameState.getJsonState());
            lastLevel = gameStateInfo.getLevel();
            currentPlayer.setMovesCount(gameStateInfo.getPlayerScore() - 100);
            levelManager.prepareLevel(lastLevel, gameStateInfo.getPlayerCoordinate(), gameStateInfo.getCubeSides());
        }

        return ResponseEntity.ok(new GameInit(totalLevels, lastLevel));
    }

    @PostMapping("/cubemaze/move")
    public ResponseEntity<?> movePlayer(@RequestBody MoveRequest moveRequest, Principal principal) {
        String playerName = principal.getName();
        if (!Objects.equals(playerName, currentPlayer.getName())) {
            savePlayerData(currentPlayer);
            currentPlayer = loadPlayerData(playerName);
        }
        String direction = moveRequest.direction();
        char dir = direction.toLowerCase().charAt(0);
        int moveResult = levelManager.getCurrentLevel().moveCube(dir);
        currentPlayer.decrementMovesCount();

        if (moveResult == 1) {
            if (levelManager.hasNextLevel()) {
                levelManager.goToNextLevel();
                currentPlayer.updateMovesCount();
                JsonObject fieldDataJson = levelManager.getCurrentLevel().getFieldDataAsJson();
                fieldDataJson.addProperty("nextLevel", true);
                return ResponseEntity.ok().body(fieldDataJson.toString());
            } else {
                JsonObject finishGameDataJson = new JsonObject();
                finishGameDataJson.addProperty("message", "Game completed");
                finishGameDataJson.addProperty("gameCompleted", true);
                return ResponseEntity.ok().body(finishGameDataJson.toString());
            }
        }

        JsonObject cubeDataJson = levelManager.getCurrentLevel().getCubeDataAsJson();
        return ResponseEntity.ok(cubeDataJson.toString());
    }

    @PostMapping("/cubemaze/selectMap")
    public ResponseEntity<?> selectMap(@RequestBody LevelSelection selection) {
        int levelIndex = selection.levelIndex();
        if (levelIndex >= 0 && levelIndex < levelManager.getLevels().size()) {
            levelManager.setCurrentLevelIndex(levelIndex);
            JsonObject fieldDataJson = levelManager.getCurrentLevel().getFieldDataAsJson();
            return ResponseEntity.ok(fieldDataJson.toString());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid level index");
        }
    }

    @PostMapping("/cubemaze/save")
    public ResponseEntity<?> saveGameState(Principal principal) {
        String playerName = principal.getName();
        if (!Objects.equals(playerName, currentPlayer.getName()))
            currentPlayer = loadPlayerData(playerName);

        savePlayerData(currentPlayer);
        saveGameState(playerName);
        return ResponseEntity.ok().build();
    }


    private void savePlayerData(Player player) {
        scoreService.addScore(
                new Score(player.getName(),
                "CubeMaze",
                player.getMovesCount(),
                new Timestamp(System.currentTimeMillis()))
        );
    }


    private Player loadPlayerData(String playerName) {
        GameState gameState = gameStateService.getGameStates(playerName, "CubeMaze");
        if (gameState != null) {
            GameStateInfo gameStateInfo = GameStateInfo.fromJsonString(gameState.getJsonState());
            return new Player(playerName, gameStateInfo.getPlayerScore() - 100); // Assuming Player constructor accepts name and movesCount
        } else {
            return new Player(playerName, 100); // Default moves count is 100
        }
    }

    private void saveGameState(String playerName) {
        GameStateInfo gameStateInfo = new GameStateInfo(
                levelManager.getCurrentLevelIndex(),
                currentPlayer.getMovesCount(),
                levelManager.getCurrentLevel().getCube().getCoordinate(),
                levelManager.getCurrentLevel().getCube().getSides()
        );
        gameStateService.saveGameState(
                new GameState(
                        currentPlayer.getName(),
                        "CubeMaze",
                        gameStateInfo.toJsonString(),
                        new Timestamp(System.currentTimeMillis())
                )
        );
    }

    private GameStateInfo loadGameState(String playerName) {
        GameState gameState = gameStateService.getGameStates(playerName, "CubeMaze");
        if (gameState != null) {
            return GameStateInfo.fromJsonString(gameState.getJsonState());
        }
        return null;
    }

    public record GameInit(int totalLevels, int lastLevel) {}

    public record MoveRequest(String direction) {}

    public record LevelSelection(int levelIndex) {}
}
