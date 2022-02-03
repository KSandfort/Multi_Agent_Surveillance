package model;

public class SpawnArea extends Area{
    public SpawnArea(int length, int width) {
        super(length, width);
    }

    public SpawnArea(int posX, int posY, int posXend, int posYend) {
        super(posX, posY, posXend, posYend);
    }
}
