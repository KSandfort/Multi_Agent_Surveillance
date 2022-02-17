package model;

import javafx.scene.Node;

import java.util.ArrayList;

public abstract class MapItem {
    Vector2D position;
    protected GameMap map;

    public Vector2D getPosition(){
        return position;
    };
    public void setPosition(Vector2D position){
        this.position = position;
    };

    public void update(ArrayList<MapItem> items) {

    };

    public void setMap(GameMap map){
        this.map = map;
    }

    public abstract ArrayList<Node> getComponents();

    public GameMap getMap() { return map; }

    public abstract boolean isSolidBody();

    public abstract boolean isDynamicObject();

    public abstract boolean isTransparentObject();

}
