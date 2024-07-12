package sk.tuke.gamestudio.game.cubemaze.core;


public class PaintingTile extends ColoredTile{
    private boolean IsCanPaintCubeSide;

    public PaintingTile(ColorPalette color){
        super(color);
        IsCanPaintCubeSide = true;
    }

    public boolean isCanPaintCubeSide() {
        return IsCanPaintCubeSide;
    }

    public void setCanPaintCubeSide(boolean canPaintCubeSide) {
        IsCanPaintCubeSide = canPaintCubeSide;
    }
}

