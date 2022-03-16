package Enities;

import agents.AbstractAgent;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import model.*;

import java.util.ArrayList;
import java.util.Random;

/**
 *   Implementation of the Bug-0 algorithm
 *   -------------------------------------
 *   Basic description of how this little Bug behaves:
 *
 *   The Bug always has a specific goal position that it tries to reach, and it is obsessed with reaching this goal.
 *  However, it's very possible that it'll encounter an obstruction as it tries to reach the wall...
 *
 *   If the Bug sees a wall (area object) in its FOV, it loses its focus on reaching the goal, and instead focuses on
 *  crawling along the wall.
 *
 */

public class Bug extends Entity {

    // Variables
    AbstractAgent agent;

    static int bugCount = 0;

    // Coordinates of the bug's goal position
    double goalX = 0;
    double goalY = 0;

    // Angle from the bug to the goal position
    double goalDirection = 0;

    // Initial angles for the bug's goal position (basically the main orientation used when the goal is offset)
    double initialGoalDirection = 0;

    // Range [0,1] of how much of the FOV is obstructed by a wall, usually 0.30-0.50 when crawling around a wall
    double wallCloseness = 0;

    // Controls which direction the bug should turn when it encounters a wall.
    // 1 = clockwise rotation, -1 = anticlockwise rotation
    int wallRotationPreference = 1;

    double originX = 0;
    double originY = 0;

    String state = "goal";

    // ------------------------
    //      BUG PARAMETERS
    // ------------------------

    // Weights of the direction pivot conditions
    // goalPivotWeight refers to how much the bug tries to navigate to the goal position
    // wallPivotWeight refers to how much the bug tries to get away from walls
    double goalPivotWeight = 0.4;
    double wallPivotWeight = 1.05;

    // If the bug's wallCloseness percentage goes above this threshold, it'll enter wall crawl mode.
    double wallClosenessConditionPercentage = 0.4;

    // Controls how unlikely it is that the bug's wall rotation preference swaps when not crawling along a wall
    // Bigger values = bug is less likely to swap its preference
    int wallRotationSwapChance = 100;

    /**
     * Constructor
     * @param x
     * @param y
     */
    public Bug(double x, double y, GameMap map, double goal_direction) {
        super(x, y, map);

        this.ID = Bug.bugCount;
        Bug.bugCount++;

        originX = x;
        originY = y;

        int goalDistance = 5;

        goalX = originX + goalDistance * Math.cos(goal_direction);
        goalY = originY + goalDistance * Math.sin(goal_direction);

        goalDirection = goal_direction;
        initialGoalDirection = goalDirection;

        setDirection(new Vector2D(Math.cos(goalDirection), Math.sin(goalDirection)));
        goalDirection = Math.atan2(getDirection().getY(),getDirection().getX());

        Random rand=new Random();
        int X = rand.nextInt(2);
        if(X == 0)
            wallRotationPreference = 1;
        else
            wallRotationPreference = -1;
    }

    boolean crawlingAlongWall = false;
    boolean swappedPreference = false;

    // Help this poor fella if it gets stuck somewhere;
    double stuckTracker = 0.0;
    int stuckTimer = 0;
    boolean tryingToUnstuck = false;

    @Override
    public void update(ArrayList<MapItem> items) {
        prevPos = getPosition();

        if (!tryingToUnstuck)
            goalDirection = Math.atan2(goalY - getPosition().getY(), goalX - getPosition().getX());

        double goalOffset = 2;

        if (Math.abs(getPosition().getX() - goalX) < 2) {
            goalX += (goalOffset * Math.cos(initialGoalDirection));
            // goalX = -1 * (goalX - originX) + originX;
        }

        if (Math.abs(getPosition().getY() - goalY) < 2) {
            goalY += (goalOffset * Math.sin(initialGoalDirection));
            // goalY = -1 * (goalY - originY) + originY;

        }

        ArrayList<Ray> vision = FOV();

        // get the middle FOV ray to see what's right ahead of the entity
        double averageDistance = 0;
        Vector2D longestRay = vision.get(0).getDirection();

        for (Ray ray : vision) {
            Vector2D rayVector = ray.getDirection();
            averageDistance += Vector2D.length(rayVector);
            if (Vector2D.length(rayVector) > Vector2D.length(longestRay))
                longestRay = rayVector;
        }
        double preferredTheta = Math.atan2(longestRay.getY(),longestRay.getX());
        averageDistance /= vision.size();
        double distanceToWall = averageDistance;

        wallCloseness = (1 - distanceToWall/getFovDepth());

        crawlingAlongWall = (wallCloseness > wallClosenessConditionPercentage);
        if (crawlingAlongWall)
            state = "wall";
        else
            state = "goal";

        double theta = Math.atan2(getDirection().getY(),getDirection().getX());
        double angleDifference = goalDirection - theta;
        angleDifference = (angleDifference + Math.PI) % (2*Math.PI) - Math.PI;
        double pivotToGoal =  goalPivotWeight * (1-wallCloseness) * angleDifference;

        double pivotFromWall = wallPivotWeight * wallCloseness * wallRotationPreference;

        if (!tryingToUnstuck)
            direction.pivot(pivotToGoal + pivotFromWall);
        else
            direction.pivot(pivotToGoal);

        prevPos = getPosition();

        boolean inSpecialArea = false;

        this.setPosition(Vector2D.add(getPosition(), Vector2D.scalar(direction, baseSpeedGuard))); // TODO: Change speed
        direction.normalize();
        hitBox.transform(this);

        for(MapItem item : items) {
            if (((Area) item).isAgentInsideArea(this)){
                Area areaItem = (Area) item;
                areaItem.onAgentCollision(this);
                inSpecialArea = true;
            }
        }

        // randomly switch wall rotation preference when not already crawling along a wall
        if (!crawlingAlongWall) {
            Random rand = new Random();
            int X = rand.nextInt(wallRotationSwapChance);
            if (X == 1) {
                wallRotationPreference *= -1;
            }
        }

        if(!inSpecialArea){
            this.resetEntityParam();
        }

        if (!tryingToUnstuck) {
            stuckTracker = Vector2D.distance(getPosition(), getPrevPos());
            if (stuckTracker < 0.05) {
                stuckTimer++;

                if (stuckTimer > 300) {
                    tryingToUnstuck = true;
                }
            } else {
                stuckTimer = 0;
            }
        } else {
            stuckTimer--;
            if (stuckTimer < 0) {
                goalDirection = theta + Math.PI;
                tryingToUnstuck = false;
            }
        }

    }

    @Override
    public boolean isIntruder() {
        return false;
    }

    @Override
    public ArrayList<Node> getComponents() {
        double sf = SimulationGUI.SCALING_FACTOR;
        int offset = SimulationGUI.CANVAS_OFFSET;
        ArrayList<Node> components = new ArrayList<>();
        Circle circle = new Circle();
        circle.setFill(Color.web("#0000FF", 1));
        circle.setCenterX((getPosition().getX() * sf) + offset);
        circle.setCenterY((getPosition().getY() * sf) + offset);
        circle.setRadius(1 * sf);

        Line goalLine = new Line(
                getPosition().getX() * sf + offset,
                getPosition().getY() * sf + offset,
                goalX * sf + offset,
                goalY * sf + offset
        );
        goalLine.setStroke(Color.web("#ff5733", 1));
        goalLine.setStrokeWidth(2);

        components.add(goalLine);

        Line line = new Line(
                (getPosition().getX() * sf) + offset,
                (getPosition().getY() * sf) + offset,
                (getPosition().getX() * sf) + offset + 10,
                (getPosition().getY() * sf) + offset );
        line.setStroke(Color.web("#C8E1E7", 1));
        line.setStrokeWidth(5);
        Text text= new Text("Bug " + ID);
        text.setX((getPosition().getX() * sf) + offset -20);
        text.setY((getPosition().getY() * sf) + offset -12);

        Text debugtext = new Text(Math.round(wallCloseness * 1000)/1000.0 + "%" + " - " + state + " - " + stuckTimer );
        debugtext.setX((getPosition().getX() * sf) + offset -20);
        debugtext.setY((getPosition().getY() * sf) + offset + 20);


        components.add(circle);
        components.add(text);
        components.add(debugtext);

        ArrayList<Ray> rays = this.FOV();
        for (Ray ray : rays){
            components.add(ray.getComponent());
        }
        components.addAll(hitBox.getComponents());
        return components;
    }
}
