package model;

/**
 * object constructed by 2 walls and one door in the middle
 * can be vertical or horizontal
 */
public class WallWithDoor extends WallWithItem{

    public WallWithDoor(double xFrom, double yFrom, double xTo, double yTo, boolean vertical) {
        super(xFrom, yFrom, xTo, yTo, vertical);
    }

    public WallWithDoor(Vector2D pos1, Vector2D pos2, boolean vertical) {
        super(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), vertical);
    }

    public void createWallItem(double xFrom, double yFrom, double xTo, double yTo, boolean vertical){
        double length;
        if (vertical){
            length = Math.abs(yFrom - yTo);
            setItem(new Door(xFrom, yFrom+(length/3), xTo, yFrom+2*(length/3)));
        } else {
            length = Math.abs(xFrom - xTo);
            setItem(new Door((xFrom - (length/3)), yFrom, xFrom - 2*(length/3), yTo));
        }
    }
}
