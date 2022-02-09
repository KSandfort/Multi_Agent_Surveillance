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
        double newX = rand.nextDouble()*100;
        double newY = rand.nextDouble()*100;
        return new Vector2D(newX, newY);
    }

}


