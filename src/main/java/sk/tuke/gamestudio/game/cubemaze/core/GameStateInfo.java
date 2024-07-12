package sk.tuke.gamestudio.game.cubemaze.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.EnumMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameStateInfo {
    private int level;
    private int playerScore;
    private Coordinate playerCoordinate;
    private EnumMap<CubeSideType, CubeSide> cubeSides;

    public String toJsonString() throws GameException{
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        }catch (JsonProcessingException e){
            throw new GameException("Error parsing to JSON file: ", e);
        }
    }

    public static GameStateInfo fromJsonString(String jsonString) throws GameException{
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, GameStateInfo.class);
        }catch (JsonProcessingException e){
            throw new GameException("Error parsing from JSON file: ", e);
        }

    }
}
