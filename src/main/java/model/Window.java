package model;

import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * object that the agent can see through, but not walks through
 */
public class Window extends Wall{

    /**
     * Constructor
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     */
    public Window(double xFrom, double yFrom, double xTo, double yTo) {
        super(xFrom, yFrom, xTo, yTo);
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
        rectangle.setFill(Color.web("#5a4de3", 0.2));
        components.add(rectangle);

        // outline
        for (int i = 0; i < cornerPoints.length; i++) {
            Line line = new Line(
                    (cornerPoints[i].getX() * sf) + offset,
                    (cornerPoints[i].getY() * sf) + offset,
                    (cornerPoints[(i + 1) % 4].getX() * sf) + offset,
                    (cornerPoints[(i + 1) % 4].getY() * sf) + offset);
            line.setStroke(Color.web("#5a4de3"));
            line.setStrokeWidth(2);
            components.add(line);
        }
        return components;
    }

    public boolean isTransparentObject() {
        return true;
    }
}
