package model;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class TargetArea extends Area{

    public TargetArea(Vector2D pos1, Vector2D pos2, Vector2D pos3, Vector2D pos4) {
        super(pos1, pos2, pos3, pos4);
    }

    public boolean insideTargetArea(Vector2D coordinates){
        return isInsideArea(coordinates);
    }

    @Override
    public ArrayList<Node> getComponents() {
        // TODO: Add 4 lines that mark the border
        Rectangle rect = new Rectangle();
        rect.setX(getPosition().getX());
        rect.setY(getPosition().getY());


        return null;
    }
}
