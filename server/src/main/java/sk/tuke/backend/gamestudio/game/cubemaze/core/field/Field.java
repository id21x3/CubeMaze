package sk.tuke.backend.gamestudio.game.cubemaze.core.field;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import sk.tuke.backend.gamestudio.game.cubemaze.core.MoveDirection;
import sk.tuke.backend.gamestudio.game.cubemaze.core.cube.Cube;
import sk.tuke.backend.gamestudio.game.cubemaze.core.cube.CubeSideType;
import sk.tuke.backend.gamestudio.game.cubemaze.core.game.GameException;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Field {
    private Tile[][] field;
    private Cube cube;

    public Field(Cube cube) {
        this.cube = cube;
    }

    public void prepareStructures(JsonObject jsonObject) throws GameException {
        if (jsonObject.has("map")) {
            List<int[]> rows = new ArrayList<>();
            int cubeRow = -1;
            int cubeCol = -1;

            for (int i = 0; i < jsonObject.getAsJsonArray("map").size(); i++) {
                List<Integer> rowList = new ArrayList<>();
                for (var element : jsonObject.getAsJsonArray("map").get(i).getAsJsonArray()) {
                    rowList.add(element.getAsInt());
                }
                int[] rowArray = rowList.stream().mapToInt(Integer::intValue).toArray();
                rows.add(rowArray);

                if (rowList.contains(9)) {
                    cubeRow = i;
                    cubeCol = rowList.indexOf(9);
                }
            }

            if (cubeRow != -1 && cubeCol != -1) {
                generateField(rows);
                cube.getCoordinate().setCoordinate(cubeCol, cubeRow);
            } else {
                throw new GameException("Error: Cube coordinates not found in the map.");
            }
        } else {
            throw new GameException("Error: JSON file does not contain a 'map' field.");
        }
    }

    public void generateField(List<int[]> f) {
        field = new Tile[f.size()][];
        for (int i = 0; i < f.size(); i++) {
            int[] row = f.get(i);
            field[i] = new Tile[row.length];
            for (int j = 0; j < row.length; j++) {
                Tile tile = switch (row[j]) {
                    case 0 -> new ClearTile();
                    case 2 -> new ColoredTile(ColorPalette.RED);
                    case 3 -> new PaintingTile(ColorPalette.RED);
                    case 4 -> new ColoredTile(ColorPalette.YELLOW);
                    case 5 -> new PaintingTile(ColorPalette.YELLOW);
                    case 6 -> new ColoredTile(ColorPalette.CYAN);
                    case 7 -> new PaintingTile(ColorPalette.CYAN);
                    case 8 -> new ColoredTile(TileState.FINISH);
                    case 9 -> new ColoredTile(TileState.CUBE_ON);
                    default -> new ColoredTile();
                };
                field[i][j] = tile;
            }
        }
    }

    public int moveCube(char direction) {
        int newX = cube.getCoordinate().getX();
        int newY = cube.getCoordinate().getY();
        MoveDirection moveDirection;
        Tile currentTile = field[newY][newX];

        switch (direction) {
            case 'w', 'W' -> { newY--; moveDirection = MoveDirection.FORWARD; }
            case 'a', 'A' -> { newX--; moveDirection = MoveDirection.LEFT; }
            case 'd', 'D' -> { newX++; moveDirection = MoveDirection.RIGHT; }
            case 's', 'S' -> { newY++; moveDirection = MoveDirection.BACKWARD; }
            default -> {
                return 0;
            }
        }

        if (isValidMove(newX, newY, moveDirection)) {
            ((ColoredTile) currentTile).setState(TileState.TILE);

            cube.move(moveDirection);

            ColoredTile newTile = (ColoredTile) field[newY][newX];

            if (newTile
                    .getState()
                    .equals(TileState.FINISH)) {
                newTile.setState(TileState.CUBE_ON);
                return 1;
            }
            newTile.setState(TileState.CUBE_ON);
        }
        return 0;
    }

    public boolean isValidMove(int x, int y, MoveDirection direction) {
        if (x < 0 || x >= field[0].length || y < 0 || y >= field.length)
            return false;
        if(field[y][x] instanceof ClearTile)
            return false;


        ColoredTile coloredTile = (ColoredTile) field[y][x];

        TileState state = coloredTile.getState();
        CubeSideType sideType = getSideTypeForDirection(direction);

        if(coloredTile instanceof PaintingTile)
            cube.getSide(sideType).setColor(coloredTile.getColor());

        return state == TileState.FINISH ||
                (state == TileState.TILE &&
                        (coloredTile instanceof PaintingTile ||
                                coloredTile.getColor() == cube.getSide(sideType).getColor()));
    }

    private CubeSideType getSideTypeForDirection(MoveDirection direction) {
        return switch (direction) {
            case FORWARD -> CubeSideType.FRONT;
            case BACKWARD -> CubeSideType.BACK;
            case LEFT -> CubeSideType.LEFT;
            case RIGHT -> CubeSideType.RIGHT;
        };
    }

    public void reset() {
        cube.reset();
        generateField(new ArrayList<>());
    }

    public JsonObject getFieldDataAsJson() {
        JsonObject fieldData = new JsonObject();
        JsonArray rowsData = new JsonArray();

        for (Tile[] row : this.field) {
            JsonArray rowData = new JsonArray();
            for (Tile tile : row) {
                JsonObject tileData = new JsonObject();
                tileData.addProperty("type", tile.getClass().getSimpleName());
                if (tile instanceof ColoredTile) {
                    tileData.addProperty("color", ((ColoredTile) tile).getColor().toString());
                    tileData.addProperty("state", ((ColoredTile) tile).getState().toString());
                }
                rowData.add(tileData);
            }
            rowsData.add(rowData);
        }
        fieldData.add("field", rowsData);
        fieldData.add("cube", getCubeDataAsJson());

        return fieldData;
    }

    public JsonObject getCubeDataAsJson() {
        JsonObject cubeData = new JsonObject();

        cubeData.addProperty("x", cube.getCoordinate().getX());
        cubeData.addProperty("y", cube.getCoordinate().getY());

        JsonObject sidesData = new JsonObject();
        for (CubeSideType sideType : CubeSideType.values()) {
            sidesData.addProperty(sideType.toString(), cube.getSide(sideType).getColor().toString());
        }
        cubeData.add("sides", sidesData);

        return cubeData;
    }




}

