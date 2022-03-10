package Enities;

import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

    /**
     * @param entity
     * @return closest item that is detected by the ray, null if there is no item
     */
    public MapItem getDetectedItems(Entity entity){
        double minDistance = entity.fovDepth;
        MapItem closestItem = null;

        ArrayList<MapItem> allItems = new ArrayList<>();
        allItems.addAll(entity.getMap().getStaticItems());
        allItems.addAll(entity.getMap().getMovingItems());

        // Scan all static items on the map
        for (MapItem item : allItems){
            // Find the closest object
            for (int j = 0; j < item.getCornerPoints().length; j++){
                double currentDistance = Vector2D.distance(this.getOrigin(), this.getPoint(), item.getCornerPoints()[j], item.getCornerPoints()[(j + 1) % 4]);
                if (currentDistance < minDistance && currentDistance > 0 && item != entity) {
                    minDistance = currentDistance;
                    closestItem = item;
                }
            }
        }
        return closestItem;
    }

}
