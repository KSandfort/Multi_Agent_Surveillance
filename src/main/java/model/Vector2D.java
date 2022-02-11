package model;

import java.util.Random;

/**
 * Class that represents a two-dimensional vector.
 */
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

    public static Vector2D resize(Vector2D vector, double scalar){
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


    /**
     * Getter for x-coordinate.
     * @return
     */
    public double getX(){
        return x;
    }

    /**
     * Setter for x-coordinate.
     * @param x
     */
    public void setX(double x){
        this.x = x;
    }

    /**
     * Getter for y-coordinate.
     * @return
     */
    public double getY(){
        return y;
    }

    /**
     * Setter for y-coordinate.
     * @param y
     */
    public void setY(double y){
        this.y = y;
    }

    public static Vector2D randomVector(){
        Random rand = new Random();
        double newX = rand.nextDouble()*100 - 50;
        double newY = rand.nextDouble()*100 - 50;
        return new Vector2D(newX, newY);
    }

    public double dist(Vector2D other){
        double a = other.getX() - getX();
        double b = other.getY() - getY();
        return Math.sqrt(Math.pow(a,2) + Math.pow(b,2));
    }

}


