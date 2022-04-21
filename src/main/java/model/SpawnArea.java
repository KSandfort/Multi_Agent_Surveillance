package model;

import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * Area that the entities are placed on at the start of the game.
 */
public class SpawnArea extends Area {

    // Variables
    boolean guardsArea; // True if it is for guards, false if it is for intruders.
    double x1, y1, x2, y2; // Rectangle positions

    /**
     * Constructor
     * @param forGuards
     * @param pos1
     * @param pos2
     * @param pos3
     * @param pos4
     */
    public SpawnArea(boolean forGuards, Vector2D pos1, Vector2D pos2, Vector2D pos3, Vector2D pos4) {
        super(pos1, pos2, pos3, pos4);
        guardsArea = forGuards;
    }

    /**
     * Constructor
     * @param forGuards
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     */
    public SpawnArea(boolean forGuards, double xFrom, double yFrom, double xTo, double yTo) {
        super(xFrom, yFrom, xTo, yTo);
        x1 = xFrom;
        y1 = yFrom;
        x2 = xTo;
        y2 = yTo;
        guardsArea = forGuards;
    }

    @Override
    public ArrayList<Node> getComponents() {
        ArrayList<Node> components = new ArrayList<>();
        int offset = SimulationGUI.CANVAS_OFFSET;
        double sf = SimulationGUI.SCALING_FACTOR;

        // fill
        // get top left and bottom right corner point
        double originX = Double.MAX_VALUE;
        double originY = Double.MAX_VALUE;
        double targetX = Double.MIN_VALUE;
        double targetY = Double.MIN_VALUE;
        for(int i = 0; i < cornerPoints.length; i++) {
            if (cornerPoints[i].getX() < originX)
                originX = cornerPoints[i].getX();
            if (cornerPoints[i].getX() > targetX)
                targetX = cornerPoints[i].getX();
            if (cornerPoints[i].getY() < originY)
                originY = cornerPoints[i].getY();
            if (cornerPoints[i].getY() > targetY)
                targetY = cornerPoints[i].getY();
        }
        Rectangle rectangle = new Rectangle(
                (originX * sf) + offset,
                (originY * sf) + offset,
                Math.abs(targetX - originX) * sf,
                Math.abs(targetY - originY) * sf);
        if (guardsArea) {
            rectangle.setFill(Color.web("#0000FF", 0.4));
        }
        else {
            rectangle.setFill(Color.web("#FF0000", 0.4));
        }
        components.add(rectangle);

        // outline
        for (int i = 0; i < cornerPoints.length; i++) {
            Line line = new Line(
                    (cornerPoints[i].getX() * sf) + offset,
                    (cornerPoints[i].getY() * sf) + offset,
                    (cornerPoints[(i + 1) % 4].getX() * sf) + offset,
                    (cornerPoints[(i + 1) % 4].getY() * sf) + offset);
            if (guardsArea) {
                line.setStroke(Color.web("#0000FF", 0.8));
            }
            else {
                line.setStroke(Color.web("#FF0000", 0.8));
            }
            line.setStrokeWidth(4);
            components.add(line);
        }
        return components;
    }

    @Override
    public boolean isSolidBody() {
        return false;
    }

    @Override
    public boolean isDynamicObject() {
        return false;
    }

}
