package model;

import Enities.Entity;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

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
        // get bottom right point
        Rectangle rectangle = new Rectangle(
                (originX * sf) + offset,
                (originY * sf) + offset,
                Math.abs(targetX - originX) * sf,
                Math.abs(targetY - originY) * sf);
        rectangle.setFill(Color.web("#331a13"));
        components.add(rectangle);

        // outline
        for (int i = 0; i < cornerPoints.length; i++) {
            components.add(new Line(
                    (cornerPoints[i].getX() * sf) + offset,
                    (cornerPoints[i].getY() * sf) + offset,
                    (cornerPoints[(i + 1) % 4].getX() * sf) + offset,
                    (cornerPoints[(i + 1) % 4].getY() * sf) + offset));
        }
        // Add diagonal lines
        /*
        components.add(new Line(
                (cornerPoints[0].getX() * sf) + offset,
                (cornerPoints[0].getY() * sf) + offset,
                (cornerPoints[2].getX() * sf) + offset,
                (cornerPoints[2].getY() * sf) + offset));
        components.add(new Line(
                (cornerPoints[1].getX() * sf) + offset,
                (cornerPoints[1].getY() * sf) + offset,
                (cornerPoints[3].getX() * sf) + offset,
                (cornerPoints[3].getY() * sf) + offset));


         */
        return components;
    }

    @Override
    public boolean isSolidBody() {
        return true;
    }

    @Override
    public boolean isAgentInsideArea(Entity agent) {
        Vector2D dir = getAgentCollisionDirection(agent);
        return (super.isAgentInsideArea(agent) || dir != null);
    }

    public Vector2D getAgentCollisionDirection(Entity agent){
        Vector2D pos = agent.getPosition();

        Vector2D dir = null;
        for (int i = 0; i < cornerPoints.length; i++) {
            Vector2D c1 = cornerPoints[i];
            Vector2D c2 = cornerPoints[(i + 1)%4];

            //calculate closest point on edge using vector projection
            //following the math on https://en.wikipedia.org/wiki/Vector_projection
            Vector2D a = Vector2D.subtract(pos,c1);
            Vector2D b = Vector2D.subtract(c2, c1);

            double scalar = Vector2D.dotProduct(a,b) / Vector2D.dotProduct(b,b);
            Vector2D a1 = Vector2D.scalar(b,scalar);
            Vector2D a2 = Vector2D.subtract(a,a1);

            if(scalar < 0){a2 = a;}
            else if(scalar > 1){a2 = Vector2D.subtract(pos,c2);}

            Vector2D delta = a2;
            double dist = Vector2D.length(delta);
            if(dist <= agent.getRadius())
            {
                if(dir == null)
                {
                    dir = delta;
                }else
                {
                    double currDist = Vector2D.length(dir);
                    if(dist < currDist)
                    {
                        dir = delta;
                    }
                }
            }
        }
        return dir;
    }

    public boolean isTransparentObject() {
        return false;
    }

    @Override
    public void onAgentCollision(Entity entity)
    {
        Vector2D pos = entity.getPosition();
        Vector2D dir = getAgentCollisionDirection(entity);
        if (dir == null) return;
        if(dir.getX() == 0 && dir.getY() == 0)
        {
            entity.setPosition(entity.getPrevPos());
        }else {
            double length = Vector2D.length(dir);
            double diff = entity.getRadius() - length;
            Vector2D push = Vector2D.scalar(dir, diff / length);
            entity.setPosition(Vector2D.add(pos, push));
        }
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
