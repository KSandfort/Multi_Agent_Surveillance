package model;

import Enities.Entity;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * object that the agent can see through, but not walks through
 */
public class Window extends Area{
    public Window(double xFrom, double yFrom, double xTo, double yTo) {
        super(xFrom, yFrom, xTo, yTo);
    }

    public Window(Vector2D pos1, Vector2D pos2, Vector2D pos3, Vector2D pos4) {
        super(pos1, pos2, pos3, pos4);
    }

    @Override
    public void onAgentCollision(Entity agent)
    {
        super.onAgentCollision(agent);
        Vector2D delta = Vector2D.scalar(agent.getDirection(), agent.getVelocity());
        while (isInsideArea(agent.getPosition())){
            agent.setPosition(Vector2D.subtract(agent.getPosition(), delta));
        }
        agent.setDirection(Vector2D.add(agent.getDirection(), Vector2D.randomVector()));
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
            line.setStroke(Color.web("#5a4de3", 0.8));
            line.setStrokeWidth(2);
            components.add(line);
        }
        return components;
    }

    @Override
    public boolean isSolidBody() {
        return true;
    }
}
