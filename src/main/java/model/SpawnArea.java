package model;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class SpawnArea extends Area{
    public SpawnArea(int length, int width) {
        super(length, width);
    }

    public SpawnArea(int posX, int posY, int posXend, int posYend) {
        super(posX, posY, posXend, posYend);
    }

    @Override
    public Node getComponent() {
        Rectangle rect = new Rectangle();
        rect.setX(getPosition().getX());
        rect.setY(getPosition().getY());
        rect.setWidth(getWidth());
        rect.setHeight(getLength());
        return rect;
    }
}
