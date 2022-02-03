package Enities;

public class BaseEntity {

    double movementSpeed;
    double fovAngle;
    double visionRange;
    //angle of forward facing vector with respect to the entity,
    //0 degrees is in +x direction turning counter clockwise with an increase of angle
    double angle;
    double turnSpeed;//rotation in degrees/sec
    boolean isIntruder = false;//set to false for now since we dont use it yet

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

    }

    //entity will have to be able to see its environments
    public void see()
    {
        //no clue if a method is enough
    }

    //entity needs to be able to hear
    public void hear()
    {
        //no clue if a method is enough
    }

    //entity needs to be able to place markers for indirect communication
    public void placeMarker(MarkerType markerType)
    {
        Marker marker = new Marker(markerType,isIntruder);
        //TODO: marker placement in map(need the map to be implemented first)
    }

}
