package sk.tuke.backend.gamestudio.game.cubemaze.core.field;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClearTile implements Tile{

    @JsonProperty
    public String getType() {
        return "clear";
    }


}
