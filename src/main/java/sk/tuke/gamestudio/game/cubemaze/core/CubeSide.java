package sk.tuke.gamestudio.game.cubemaze.core;

import lombok.Getter;
import lombok.Setter;

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

