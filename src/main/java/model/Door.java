package model;

import Enities.Entity;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import utils.DefaultValues;

import java.util.ArrayList;

/**
 * decreases the agents velocity to simulate the time it takes to go through the door
 * TODO makes a sound when entering
 */
public class Door extends Wall {

    public Door(double xFrom, double yFrom, double xTo, double yTo) {
        super(xFrom, yFrom, xTo, yTo);
        setAreaSpeedFactor(DefaultValues.areaDoorSpeedFactor);
    }

    @Override
    public ArrayList<Node> getComponents() {
        ArrayList<Node> components = new ArrayList<>();
        int offset = SimulationGUI.CANVAS_OFFSET;
        double sf = SimulationGUI.SCALING_FACTOR;

        for (int i = 0; i < cornerPoints.length; i++) {
            Line line = new Line(
                    (cornerPoints[i].getX() * sf) + offset,
                    (cornerPoints[i].getY() * sf) + offset,
                    (cornerPoints[(i + 1) % 4].getX() * sf) + offset,
                    (cornerPoints[(i + 1) % 4].getY() * sf) + offset);
            line.setStroke(Color.web("#b8ad51", 1));
            line.setStrokeWidth(2);
            components.add(line);
        }
        return components;
    }

    @Override
    public void onAgentCollision(Entity entity)
    {
        entity.setFovAngle(getAreaFovAngle());
        entity.setFovDepth(getAreaFovDepth());
        //TODO add hearing
    }

    public boolean isTransparentObject() {
        return true;
    }
}
