package sk.tuke.backend.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.backend.gamestudio.entity.GameState;
import sk.tuke.backend.gamestudio.service.gamestate.GameStateService;

@RestController
@RequestMapping("/api/gamestate")
public class GameStateServiceRest {

    @Autowired
    private GameStateService gameStateService;

    @GetMapping("/{player}/{game}")
    public GameState getGameStates(@PathVariable String player, @PathVariable String game) {
        return gameStateService.getGameStates(player, game);
    }

    @PostMapping
    public void saveGameState(@RequestBody GameState gameState) {
        gameStateService.saveGameState(gameState);
    }

    @DeleteMapping("/{player}/{game}")
    public void deleteGameStates(@PathVariable String player, @PathVariable String game) {
        gameStateService.deleteGameStates(player, game);
    }
}