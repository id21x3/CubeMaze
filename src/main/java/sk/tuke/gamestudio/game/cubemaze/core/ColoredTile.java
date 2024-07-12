package sk.tuke.gamestudio.game.cubemaze.core;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ColoredTile implements Tile{
    private TileState state;
    private ColorPalette color;

    public ColoredTile(){
        this(ColorPalette.RESET, TileState.TILE);
    }
    public ColoredTile(ColorPalette color){
        this(color, TileState.TILE);
    }
    public ColoredTile(TileState state){
        this(ColorPalette.RESET, state);
    }
    public ColoredTile(ColorPalette color, TileState state){
        this.color = color;
        this.state = state;
    }

}
