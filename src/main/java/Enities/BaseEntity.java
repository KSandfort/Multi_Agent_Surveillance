package Enities;

public class BaseEntity {

    double movementSpeed;
    double sprintFactor;//number by which the movement speed needs to be increased when sprinting
    double fovAngle;
    double visionRange;
    //angle of forward facing vector with respect to the entity,
    //0 degrees is in +x direction turning counter clockwise with an increase of angle
    double angle;
    double turnSpeed;//rotation in degrees/sec
    boolean isIntruder = false;//set to false for now since we dont use it yet
    boolean isSprinting = true;
    //move the entity
    public void move()
    {
        //calculate direction vector based on angle
        double dirX = Math.cos(angle);
        double dirY = Math.sin(angle);

        //TODO:update position using calculated direction
    }

    public void turn(boolean left)
    {
        double rotation = left ? 1 : -1;
        //TODO: same as move method
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
