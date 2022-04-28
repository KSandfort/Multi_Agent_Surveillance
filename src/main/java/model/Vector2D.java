package model;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

/**
 * Class that represents a two-dimensional vector.
 */
@Getter
@Setter
public class Vector2D {

    // Variables
    private double x;
    private double y;

    /**
     * Constructor
     * @param x
     * @param y
     */
    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a new vector that adds together vectors a and b.
     * @param a
     * @param b
     * @return
     */
    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.getX() + b.getX(), a.getY() + b.getY());
    }

    /**
     * Returns a new vector that subtracts vector b from vector a.
     * @param a
     * @param b
     * @return
     */
    public static Vector2D subtract(Vector2D a, Vector2D b) {
        return new Vector2D(a.getX() - b.getX(), a.getY() - b.getY());
    }

    public static double length(Vector2D a) {
        return Math.sqrt(Math.pow(a.getX(), 2) + Math.pow(a.getY(), 2));
    }

    public static double distance(Vector2D a, Vector2D b) {
        return Vector2D.length(Vector2D.subtract(a, b));
    }

    /**
     * Distance from the line a -> b to the line c -> d.
     * Returns the Double.MAX_VALUE if the lines don't touch or the distance is "negative".
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    public static double distance(Vector2D a, Vector2D b, Vector2D c, Vector2D d) {
        Vector2D e = Vector2D.subtract(b, a);
        Vector2D f = Vector2D.subtract(d, c);
        Vector2D p = new Vector2D(-e.getY(), e.getX());

        // Check that the distance isn't calculated in a negative direction.
        Vector2D p2 = new Vector2D(-f.getY(), f.getX());
        if (Vector2D.dotProduct(e, p2) == 0) {
            return Double.MAX_VALUE;
        }
        double h2 = Vector2D.dotProduct(Vector2D.subtract(c, a), p2) / Vector2D.dotProduct(e, p2);
        if (h2 < 0) {
            return Double.MAX_VALUE;
        }

        if (Vector2D.dotProduct(f, p) == 0) {
            return Double.MAX_VALUE;
        }
        double h = Vector2D.dotProduct(Vector2D.subtract(a, c), p) / Vector2D.dotProduct(f, p);
        if (h >= 0 && h <= 1) {
            return Vector2D.distance(a, Vector2D.add(c, Vector2D.scalar(f, h)));
        }
        else {
            return Double.MAX_VALUE;
        }
    }

    /**
     * Returns a new vector that is the normalized version of an input vector a.
     * @param a
     * @return
     */
    public static Vector2D normalize(Vector2D a) {
        double length = Vector2D.length(a);
        return new Vector2D(a.getX()/length, a.getY()/length);
    }

    public void normalize(){
        double length = Vector2D.length(this);
        this.setX(getX()/length);
        this.setY(getY()/length);
    }

    /**
     * Calculates the dot product of two given vectors
     * @param a first vector
     * @param b second vector
     * @return dot product
     */
    public static double dotProduct(Vector2D a, Vector2D b) {
        return (a.getX() * b.getX()) + (a.getY() * b.getY());
    }

    /**
     * Scales vector by a scalar and returns the result as a new vector.
     * @param a
     * @param factor
     * @return
     */
    public static Vector2D scalar(Vector2D a, double factor) {
        return new Vector2D(a.getX() * factor, a.getY() * factor);
    }

    public static Vector2D resize(Vector2D vector, double scalar) {
        Vector2D norm = Vector2D.normalize(vector);
        return Vector2D.scalar(norm, scalar);
    }

    /**
     * Returns true, if the line from a to b intersects with the line from c to d.
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    public static boolean doTwoLinesCross(Vector2D a, Vector2D b, Vector2D c, Vector2D d) {
        Vector2D e = Vector2D.subtract(b, a);
        Vector2D f = Vector2D.subtract(d, c);
        Vector2D p = new Vector2D(-e.getY(), e.getX());
        if (Vector2D.dotProduct(f, p) == 0) {
            return false;
        }
        double h = Vector2D.dotProduct(Vector2D.subtract(a, c), p) / Vector2D.dotProduct(f, p);
        if (h > 0 && h < 1) {
            return true;
        }
        else {
            return false;
        }
    }

    public void pivot(double angle){
        x = getX()*Math.cos(Math.toRadians(angle)) - getY()*Math.sin(Math.toRadians(angle));
        y = getX()*Math.sin(Math.toRadians(angle)) + getY()*Math.cos(Math.toRadians(angle));
    }

    /**
     * checks if 2 line segments ab, cd intersect
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    public static boolean doTwoSegmentsCross(Vector2D a, Vector2D b, Vector2D c, Vector2D d){
        int orientA = checkOrientation(a, c, d);
        int orientB = checkOrientation(b, c, d);
        int orientC = checkOrientation(c, a, b);
        int orientD = checkOrientation(d, a, b);

        if (orientA != orientB && orientC != orientD) return true;

        //check collinearity
        return (checkCollinearity(orientA, a, c, d) || checkCollinearity(orientB, b, c, d) ||
                checkCollinearity(orientC, c, a, b) ||checkCollinearity(orientD, d, a, b));
    }

    /**
     * checks if vector a lies on the line segment between b and c
     * @return true if
     */
    public static boolean checkCollinearity(int o, Vector2D a,Vector2D b, Vector2D c) {
        return (o == 0 && isOnSegment(a,b,c));
    }

    /**
     * checks if a point is on the line segment of two other points
     * @param a point that is to be checked
     * @param b start of line segment
     * @param c end of line segment
     * @return
     */
    public static boolean isOnSegment(Vector2D a,Vector2D b, Vector2D c){
        boolean lineOnX = (a.getX() <= Math.max(b.getX(), c.getX())) && (a.getX() >= Math.min(b.getX(), c.getX()));
        boolean lineOnY = (a.getY() <= Math.max(b.getY(), c.getY())) && (a.getY() >= Math.min(b.getY(), c.getY()));
        return (lineOnX && lineOnY);
    }

    /**
     * finds orientation of one point to a line segment
     * @param a point to check orientation
     * @param b start of the line segment
     * @param c end of the line segment
     * @return 0 if the point lies on the line segment, 1 for clockwise orientation, 2 for counterclockwise orientation
     */
    public static int checkOrientation(Vector2D a, Vector2D b, Vector2D c){
        Vector2D intersectB = subtract(a, b);
        Vector2D intersectC = subtract(a, c);
        double orientation = crossProduct(intersectB, intersectC);

        if (orientation == 0) return 0;
        else if (orientation > 0) return 1; //clockwise
        else return 2;
    }

    /**
     * calculates cross product of two vectors
     * @param a
     * @param b
     * @return
     */
    private static double crossProduct(Vector2D a, Vector2D b){
        return (a.getX()*b.getY()) - (a.getY()* b.getX());
    }

    public static Vector2D randomVector(){
        Random rand = new Random();
        double newX = rand.nextDouble()*100 - 50;
        double newY = rand.nextDouble()*100 - 50;
        return new Vector2D(newX, newY);
    }

    /**
     * atan2 in degrees
     * @param a first vector (origin)
     * @param b second vector
     * @return
     */
    public static double shortestAngle(Vector2D a, Vector2D b) {
        return Math.atan2(b.getY()*a.getX() - b.getX()*a.getY(), b.getX()*a.getX() - b.getY()*a.getY())*180/Math.PI;
    }

    public String print() {
        return ("Vector: x = " + x + ", y = " + y);
    }

    public Vector2D clone() {
        return new Vector2D(x,y);
    }

}


