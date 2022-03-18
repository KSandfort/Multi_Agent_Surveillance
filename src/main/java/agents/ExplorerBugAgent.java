package agents;

import Enities.Entity;
import Enities.Ray;
import model.MapItem;
import model.Vector2D;

import java.util.ArrayList;
import java.util.Random;

public class ExplorerBugAgent extends AbstractAgent {
    // Coordinates of the bug's goal position
    double goalX = 1000;
    double goalY = 1000;

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

    boolean crawlingAlongWall = false;
    boolean swappedPreference = false;

    // Help this poor fella if it gets stuck somewhere;
    double stuckTracker = 0.0;
    int stuckTimer = 0;
    boolean tryingToUnstuck = false;

    @Override
    public void addControls() {

    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        Entity e = entityInstance;

        Vector2D pos = e.getPosition();
        Vector2D prevPos = e.getPrevPos();
        Vector2D dir = e.getDirection();

        if (!tryingToUnstuck)
            goalDirection = Math.atan2(goalY - e.getPosition().getY(), goalX - e.getPosition().getX());

        double goalOffset = 2;

        if (Math.abs(pos.getX() - goalX) < 2) {
            goalX += (goalOffset * Math.cos(initialGoalDirection));
            // goalX = -1 * (goalX - originX) + originX;
        }

        if (Math.abs(pos.getY() - goalY) < 2) {
            goalY += (goalOffset * Math.sin(initialGoalDirection));
            // goalY = -1 * (goalY - originY) + originY;

        }

        ArrayList<Ray> vision = e.FOV();

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

        wallCloseness = (1 - distanceToWall/e.getFovDepth());

        crawlingAlongWall = (wallCloseness > wallClosenessConditionPercentage);
        if (crawlingAlongWall)
            state = "wall";
        else
            state = "goal";

        double theta = Math.atan2(dir.getY(),dir.getX());
        double angleDifference = goalDirection - theta;
        angleDifference = (angleDifference + Math.PI) % (2*Math.PI) - Math.PI;
        double pivotToGoal =  goalPivotWeight * (1-wallCloseness) * angleDifference;

        double pivotFromWall = wallPivotWeight * wallCloseness * wallRotationPreference;

        if (!tryingToUnstuck)
            dir.pivot(pivotToGoal + pivotFromWall);
        else
            dir.pivot(pivotToGoal);



        // randomly switch wall rotation preference when not already crawling along a wall
        if (!crawlingAlongWall) {
            Random rand = new Random();
            int X = rand.nextInt(wallRotationSwapChance);
            if (X == 1) {
                wallRotationPreference *= -1;
            }
        }

        prevPos = e.getPosition();

        // Move
        double velocity = 0.2;
        e.getDirection().normalize();
        e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity)));

        pos = e.getPosition();

        if (!tryingToUnstuck) {
            stuckTracker = Vector2D.distance(pos, prevPos);
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
}
