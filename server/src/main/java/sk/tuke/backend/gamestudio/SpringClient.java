package sk.tuke.backend.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.backend.gamestudio.game.cubemaze.consoleui.ConsoleUiManager;
import sk.tuke.backend.gamestudio.game.cubemaze.core.cube.Cube;
import sk.tuke.backend.gamestudio.game.cubemaze.core.field.Field;
import sk.tuke.backend.gamestudio.game.cubemaze.core.game.LevelManager;
import sk.tuke.backend.gamestudio.service.comment.CommentService;
import sk.tuke.backend.gamestudio.service.comment.CommentServiceRestClient;
import sk.tuke.backend.gamestudio.service.gamestate.GameStateService;
import sk.tuke.backend.gamestudio.service.gamestate.GameStateServiceRestClient;
import sk.tuke.backend.gamestudio.service.rating.RatingService;
import sk.tuke.backend.gamestudio.service.rating.RatingServiceRestClient;
import sk.tuke.backend.gamestudio.service.score.ScoreService;
import sk.tuke.backend.gamestudio.service.score.ScoreServiceRestClient;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.backend.gamestudio.server.*"))
public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUiManager uiManager) {
        return args -> uiManager.run();
    }

    @Bean
    public ConsoleUiManager uiManager(LevelManager levelManager) {
        return new ConsoleUiManager(levelManager);
    }

    @Bean
    public LevelManager levelManager(){
        return new LevelManager("maps/levels.json");
    }

    @Bean
    public Field field(Cube cube) {
        return new Field(cube);
    }

    @Bean
    public Cube cube() {
        return new Cube();
    }

    @Bean
    public ScoreService scoreService() {
        //return new ScoreServiceJPA();
        return new ScoreServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        //return new RatingServiceJPA();
        return new RatingServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        //return new CommentServiceJPA();
        return new CommentServiceRestClient();
    }

    @Bean
    public GameStateService gameStateService() {
        //return new GameStateServiceJPA();
        return new GameStateServiceRestClient();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


