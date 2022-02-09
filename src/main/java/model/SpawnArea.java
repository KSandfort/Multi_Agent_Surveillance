package model;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class SpawnArea extends Area{
    public SpawnArea(Vector2D pos1, Vector2D pos2, Vector2D pos3, Vector2D pos4) {
        super(pos1, pos2, pos3, pos4);
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
