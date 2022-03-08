package Enities;

import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.Vector2D;

/**
 * This class represents a ray in the simulation that builds the vision
 * in a FOV of an entity.
 */
public class Ray {

    // Variables
    Vector2D origin;
    Vector2D direction;

    /**
     * Constructor
     * @param origin
     * @param direction
     */
    public Ray(Vector2D origin, Vector2D direction){
        this.origin = origin;
        this.direction = direction;
    }

    /**
     * Returns the point the ray is pointing at.
     * @return
     */
    public Vector2D getPoint(){
        return Vector2D.add(origin, direction);
    }

    /**
     * Determinant calculation.
     * @param a
     * @param b
     * @return
     */
    public static double det(Double[] a, Double [] b){
        return a[0]*b[1] - a[1] * b[0];
    }

    public Node getComponent(){
        double sf = SimulationGUI.SCALING_FACTOR;
        int offset = SimulationGUI.CANVAS_OFFSET;
        Line line = new Line(
                (origin.getX() * sf) + offset,
                (origin.getY() * sf) + offset,
                (getPoint().getX() * sf) + offset,
                (getPoint().getY() * sf) + offset);
        line.setStroke(Color.rgb(0,0,0,0.2));
        return line;
    }

    public Vector2D getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2D origin) {
        this.origin = origin;
    }

    public Vector2D getDirection() {
        return direction;
    }

    public void setDirection(Vector2D direction) {
        this.direction = direction;
    }
}
