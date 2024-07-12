package sk.tuke.backend.gamestudio.game.cubemaze.core.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import sk.tuke.backend.gamestudio.game.cubemaze.core.Coordinate;
import sk.tuke.backend.gamestudio.game.cubemaze.core.field.Field;
import sk.tuke.backend.gamestudio.game.cubemaze.core.cube.Cube;
import sk.tuke.backend.gamestudio.game.cubemaze.core.cube.CubeSide;
import sk.tuke.backend.gamestudio.game.cubemaze.core.cube.CubeSideType;
import sk.tuke.backend.gamestudio.game.cubemaze.core.field.ColoredTile;
import sk.tuke.backend.gamestudio.game.cubemaze.core.field.TileState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class LevelManager {
    @Getter
    private List<Field> levels;

    @Getter
    @Setter
    private int currentLevelIndex;
    private final String filePath;

    public LevelManager(String filePath) {
        this.filePath = filePath;
        this.currentLevelIndex = 0;
    }

    public void loadLevels() throws GameException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new GameException("Error: Levels file not found.");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonStringBuilder.append(line).append('\n');
            }

            JsonObject jsonObject = JsonParser.parseString(jsonStringBuilder.toString()).getAsJsonObject();

            if (jsonObject.has("levels")) {
                levels = new ArrayList<>();
                for (var levelJson : jsonObject.getAsJsonArray("levels")) {
                    Field field = new Field(new Cube());
                    field.prepareStructures(levelJson.getAsJsonObject());
                    levels.add(field);
                }
            } else {
                throw new GameException("Error: JSON file does not contain a 'levels' field.");
            }
        } catch (IOException e) {
            throw new GameException("Error reading JSON file: " + e.getMessage());
        }
    }


    public Field getCurrentLevel() {
        if (levels == null || levels.isEmpty()) {
            return null;
        }
        return levels.get(currentLevelIndex);
    }

    public void goToNextLevel() {
        currentLevelIndex++;
    }

    public boolean hasNextLevel() {
        return levels != null && currentLevelIndex < levels.size() - 1;
    }

    public void reset() {
        currentLevelIndex = 0;
    }

    public int getLevelsCount(){
        return  levels.size();
    }

    public void prepareLevel(int level, Coordinate coordinate, EnumMap<CubeSideType, CubeSide> cubeSides) {
        currentLevelIndex = level;
        Coordinate oldCoordinate = getCurrentLevel().getCube().getCoordinate();
        ((ColoredTile) getCurrentLevel().getField()[oldCoordinate.getY()][oldCoordinate.getX()]).setState(TileState.TILE);

        getCurrentLevel().getCube().setCoordinate(coordinate);
        getCurrentLevel().getCube().setSides(cubeSides);

        ((ColoredTile) getCurrentLevel().getField()[coordinate.getY()][coordinate.getX()]).setState(TileState.CUBE_ON);
    }
}
