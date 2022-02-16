package model.mapObjects.fixedItems;

import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.Vector2D;
import model.mapObjects.fixedItems.Area;

import java.util.ArrayList;

/**
 * this area decreases the visual range an angle of an entity
 */
public class ShadedArea extends Area {
    public ShadedArea(double xFrom, double yFrom, double xTo, double yTo) {
        super(xFrom, yFrom, xTo, yTo);
        setAreaFovAngle(getAreaFovAngle()*0.5);
        setAreaFovDepth(getAreaFovDepth()*0.5);
    }

    public ShadedArea(Vector2D pos1, Vector2D pos2, Vector2D pos3, Vector2D pos4) {
        super(pos1, pos2, pos3, pos4);
        setAreaFovAngle(getAreaFovAngle()*0.5);
        setAreaFovDepth(getAreaFovDepth()*0.5);
    }

    public ShadedArea(Vector2D[] pos) throws Exception {
        super(pos);
        setAreaFovAngle(getAreaFovAngle()*0.5);
        setAreaFovDepth(getAreaFovDepth()*0.5);
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
            line.setStroke(Color.web("#00FFBB", 0.2));
            line.setStrokeWidth(4);
            components.add(line);
        }
        return components;
    }
}
