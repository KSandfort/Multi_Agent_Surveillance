package model;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class Wall extends Area {
    public Wall(int length, int width) {
        super(length, width);
    }

    public Wall(int posX, int posY, int posXend, int posYend) {
        super(posX, posY, posXend, posYend);
    }

    //returns javafx component
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
