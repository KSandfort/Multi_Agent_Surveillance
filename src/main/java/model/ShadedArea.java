package model;

import Enities.Entity;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import utils.DefaultValues;

import java.util.ArrayList;

/**
 * this area decreases the visual range an angle of an entity
 */
public class ShadedArea extends Area{
    private double areaSoundVolume = 2;

    public ShadedArea(double xFrom, double yFrom, double xTo, double yTo) {
        super(xFrom, yFrom, xTo, yTo);
        setAreaFovAngle(getAreaFovAngle() * DefaultValues.areaShadedFovAngleFactor);
        setAreaFovDepth(getAreaFovDepth() * DefaultValues.areaShadedFovDepthFactor);
    }

    public ShadedArea(Vector2D pos1, Vector2D pos2, Vector2D pos3, Vector2D pos4) {
        super(pos1, pos2, pos3, pos4);
        setAreaFovAngle(getAreaFovAngle() * DefaultValues.areaShadedFovAngleFactor);
        setAreaFovDepth(getAreaFovDepth() * DefaultValues.areaShadedFovDepthFactor);
    }

    public ShadedArea(Vector2D[] pos) throws Exception {
        super(pos);
        setAreaFovAngle(getAreaFovAngle() * DefaultValues.areaShadedFovAngleFactor);
        setAreaFovDepth(getAreaFovDepth() * DefaultValues.areaShadedFovDepthFactor);
    }

    public void onAgentCollision(Entity entity) {
        entity.setFovAngle(getAreaFovAngle());
        entity.setFovDepth(getAreaFovDepth());
    }

    @Override
    public ArrayList<Node> getComponents() {
        ArrayList<Node> components = new ArrayList<>();
        int offset = SimulationGUI.CANVAS_OFFSET;
        double sf = SimulationGUI.SCALING_FACTOR;

        double originX = Double.MAX_VALUE;
        double originY = Double.MAX_VALUE;
        double targetX = Double.MIN_VALUE;
        double targetY = Double.MIN_VALUE;

        for (Vector2D cornerPoint : cornerPoints) {
            if (cornerPoint.getX() < originX)
                originX = cornerPoint.getX();
            if (cornerPoint.getX() > targetX)
                targetX = cornerPoint.getX();
            if (cornerPoint.getY() < originY)
                originY = cornerPoint.getY();
            if (cornerPoint.getY() > targetY)
                targetY = cornerPoint.getY();
        }
        Rectangle rectangle = new Rectangle(
                (originX * sf) + offset,
                (originY * sf) + offset,
                Math.abs(targetX - originX) * sf,
                Math.abs(targetY - originY) * sf);

        rectangle.setFill(Color.web("#000000"));
        rectangle.setOpacity(.5);
        components.add(rectangle);

        return components;
    }
}
