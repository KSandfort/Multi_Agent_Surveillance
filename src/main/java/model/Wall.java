package model;

import Enities.BaseEntity;
import Enities.Entity;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import java.util.ArrayList;

/**
 * Wall that can be placed on a map.
 */
public class Wall extends Area {

    /**
     * Constructor
     * @param pos1
     * @param pos2
     * @param pos3
     * @param pos4
     */
    public Wall(Vector2D pos1, Vector2D pos2, Vector2D pos3, Vector2D pos4) {
        super(pos1, pos2, pos3, pos4);
    }

    public Wall(double xFrom, double yFrom, double xTo, double yTo) {
        super(xFrom, yFrom, xTo, yTo);
    }

    /**
     * Returns JavaFX component.
     * @return
     */
    @Override
    public ArrayList<Node> getComponents() {
        ArrayList<Node> components = new ArrayList<>();
        int offset = SimulationGUI.CANVAS_OFFSET;
        double sf = SimulationGUI.SCALING_FACTOR;

        for (int i = 0; i < cornerPoints.length; i++) {
            components.add(new Line(
                    (cornerPoints[i].getX() * sf) + offset,
                    (cornerPoints[i].getY() * sf) + offset,
                    (cornerPoints[(i + 1) % 4].getX() * sf) + offset,
                    (cornerPoints[(i + 1) % 4].getY() * sf) + offset));
        }
        return components;
    }

    @Override
    public boolean isSolidBody() {
        return true;
    }

    @Override
    public boolean isDynamicObject() {
        return false;
    }

    @Override
    public boolean isStaticObject() {
        return true;
    }

    @Override
    public boolean isAgentInsideArea(Entity agent) {
        Vector2D agentPos = agent.getPosition();
        //the position of the closest point inside the wall relative to the agent
        return false;
        //TODO: Implement functionality
    }

    @Override
    public void onAgentCollision(Entity agent)
    {
        super.onAgentCollision(agent);
        System.out.println("Entered area");
    }

    /*
    //this function assumes an agent is inside of the area
    @Override
    public void onAgentCollision(BaseEntity agent)
    {
        //agent is currently inside a wall, so we push the agent out of the wall
        //find the nearest side of the wall assuming the wall is a rectangle
        Vector2D agentPos = agent.getPosition();

        //coordinates of the closest point on the edge of the square to the agent
        double targetX = getPosition().getX();
        double targetY = getPosition().getY();

        if(agentPos.getX() > targetX + (getWidth()/2.0))
        {
            targetX += getWidth();
        }

        if(agentPos.getY() > targetY + (getLength()/2.0))
        {
            targetY += getLength();
        }

        //distance to nearest x/y edge
        double xDiff = Math.abs(targetX - agentPos.getX());
        double yDiff = Math.abs(targetY - agentPos.getY());
        Vector2D newPos;
        if(xDiff < yDiff)
        {
            if(targetX == getPosition().getX())
            {
                targetX -= agent.getRadius();
            }else
            {
                targetX += agent.getRadius();
            }
            newPos = new Vector2D(targetX, agentPos.getY());
        }else
        {
            if(targetY == getPosition().getY())
            {
                targetY -= agent.getRadius();
            }else
            {
                targetY += agent.getRadius();
            }
            newPos = new Vector2D(agentPos.getX(), targetY);
        }

        agent.setPosition(newPos);

    }

     */
}
