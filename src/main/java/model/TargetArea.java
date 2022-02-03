package model;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

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
