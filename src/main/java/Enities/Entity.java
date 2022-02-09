package Enities;

import javafx.scene.Node;
import model.MapItem;
import model.Vector2D;

public abstract class Entity extends MapItem {
    double explorationFactor;
    double speed;
    double fovAngle;
    double fovLength;
    Vector2D fovDirection;
    boolean isIntruder;

    public Entity(int x, int y){
        this.setPosition(new Vector2D(x,y));
    }
}
