package sk.tuke.gamestudio;

import sk.tuke.gamestudio.game.cubemaze.consoleui.ConsoleUiManager;
import sk.tuke.gamestudio.game.cubemaze.core.Field;
import sk.tuke.gamestudio.game.cubemaze.core.Cube;
import sk.tuke.gamestudio.game.cubemaze.core.LevelManager;

import java.io.IOException;

public class ClientApplication {
    public static void main(String[] args) throws IOException {
        //LanternUiManager uiManager = new LanternUiManager(new Field(new Cube()));
        ConsoleUiManager uiManager = new ConsoleUiManager(new LevelManager("maps/levels.json"));
        uiManager.run();
    }
}
