package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.GameState;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.game.cubemaze.core.*;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.gamestate.GameStateService;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.score.ScoreService;

import java.util.List;

@Controller
@RequestMapping("/cubemaze")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class CubeMazeController {
    @Autowired
    private UserController userController;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private GameStateService gameStateService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    private final LevelManager levelManager = new LevelManager("maps/levels.json");
    private Player player;
    private GameState gameState;

    @GetMapping
    public String startGame(Model model) {
        User loggedUser = userController.getLoggedUser();
        if (loggedUser == null) {
            return "redirect:/login";
        }

        player = new Player(userController.getLoggedUser().getLogin());
        gameState = gameStateService.getGameStates(player.getName(), "CubeMaze");

        // Загрузка уровней в LevelManager
        levelManager.loadLevels();

        // Если есть сохранённое состояние, используем его для определения доступных уровней
        int lastLevel = 0; // Начальный уровень по умолчанию
        if (gameState != null) {
            GameStateInfo gameStateInfo = GameStateInfo.fromJsonString(gameState.getJsonState());
            lastLevel = gameStateInfo.getLevel();
            player.setMovesCount(gameStateInfo.getPlayerScore() - 100);
            levelManager.prepareLevel(lastLevel, gameStateInfo.getPlayerCoordinate(), gameStateInfo.getCubeSides());
        }

        model.addAttribute("player", player);
        model.addAttribute("levels", levelManager.getLevels());
        model.addAttribute("lastLevel", lastLevel);

        return "chooseMap"; // Перенаправляем на страницу выбора уровня
    }

    @PostMapping("/move")
    public String movePlayer(@RequestParam String direction, Model model) {
        char dir = direction.toLowerCase().charAt(0);
        int moveResult = levelManager.getCurrentLevel().moveCube(dir);

        model.addAttribute("player", player);
        model.addAttribute("levelManager", levelManager);
        model.addAttribute("htmlField", getHtmlField());

        return "game";
    }


    @PostMapping("/selectMap")
    public String selectMap(@RequestParam("levelIndex") int levelIndex, Model model) {
        if (levelIndex >= 0 && levelIndex < levelManager.getLevels().size()) {
            levelManager.setCurrentLevelIndex(levelIndex);
            model.addAttribute("player", player);
            model.addAttribute("levelManager", levelManager);
            model.addAttribute("htmlField", getHtmlField());  // Добавление HTML-представления поля
            return "game"; // Переход к игровому процессу
        } else {
            return "chooseMap"; // Вернуться к выбору карты, если выбор неверен
        }
    }

    @GetMapping("/chooseMap")
    public String chooseMap(Model model) {
        List<Field> levels = levelManager.getLevels();
        model.addAttribute("levels", levels);

        return "chooseMap";
    }

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        Tile[][] field = levelManager.getCurrentLevel().getField();

        sb.append("<div class='field'>");

        for (Tile[] row : field) {
            sb.append("<div class='row'>");
            for (Tile tile : row) {
                if (tile instanceof ColoredTile coloredTile) {
                    sb.append(getTileClass(coloredTile)).append("</div>");
                } else {
                    sb.append("<div class='tile-clear'></div>");
                }
            }
            sb.append("</div>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    private String getTileClass(ColoredTile tile) {
        return switch (tile.getState()) {
            case TILE -> "<div class='tile-normal'>";
            case CUBE_ON -> "<div class='tile-normal'><div class='cube'>" +
                    "<div class='front'></div>" +
                    "<div class='back'></div>" +
                    "<div class='top'></div>" +
                    "<div class='bottom'></div>" +
                    "<div class='left'></div>" +
                    "<div class='right'></div>" +
                    "</div></div>";
            case FINISH -> "<div class='tile-finish'>";
        };
    }
}
