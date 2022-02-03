package model;

public class TargetArea extends Area{

    public TargetArea(int length, int width) {
        super(length, width);
    }

    public TargetArea(int posX, int posY, int length, int width) {
        super(posX, posY, length, width);
    }

    public boolean insideTargetArea(Vector2D coordinates){
        return isInsideArea(coordinates);
    }
}
