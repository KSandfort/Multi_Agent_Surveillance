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

    /**
     * Constructor
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     * @param targetX
     * @param targetY
     * @param directionX
     * @param directionY
     */
    public Teleport(double xFrom, double yFrom, double xTo, double yTo, double targetX, double targetY, double directionX, double directionY) {
        super(xFrom, yFrom, xTo, yTo);
        this.target = new Vector2D(targetX, targetY);
        this.direction = Vector2D.normalize(new Vector2D(directionX, directionY));
    }

    public Vector2D getExitPosition() {
        return target;
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
        rectangle.setFill(Color.web("#FFAC12", 0.4));
        components.add(rectangle);

        // outline
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

        // target line
        Line line = new Line(
                (cornerPoints[0].getX() + cornerPoints[2].getX())/2 * sf + offset,
                (cornerPoints[0].getY() + cornerPoints[1].getY())/2 * sf + offset,
                target.getX() * sf + offset,
                target.getY() * sf + offset);
        line.setStroke(Color.web("#FFAC12", 1));
        line.getStrokeDashArray().addAll(10d);
        components.add(line);

        Rectangle rect = new Rectangle((target.getX() * sf) + offset - 5, (target.getY() * sf) + offset - 5, 10, 10);
        rect.setFill(Color.web("#FFAC12", 1));
        components.add(rect);

        return components;
    }
}
