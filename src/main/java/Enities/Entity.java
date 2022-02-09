package Enities;

import javafx.scene.Node;
import model.MapItem;
import model.Vector2D;

public abstract class Entity extends MapItem {
    double explorationFactor;
    double speed;
    double fovAngle;
    double fovLength;
    //Vector2D fovDirection;
    double angle;
    boolean isIntruder;
    double sprintMovementFactor;//number by which the movement speed needs to be increased when sprinting
    double sprintRotationFactor;//number by which the rotation speed needs to be decreased when sprinting
    boolean isSprinting = true;

    double turnSpeed;//rotation in degrees/sec
    double radius;//width of the entity

    Vector2D velocity;

    public Entity(int x, int y){
        this.setPosition(new Vector2D(x,y));
        velocity = new Vector2D(0, 0);
    }

    public void update() {
        this.setPosition(Vector2D.add(getPosition(), velocity));
    }
}
