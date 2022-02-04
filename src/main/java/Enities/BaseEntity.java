package Enities;

import model.*;

import java.util.ArrayList;

public class BaseEntity extends MapItem {

    double radius;
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
        //percept
        //move
        //check for collisions at new position
        checkCollision();
    }

    private void checkCollision()
    {
        GameMap map = getMap();
        ArrayList<MapItem> items = map.getMapItems();
        for (int i = 0; i < items.size(); i++) {
            MapItem item = items.get(i);
            //check if body is an area, we maybe need a new method for this
            //current solution is kinda cheaty imo
            Area area;
            try{area = (Area) item;}
            catch (Exception ignored){
                continue;//skip for loop iteration since the item is not an area
            }

            //check if were inside the area
            if(!area.isInsideArea(getPosition())) {continue;}//were not in the area

            area.onAgentCollision(this);
        }
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

    //no idea if a see() and hear() method is appropriate should maybe be a percept() method instead

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
        //make new marker and add it to the list of items
        Marker marker = new Marker(markerType,new Vector2D(getPosition()),isIntruder);
        getMap().addMapItem(marker);
    }

    public double getRadius() {return radius;}
}
