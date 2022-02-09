package model;

import javafx.scene.Node;

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

    public abstract Node getComponent();
    public GameMap getMap() { return map; }
}
