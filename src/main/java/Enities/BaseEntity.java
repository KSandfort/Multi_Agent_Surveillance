package Enities;

import model.*;

public class BaseEntity extends MapItem {

    double movementSpeed;
    double fovAngle;
    double visionRange;
    //angle of forward facing vector with respect to the entity,
    //0 degrees is in +x direction turning counter clockwise with an increase of angle
    double angle;
    double turnSpeed;//rotation in degrees/sec
    boolean isIntruder = false;//set to false for now since we dont use it yet
    boolean isSprinting = true;
    double sprintMovementFactor;//number by which the movement speed needs to be increased when sprinting
    double sprintRotationFactor;//number by which the rotation speed needs to be decreased when sprinting

    public void update()
    {


        checkCollision();
    }

    private void checkCollision()
    {

    }

    //move the entity
    public void move(double timeStep)
    {
        //increase speed when sprinting
        double speed = isSprinting ? movementSpeed * sprintMovementFactor : movementSpeed;

        //calculate direction vector based on angle
        double deltaX = Math.cos(angle) * speed * timeStep;
        double deltaY = Math.sin(angle) * speed * timeStep;

        getPosition().add(deltaX,deltaY);
    }

    public void turn(boolean left,double timeStep)
    {
        double rotationDirection = left ? 1 : -1;
        double rotationSpeed = isSprinting ? turnSpeed * sprintRotationFactor : turnSpeed;

        angle += rotationSpeed * rotationDirection * timeStep;
    }

    //no idea if a see() and hear() method is appropriate should meybe be a percept() method instead

    //entity will have to be able to see its environments
    public void see()
    {
        //check distance to see if calculation is necessary

        //check angle to see if it is within FOV

        //i guess do stuff if it is in FOV

        //walls will probably need some different calculations since you cant consider is a circle
    }

    //entity needs to be able to hear
    public void hear()
    {

    }

    //entity needs to be able to place markers for indirect communication
    public void placeMarker(MarkerType markerType)
    {
        Marker marker = new Marker(markerType,isIntruder);
        //TODO: marker placement in map(need the map to be implemented first)
    }

}
