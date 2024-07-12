package sk.tuke.backend.gamestudio.service.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sk.tuke.backend.gamestudio.entity.Score;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
public class ScoreServiceRestClient implements ScoreService {
    @Value("${remote.server.api}/score")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addScore(Score score) {
        restTemplate.postForEntity(url, score, Score.class);
    }

    @Override
    public List<Score> getTopScores(String game) {
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForEntity(url + "/" + game, Score[].class).getBody()));
    }

    @Override
    public Score getMaxPointsForPlayer(String player) {
        return Objects.requireNonNull(restTemplate.getForEntity(url + "/maxPoints/" + player, Score.class).getBody());
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}