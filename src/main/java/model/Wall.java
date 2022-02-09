package model;

import Enities.BaseEntity;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class Wall extends Area {
    public Wall(int length, int width) {
        super(length, width);
    }

    public Wall(int posX, int posY, int posXend, int posYend) {
        super(posX, posY, posXend, posYend);
    }

    //returns javafx component
    @Override
    public Node getComponent() {
        Rectangle rect = new Rectangle();
        rect.setX(getPosition().getX());
        rect.setY(getPosition().getY());
        rect.setWidth(getWidth());
        rect.setHeight(getLength());
        return rect;
    }

    @Override
    public boolean isAgentInsideArea(BaseEntity agent) {
        Vector2D agentPos = agent.getPosition();
        //the position of the closest point inside the wall relative to the agent
        double xComp = agentPos.getX();
        double yComp = agentPos.getY();

        //get x-coordinate of the closest left/right edge
        if(getPosition().getX() <= agentPos.getX())
            xComp = getPosition().getX();
        else
            xComp = getPosition().getX() + getWidth();

        //get the y-coordinate of the closest top/bottom edge
        if(getPosition().getY() <= agentPos.getY())
            yComp = getPosition().getY();
        else
            yComp = getPosition().getY() + getLength();

        //delta position
        double deltaX = xComp - agentPos.getX();
        double deltaY = yComp - agentPos.getY();

        //calculate square of the distance to the wall
        double dist = deltaX*deltaX + deltaY*deltaY;
        //calculate the square of the size of the agent
        double size = agent.getRadius() * agent.getRadius();

        return dist < size;
    }

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
}
