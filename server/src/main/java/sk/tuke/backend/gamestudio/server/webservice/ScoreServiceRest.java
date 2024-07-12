package sk.tuke.backend.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.backend.gamestudio.entity.Score;
import sk.tuke.backend.gamestudio.service.score.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) {
        return scoreService.getTopScores(game);
    }

    @GetMapping("/maxPoints/{player}")
    public Score getMaxPointsForPlayer(@PathVariable String player) {
        return scoreService.getMaxPointsForPlayer(player);
    }

    @PostMapping
    public void addScore(@RequestBody Score score) {
        scoreService.addScore(score);
    }
}