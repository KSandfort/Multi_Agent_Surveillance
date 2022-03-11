package model;

import Enities.Entity;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.util.ArrayList;

public class HitBox extends Area {


    public HitBox(Vector2D pos1, Vector2D pos2, Vector2D pos3, Vector2D pos4) {
        super(pos1, pos2, pos3, pos4);
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
            line.setStroke(Color.web("#FFFFFF", 1));
            line.setStrokeWidth(1);
            components.add(line);
        }
        return components;
    }

    public void transform(Entity entity) {
        double radius = entity.radius;
        Vector2D c2 = Vector2D.add(entity.getPosition(), new Vector2D(-radius,-radius));
        Vector2D c1 = Vector2D.add(entity.getPosition(), new Vector2D(radius,-radius));
        Vector2D c3 = Vector2D.add(entity.getPosition(), new Vector2D(-radius,radius));
        Vector2D c4 = Vector2D.add(entity.getPosition(), new Vector2D(radius,radius));
        cornerPoints[0] = c1;
        cornerPoints[1] = c2;
        cornerPoints[2] = c3;
        cornerPoints[3] = c4;
    }
}
