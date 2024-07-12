package sk.tuke.gamestudio.game.cubemaze.core;

import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;

@Setter
@Getter
public class Cube {
    private Coordinate coordinate;
    private EnumMap<CubeSideType, CubeSide> sides;
    private static final int NUM_SIDES = 6;

    public Cube() {
        coordinate = new Coordinate();
        sides = new EnumMap<>(CubeSideType.class);
        initializeSides();
    }

    public void reset() {
        coordinate.reset();
        initializeSides();
    }

    private void initializeSides() {
        for (CubeSideType sideType : CubeSideType.values()) {
            sides.put(sideType, new CubeSide(sideType));
        }
    }

    public void move(MoveDirection direction) {
        CubeSide[] newSides = new CubeSide[NUM_SIDES];
        CubeSide[] oldSides = sides.values().toArray(new CubeSide[0]);

        switch (direction) {
            case FORWARD:
            case BACKWARD:
                int yOffset = (direction == MoveDirection.FORWARD) ? -1 : 1;
                coordinate.increaseY(yOffset);
                rotateYAxis(oldSides, newSides, yOffset);
                break;
            case LEFT:
            case RIGHT:
                int xOffset = (direction == MoveDirection.LEFT) ? -1 : 1;
                coordinate.increaseX(xOffset);
                rotateXAxis(oldSides, newSides, xOffset);
                break;
        }

        sides = new EnumMap<>(CubeSideType.class);
        for (int i = 0; i < NUM_SIDES; i++) {
            sides.put(CubeSideType.values()[i], newSides[i]);
        }
    }

    private void rotateXAxis(CubeSide[] oldSides, CubeSide[] newSides, int xOffset) {
        newSides[CubeSideType.FRONT.ordinal()] = oldSides[CubeSideType.FRONT.ordinal()];
        newSides[CubeSideType.BACK.ordinal()] = oldSides[CubeSideType.BACK.ordinal()];


        if (xOffset == 1) {
            newSides[CubeSideType.TOP.ordinal()] = oldSides[CubeSideType.LEFT.ordinal()];
            newSides[CubeSideType.BOTTOM.ordinal()] = oldSides[CubeSideType.RIGHT.ordinal()];
            newSides[CubeSideType.LEFT.ordinal()] = oldSides[CubeSideType.BOTTOM.ordinal()];
            newSides[CubeSideType.RIGHT.ordinal()] = oldSides[CubeSideType.TOP.ordinal()];
        } else {
            newSides[CubeSideType.TOP.ordinal()] = oldSides[CubeSideType.RIGHT.ordinal()];
            newSides[CubeSideType.BOTTOM.ordinal()] = oldSides[CubeSideType.LEFT.ordinal()];
            newSides[CubeSideType.LEFT.ordinal()] = oldSides[CubeSideType.TOP.ordinal()];
            newSides[CubeSideType.RIGHT.ordinal()] = oldSides[CubeSideType.BOTTOM.ordinal()];
        }
    }

    private void rotateYAxis(CubeSide[] oldSides, CubeSide[] newSides, int yOffset) {
        newSides[CubeSideType.LEFT.ordinal()] = oldSides[CubeSideType.LEFT.ordinal()];
        newSides[CubeSideType.RIGHT.ordinal()] = oldSides[CubeSideType.RIGHT.ordinal()];


        if (yOffset == 1) {
            newSides[CubeSideType.FRONT.ordinal()] = oldSides[CubeSideType.BOTTOM.ordinal()];
            newSides[CubeSideType.BACK.ordinal()] = oldSides[CubeSideType.TOP.ordinal()];
            newSides[CubeSideType.TOP.ordinal()] = oldSides[CubeSideType.FRONT.ordinal()];
            newSides[CubeSideType.BOTTOM.ordinal()] = oldSides[CubeSideType.BACK.ordinal()];
        } else {
            newSides[CubeSideType.FRONT.ordinal()] = oldSides[CubeSideType.TOP.ordinal()];
            newSides[CubeSideType.BACK.ordinal()] = oldSides[CubeSideType.BOTTOM.ordinal()];
            newSides[CubeSideType.TOP.ordinal()] = oldSides[CubeSideType.BACK.ordinal()];
            newSides[CubeSideType.BOTTOM.ordinal()] = oldSides[CubeSideType.FRONT.ordinal()];
        }
    }


    public void setSide(CubeSideType sideType, CubeSide side) {
        sides.put(sideType, side);
    }

    public CubeSide getSide(CubeSideType sideType) {
        return sides.get(sideType);
    }
}
