package sk.tuke.gamestudio.game.cubemaze.consoleui;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.GameState;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.cubemaze.core.*;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.gamestate.GameStateService;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.score.ScoreService;

import java.sql.Timestamp;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ConsoleUiManager {
    private static final int LENGTH = 100;
    private static final int HEIGHT = 50;

    @Getter
    @Setter
    private UiState currentState;
    private final Scanner scanner;
    private final LevelManager levelManager;
    Player player;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private GameStateService gameStateService;

    public ConsoleUiManager(LevelManager levelManager) {
        this.currentState = UiState.GAMEPLAY;
        this.scanner = new Scanner(System.in);
        this.levelManager = levelManager;
    }

    private char readInput() {
        return scanner.next().charAt(0);
    }

    private void closeScanner() {
        scanner.close();
    }

    public void run() {
        levelManager.loadLevels();
        startGameAnimation();
        player = new Player(promptForPlayerName());
        GameState gameState = gameStateService.getGameStates(player.getName(), "CubeMaze");
        setCurrentState(UiState.MENU);
        if(gameState != null){
            GameStateInfo gameStateInfo = GameStateInfo.fromJsonString(gameState.getJsonState());
            player.setMovesCount(gameStateInfo.getPlayerScore()-100);
            int choice = chooseMapAnimation(gameStateInfo.getLevel()+1);
            if(choice == gameStateInfo.getLevel()){
                levelManager.prepareLevel(gameStateInfo.getLevel(), gameStateInfo.getPlayerCoordinate(), gameStateInfo.getCubeSides());
            }else{
                levelManager.setCurrentLevelIndex(choice);
            }
        } else
            levelManager.setCurrentLevelIndex(chooseMapAnimation(1));

        while (getCurrentState() != UiState.CLOSE) {
            player.resetMovesCount();
            startLevelAnimation();
            setCurrentState(UiState.GAMEPLAY);
            while (getCurrentState() == UiState.GAMEPLAY) {

                printField();
                char input = readInput();

                if (input == 'x') {
                    gameEnd();
                    continue;
                } else if (input == 'c') {
                    nextLevel();
                    continue;
                }

                player.decrementMovesCount();
                if (levelManager.getCurrentLevel().moveCube(input) == 1) nextLevel();

            }
        }
    }

    private void printField() {
        int currentSpaces = levelManager.getCurrentLevel().getField().length + levelManager.getCurrentLevel().getField()[0].length + 25;
        String[] cube = generateCubeString();
        for (Tile[] row : levelManager.getCurrentLevel().getField()) {
            for (int i = 0; i < cube.length; i++) {
                for (int j = 0; j < currentSpaces; j++)
                    System.out.print(" ");
                for (Tile tile : row) {
                    if(tile instanceof ColoredTile coloredTile) {
                        switch (coloredTile.getState()) {
                            case TILE:
                                printTile(coloredTile, i, tile instanceof PaintingTile);
                                break;
                            case CUBE_ON:
                                System.out.print(cube[i]);
                                break;
                            case FINISH:
                                printFinish(coloredTile, i);
                        }
                    }else System.out.print("                ");
                }
                System.out.println();
                currentSpaces--;
            }

            System.out.println();
        }
    }

    private void printTile(ColoredTile coloredTile, int i, boolean canPaint){
        if (i != 0) {
            if (canPaint && i != levelManager.getCurrentLevel().getField()[0].length / 4 + 1)
                System.out.print("░░░░" + coloredTile.getColor().getCode() + "░░░" + ColorPalette.RESET.getCode() + "░░░░     ");
            else
                System.out.print(coloredTile.getColor().getCode() + "░░░░░░░░░░░     " + ColorPalette.RESET.getCode());
        } else
            System.out.print("                ");
    }

    private void printFinish(ColoredTile coloredTile, int i){
        if (i != 0) {
            if (i % 2 == 0)
                System.out.print(ColorPalette.PURPLE.getCode() + "░░░░░░░░░░░     " + ColorPalette.RESET.getCode());
            else
                System.out.print(coloredTile.getColor().getCode() + "░░░░░░░░░░░     " + ColorPalette.RESET.getCode());
        } else
            System.out.print("                ");
    }

    private String[] generateCubeString() {
        String reset = ColorPalette.RESET.getCode();
        EnumMap<CubeSideType, CubeSide> sides = levelManager.getCurrentLevel().getCube().getSides();
        String[] result = new String[6];
        result[0] = "  ═ ═ ═ ═       " + reset;
        result[1] = " / ║   / ║      " + reset;
        result[2] = "═ ═ ═ ═   ║     " + reset;
        result[3] = " ║   ═ ║ ═ ═    " + reset;
        result[4] = "  ║ /   ║ /     " + reset;
        result[5] = "   ═ ═ ═ ═      " + reset;

        for (CubeSideType sideType : CubeSideType.values()) {
            CubeSide side = sides.get(sideType);
            String colorString = side.getColor().getCode();
            boolean isWhite = (Objects.equals(colorString, reset));
            if(isWhite)
                continue;
            String top = sides.get(CubeSideType.TOP).getColor().getCode();
            String bottom = sides.get(CubeSideType.BOTTOM).getColor().getCode();
            String front = sides.get(CubeSideType.FRONT).getColor().getCode();
            String back = sides.get(CubeSideType.BACK).getColor().getCode();
            String left = sides.get(CubeSideType.LEFT).getColor().getCode();
            String right = sides.get(CubeSideType.RIGHT).getColor().getCode();

            switch (sideType) {
                case TOP:
                    result[0] = colorString + "  ═ ═ ═ ═       " + reset;
                    result[1] = colorString + " / " + front + "║   " + colorString + "/ " + front + "║      " + reset;
                    result[2] = colorString + "═ ═ ═ ═   " + front + "║     " + reset;
                    break;
                case FRONT:
                    result[0] = colorString + "  ═ ═ ═ ═       " + reset;
                    result[1] = top + " / " + colorString + "║   " + top + "/ " + colorString + "║      " + reset;
                    result[2] = top + "═ ═ ═ ═   " + colorString + "║     " + reset;
                    result[3] = back + " ║   " + colorString + "═ " + back + "║ " + colorString + "═ ═    " + reset;
                    break;
                case BACK:
                    result[2] = colorString + "═ ═ ═ ═   " + front + "║     " + reset;
                    result[3] = colorString + " ║   " + front + "═ " + colorString + "║ " + front + "═ ═    " + reset;
                    result[4] = colorString + "  ║ " + bottom + "/   " + colorString + "║ " + bottom + "/     " + reset;
                    result[5] = colorString + "   ═ ═ ═ ═      " + reset;
                    break;
                case BOTTOM:
                    result[3] = back + " ║   " + colorString + "═ " + back + "║ " + colorString + "═ ═    " + reset;
                    result[4] = back + "  ║ " + colorString + "/   " + back + "║ " + colorString + "/     " + reset;
                    result[5] = colorString + "   ═ ═ ═ ═      " + reset;
                    break;
                case LEFT:
                    result[0] = colorString + "  ═"+front+" ═ ═ "+right+"═       " + reset;
                    result[1] = colorString + " / ║   " + right + "/ ║      " + reset;
                    result[2] = colorString + "═ "+ back + "═ ═ "+right+"═   ║     " + reset;
                    result[3] = colorString + " ║   ═ " + right + "║ " + front + "═ "+right+"═    " + reset;
                    result[4] = colorString + "  ║ /   " + right + "║ /     " + reset;
                    result[5] = colorString + "   ═ "+back+"═ ═ "+right+"═      " + reset;
                    break;
                case RIGHT:
                    result[0] = left + "  ═"+front+" ═ ═ "+colorString+"═       " + reset;
                    result[1] = left + " / ║   " + colorString + "/ ║      " + reset;
                    result[2] = left + "═ "+ back + "═ ═ "+colorString+"═   ║     " + reset;
                    result[3] = left + " ║   ═ " + colorString + "║ " + front + "═ "+colorString+"═    " + reset;
                    result[4] = left + "  ║ /   " + colorString + "║ /     " + reset;
                    result[5] = left + "   ═ "+back+"═ ═ "+colorString+"═      " + reset;
                    break;
            }
        }
        return result;
    }


    private void startGameAnimation() {
        printEmptyLines(HEIGHT);
        printSpaces(LENGTH);
        printWithDelay("Welcome to the Cube Maze!", 500);

        printEmptyLines(1);
        printSpaces(LENGTH + 6);
        printWithDelay("Starting", 500);
        printWithDelay(".", 500);
        printWithDelay(".", 500);
        printWithDelay(".", 500);
    }
    private void startLevelAnimation() {
        printEmptyLines(HEIGHT);
        printSpaces(LENGTH+5);
        println("Level " + (levelManager.getCurrentLevelIndex()+1));
        printEmptyLines(HEIGHT/2);
        printWithDelay("\n", 1000);
    }

    private int chooseMapAnimation(int level){
        printEmptyLines(HEIGHT);
        printSpaces(LENGTH);
        println("Choose map ^.^\n");
        for (int i = 1; i < level; i++){
            printSpaces(LENGTH);
            println(i + " - Map " + i);
        }
        printSpaces(LENGTH);
        println(level + " - Map " + level + " <- you");
        for (int i = level+1; i <= levelManager.getLevelsCount(); i++){
            printSpaces(LENGTH);
            System.out.println(ColorPalette.RED.getCode() + i + " - Map " + i + ColorPalette.RESET.getCode());
        }

        int choice = 0;
        do {
            printEmptyLines(1);
            printSpaces(LENGTH);
            println("Please enter your choice: ");
            while (!scanner.hasNextInt()) {
                printSpaces(LENGTH);
                println("Invalid input. Please enter a number from 1 to " + level);
                scanner.next();
            }
            choice = scanner.nextInt();
            if (choice < 1 || choice > level){
                printSpaces(LENGTH);
                println("Sorry, this map is closed. Please enter a number from 1 to " + level);
            }
        } while (choice < 1 || choice > level);

        return choice-1;

    }

    private int promptForRating() {
        int rating;
        do {
            printEmptyLines(HEIGHT);
            printSpaces(LENGTH);
            print("Please rate the game from 1 to 5: ");
            String input = scanner.next();
            try {
                rating = Integer.parseInt(input);
                if (rating < 1 || rating > 5) {
                    printSpaces(LENGTH);
                    println("Invalid rating. Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                printSpaces(HEIGHT);
                println("Invalid input. Please enter a number between 1 and 5.");
                rating = -1;
            }
        } while (rating < 1 || rating > 5);

        return rating;
    }

    private String promptForComment() {
        printEmptyLines(HEIGHT);
        printSpaces(LENGTH);
        println("Please enter your comment about the game: ");
        scanner.nextLine();
        return scanner.nextLine();
    }

    private void displayStatistic() {
        System.out.println(ColorPalette.GREEN.getCode());
        List<Score> topScores = scoreService.getTopScores("CubeMaze");
        List<Comment> comments = commentService.getComments("CubeMaze");

        printEmptyLines(1);
        printSpaces(LENGTH-4);
        System.out.println("=== Game Statistics for " + "CubeMaze" + " ===\n");
        printSpaces(LENGTH);
        System.out.println("Top Players and their Scores:\n");
        printSpaces(LENGTH);
        System.out.printf("%-20s   %-10s%n", "Player", "Score");
        for (Score score : topScores) {
            printSpaces(LENGTH);
            System.out.printf("%-20s | %-10d%n", score.getPlayer(), score.getPoints());
        }

        printEmptyLines(1);
        printSpaces(LENGTH);
        System.out.println("Comments and Ratings:\n");
        printSpaces(LENGTH/2 + LENGTH/3);
        System.out.printf("%-20s   %-25s   %-10s%n", "Player", "Comment", "Rating");
        for (Comment comment : comments) {
            try {
                int rating = ratingService.getRating("CubeMaze", comment.getPlayer());
                printSpaces(LENGTH/2 + LENGTH/3);
                System.out.printf("%-20s | %-25s | %-10d%n", comment.getPlayer(), comment.getComment(), rating);
            } catch (Exception e) {
                System.err.println("Error retrieving rating for comment by player " + comment.getPlayer() + ": " + e.getMessage());
            }
        }
        System.out.println(ColorPalette.RESET.getCode());
    }


    private String promptForPlayerName() {
        displayStatistic();
        printSpaces(LENGTH);
        print("Please enter your name: ");
        return scanner.next();
    }

    private void gameEnd(){
        setCurrentState(UiState.GAMEOVER);


        if(levelManager.hasNextLevel()){
            GameStateInfo gameStateInfo = new GameStateInfo(
                    levelManager.getCurrentLevelIndex(),
                    player.getMovesCount(),
                    levelManager.getCurrentLevel().getCube().getCoordinate(),
                    levelManager.getCurrentLevel().getCube().getSides()
            );
            gameStateService.saveGameState(
                    new GameState(
                        player.getName(),
                        "CubeMaze",
                        gameStateInfo.toJsonString(),
                        new Timestamp(System.currentTimeMillis())
                    )
            );
        }

        ratingService.setRating(
                new Rating(
                        player.getName(),
                        "CubeMaze",
                        promptForRating(),
                        new Timestamp(System.currentTimeMillis())
                )
        );
        commentService.addComment(
                new Comment(
                        player.getName(),
                        "CubeMaze",
                        promptForComment(),
                        new Timestamp(System.currentTimeMillis())
                )
        );
        scoreService.addScore(
                new Score(
                        player.getName(),
                        "CubeMaze",
                        player.getMovesCount(),
                        new Timestamp(System.currentTimeMillis())
                )
        );
        setCurrentState(UiState.CLOSE);
        closeScanner();
    }

    private void nextLevel(){
        setCurrentState(UiState.NEXTLEVEL);
        if(levelManager.hasNextLevel()){
            levelManager.goToNextLevel();
        }else {
            gameEnd();
        }
    }

    private void printEmptyLines(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println();
        }
    }

    private void printSpaces(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(" ");
        }
    }

    private void println(String message) {
        System.out.println(ColorPalette.GREEN.getCode() + message + ColorPalette.RESET.getCode());
    }

    private void print(String message) {
        System.out.print(ColorPalette.GREEN.getCode() + message + ColorPalette.RESET.getCode());
    }

    private void printWithDelay(String message, long delay) {
        try {
            print(message);
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            printError(e.getMessage());
        }
    }

    private void printError(String message) {
        System.out.println(message);
    }
}