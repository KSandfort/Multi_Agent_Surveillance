package model;

import Enities.BaseEntity;
import Enities.Entity;

/**
 * Abstract class for any area placed on a map.
 */
public abstract class Area extends MapItem{

    protected Vector2D[] cornerPoints = new Vector2D[4];
    private double visionDepth = 60;
    private double visionAngle = 30;
    private double speedFactor = 0.01;
    private double hearingFactor = 1;

    public Area(double xFrom, double yFrom, double xTo, double yTo) {
        cornerPoints[0] = new Vector2D(xFrom, yTo); // Bottom left
        cornerPoints[1] = new Vector2D(xFrom, yFrom); // Top Left
        cornerPoints[2] = new Vector2D(xTo, yFrom); // Top Right
        cornerPoints[3] = new Vector2D(xTo, yTo); // Bottom Right
        position = cornerPoints[1];
    }

    public Area(Vector2D pos1, Vector2D pos2, Vector2D pos3, Vector2D pos4) {
        position = pos1;
        cornerPoints[0] = pos1;
        cornerPoints[1] = pos2;
        cornerPoints[2] = pos3;
        cornerPoints[3] = pos4;
    }

    public Area(Vector2D[] pos) throws Exception {
        if (pos.length == 4) {
            cornerPoints = pos;
        }
        else {
            throw new Exception("Invalid number of corner points. 4 Required, " + pos.length + " given.");
        }
    }

    /**
     * Checks whether a point is inside an area.
     * @param pos point
     * @return true, if point is in area
     */
    public boolean isInsideArea(Vector2D pos){
        // Count all the edge crossings.
        int edgeCount = 0;
        for (int i = 0; i < cornerPoints.length; i++) {
            Vector2D c = cornerPoints[i];
            Vector2D d = cornerPoints[(i + 1) % 4];

            if (Vector2D.doTwoSegmentsCross(pos, new Vector2D(pos.getX() + 1E4, pos.getY()), c, d)) {
                edgeCount++;
            }

        }
        if (edgeCount == 1) {
            return true;
        }
        else if (Vector2D.isOnSegment(pos, cornerPoints[0], cornerPoints[1]) ||
                Vector2D.isOnSegment(pos, cornerPoints[1], cornerPoints[2]) ||
                Vector2D.isOnSegment(pos, cornerPoints[2], cornerPoints[3]) ||
                Vector2D.isOnSegment(pos, cornerPoints[3], cornerPoints[0]) ){
            return true;
        } else {
            return false;
        }
    }

    public Vector2D [] getCornerPoints(){
        return cornerPoints;
    }

    public boolean isAgentInsideArea(Entity agent)
    {
        return isInsideArea(agent.getPosition());
    }

    public void onAgentCollision(Entity agent)
    {
        agent.setVelocity(getSpeedFactor());
        agent.setFovAngle(getVisionAngle());
        agent.setFovDepth(getVisionDepth());
        //TODO add hearing
    }

    public double getVisionDepth() {
        return visionDepth;
    }

    public void setVisionDepth(double visionDepth) {
        this.visionDepth = visionDepth;
    }

    public double getVisionAngle() {
        return visionAngle;
    }

    public void setVisionAngle(double visionAngle) {
        this.visionAngle = visionAngle;
    }

    public double getSpeedFactor() {
        return speedFactor;
    }

    public void setSpeedFactor(double speedFactor) {
        this.speedFactor = speedFactor;
    }

    public double getHearingFactor() {
        return hearingFactor;
    }

    public void setHearingFactor(double hearingFactor) {
        this.hearingFactor = hearingFactor;
    }
}
