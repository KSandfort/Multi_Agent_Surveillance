package model;

import Enities.Entity;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import utils.DefaultValues;

import java.util.ArrayList;

/**
 * this area increases the visual range an angle of an entity
 * decreases the velocity of the agents to simulate climbing the tower
 */
public class SentryTower extends Area {

    /**
     * Constructor
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     */
    public SentryTower(double xFrom, double yFrom, double xTo, double yTo) {
        super(xFrom, yFrom, xTo, yTo);
        setAreaFovAngle(getAreaFovAngle()* DefaultValues.areaSentryTowerFovAngleFactor);
        setAreaFovDepth(getAreaFovDepth()* DefaultValues.areaSentryTowerFovDepthFactor);
        setAreaSpeedFactor(DefaultValues.areaSentryTowerSpeedFactor);
    }

    public void onAgentCollision(Entity entity) {
        entity.setFovDepth(getAreaFovDepth());
        entity.setFovAngle(getAreaFovAngle());
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
            line.setStroke(Color.web("#5C5F61", 1));
            line.setStrokeWidth(4);
            components.add(line);
        }
        return components;
    }

    @Override
    public boolean isSolidBody() {
        return true;
    }

    @Override
    public boolean isTransparentObject() {
        return false;
    }
}
