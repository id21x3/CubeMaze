package sk.tuke.backend.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.backend.gamestudio.service.comment.CommentService;
import sk.tuke.backend.gamestudio.service.comment.CommentServiceJPA;
import sk.tuke.backend.gamestudio.service.gamestate.GameStateService;
import sk.tuke.backend.gamestudio.service.gamestate.GameStateServiceJPA;
import sk.tuke.backend.gamestudio.service.rating.RatingService;
import sk.tuke.backend.gamestudio.service.rating.RatingServiceJPA;
import sk.tuke.backend.gamestudio.service.score.ScoreService;
import sk.tuke.backend.gamestudio.service.score.ScoreServiceJPA;

@SpringBootApplication
@Configuration
@EntityScan("sk.tuke.backend.gamestudio")
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }
    @Bean
    public GameStateService gameStateService() {
        return new GameStateServiceJPA();
    }



}