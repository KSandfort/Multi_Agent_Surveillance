package model;

/**
 * object constructed by 2 walls and one window in the middle
 * can be vertical or horizontal
 */
public class WallWithWindow extends WallWithItem{

    /**
     * Constructor
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     * @param vertical
     */
    public WallWithWindow(double xFrom, double yFrom, double xTo, double yTo, boolean vertical) {
        super(xFrom, yFrom, xTo, yTo, vertical);
    }

    /**
     * Creates a wall object that is part of this element.
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     * @param vertical
     */
    public void createWallItem(double xFrom, double yFrom, double xTo, double yTo, boolean vertical){
        double length;
        if (vertical){
            length = Math.abs(yFrom - yTo);
            setItem(new Window(xFrom, yFrom+(length/3), xTo, yFrom+2*(length/3)));
        } else {
            length = Math.abs(xFrom - xTo);
            setItem(new Window((xFrom - (length/3)), yFrom, xFrom - 2*(length/3), yTo));
        }
    }
}
