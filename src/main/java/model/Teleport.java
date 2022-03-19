package model;

import Enities.Entity;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * takes an entity from one location to the next in a single direction to avoid looping
 * target is the area that the agent will be teleported to
 * agent will face in new 'direction'
 */
public class Teleport extends Area{
    private Vector2D target;
    private Vector2D direction;

    public Teleport(double xFrom, double yFrom, double xTo, double yTo, double targetX, double targetY, double directionX, double directionY) {
        super(xFrom, yFrom, xTo, yTo);
        this.target = new Vector2D(targetX, targetY);
        this.direction = Vector2D.normalize(new Vector2D(directionX, directionY));
    }

    public Teleport(Vector2D pos1, Vector2D pos2, Vector2D pos3, Vector2D pos4, Vector2D target, Vector2D direction) {
        super(pos1, pos2, pos3, pos4);
        this.target = target;
        this.direction = direction;
    }

    public Teleport(Vector2D[] pos, Vector2D target, Vector2D direction) throws Exception {
        super(pos);
        this.target = target;
        this.direction = direction;
    }

    /**
     * updates position of the agent and direction he faces
     * @param agent
     */
    @Override
    public void onAgentCollision(Entity agent)
    {
        super.onAgentCollision(agent);
        // new
        agent.setDirection(direction.clone());
        agent.setPosition(target);
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
            line.setStroke(Color.web("#FFAC12", 1));
            line.setStrokeWidth(4);
            components.add(line);
        }
        Rectangle rect = new Rectangle((target.getX() * sf) + offset, (target.getY() * sf) + offset, 10, 10);
        rect.setFill(Color.web("#FFAC12", 1));
        components.add(rect);

        return components;
    }
}
