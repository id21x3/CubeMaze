package sk.tuke.backend.gamestudio.game.cubemaze.core.cube;

import lombok.Getter;
import lombok.Setter;
import sk.tuke.backend.gamestudio.game.cubemaze.core.field.ColorPalette;

@Setter
@Getter
public class CubeSide {
    private CubeSideType type;
    private ColorPalette color;

    public CubeSide() {
        this(CubeSideType.FRONT, ColorPalette.RESET);
    }

    public CubeSide(CubeSideType type) {
        this(type, ColorPalette.RESET);
    }

    public CubeSide(CubeSideType type, ColorPalette color) {
        this.type = type;
        this.color = color;
    }

}

