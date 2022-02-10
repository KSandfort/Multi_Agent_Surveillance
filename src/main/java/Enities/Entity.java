package Enities;

import model.MapItem;
import model.Vector2D;

/**
 * Abstract class of an entity on the map.
 */
public abstract class Entity extends MapItem {
    double explorationFactor;
    double fovAngle;
    double fovLength;
    double angle;
    //Vector2D fovDirection;
    Vector2D direction;
    boolean isIntruder;
    double sprintMovementFactor;//number by which the movement speed needs to be increased when sprinting
    double sprintRotationFactor;//number by which the rotation speed needs to be decreased when sprinting
    boolean isSprinting = true;

    double turnSpeed;//rotation in degrees/sec
    double radius;//width of the entity

    protected double velocity;

    public Entity(double x, double y) {
        this.setPosition(new Vector2D(x,y));
        this.direction = new Vector2D(1, 0);
        velocity = 0;
    }

    /**
     * Gives the Entity a new position, based on the direction the Entity is looking at and the
     * current velocity.
     */
    public void update() {
        this.setPosition(Vector2D.add(getPosition(), Vector2D.scalar(direction, velocity)));
    }

    public Vector2D getDirection() {
        return direction;
    }

    public void setDirection(Vector2D direction) {
        this.direction = direction;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
}
