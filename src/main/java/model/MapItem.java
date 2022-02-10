package model;

import javafx.scene.Node;

import java.util.ArrayList;

public abstract class MapItem {
    Vector2D position;
    GameMap map;

    public Vector2D getPosition(){
        return position;
    };
    public void setPosition(Vector2D position){
        this.position = position;
    };

    public void update() {

    };

    public abstract ArrayList<Node> getComponents();

    public GameMap getMap() { return map; }
}
