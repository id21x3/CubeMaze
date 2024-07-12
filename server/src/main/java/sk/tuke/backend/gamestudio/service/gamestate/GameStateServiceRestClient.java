package sk.tuke.backend.gamestudio.service.gamestate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sk.tuke.backend.gamestudio.entity.GameState;

@RestController
public class GameStateServiceRestClient implements GameStateService {
    @Value("${remote.server.api}/gamestate")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void saveGameState(GameState gameState) throws GameStateException {
        try {
            restTemplate.postForEntity(url, gameState, GameState.class);
        } catch (Exception e) {
            throw new GameStateException("Problem saving game state via web service", e);
        }
    }

    @Override
    public GameState getGameStates(String player, String game) throws GameStateException {
        try {
            return restTemplate.getForEntity(url + "/" + player + "/" + game, GameState.class).getBody();
        } catch (Exception e) {
            throw new GameStateException("Problem retrieving game states via web service", e);
        }
    }

    @Override
    public void deleteGameStates(String player, String game) throws GameStateException {
        try {
            restTemplate.delete(url + "/" + player + "/" + game);
        } catch (Exception e) {
            throw new GameStateException("Problem deleting game states via web service", e);
        }
    }
}
