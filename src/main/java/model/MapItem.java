package model;

import Enities.Entity;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Abstract class for any item on the map.
 */
@Getter
@Setter
public abstract class MapItem {

    // Variables
    protected Vector2D position;
    protected GameMap map;

    public void update(ArrayList<MapItem> items) {
    };

    public abstract ArrayList<Node> getComponents();

    public abstract void onAgentCollision(Entity entity);

    public Vector2D [] getCornerPoints(){
        return new Vector2D[]{position};
    }

    public abstract boolean isSolidBody();

    public abstract boolean isDynamicObject();

    public abstract boolean isTransparentObject();

}
