package model;

public class Wall extends Area{
    public Wall(int length, int width) {
        super(length, width);
    }

    public Wall(int posX, int posY, int posXend, int posYend) {
        super(posX, posY, posXend, posYend);
    }

}
