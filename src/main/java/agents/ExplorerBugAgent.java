package agents;

import Enities.Entity;
import Enities.Intruder;
import Enities.Ray;
import model.MapItem;
import model.Vector2D;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class ExplorerBugAgent extends AbstractAgent {

    // Goal direction from bug's current position
    double goalDirection = 0;

    // Range [0,1] of how much of the FOV is obstructed by a wall, usually 0.30-0.50 when crawling around a wall
    double wallCloseness = 0;

    // Controls which direction the bug should turn when it encounters a wall.
    // -1 = rotate left, 1 = rotate right
    int wallRotationPreference = 1;

    // wall crawl state
    boolean crawlingAlongWall = false;

    // Help this poor fella if it gets stuck somewhere;
    double stuckTracker = 0.0;
    int stuckTimer = 0;
    boolean tryingToUnstuck = false;
    double getGoalDirectionAfterUnstuck = 0;

    int initializationStep = 0;

    // all bugs share this
    static int guardCount = 0;

    // ------------------------
    //      BUG PARAMETERS
    // ------------------------

    // Weights of the direction pivot conditions
    // goalPivotWeight refers to how much the bug tries to navigate to the goal position
    // wallPivotWeight refers to how much the bug tries to get away from walls
    double goalPivotWeight = 1.0;
    double wallPivotWeight = 3.0;

    // If the bug's wallCloseness percentage goes above this threshold, it'll enter wall crawl mode.
    double wallClosenessConditionPercentage = 0.3;

    @Override
    public void addControls() {
        // The bug has a free will of its own, no puppetmaster needed here
    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        Entity e = entityInstance;

        Vector2D pos = e.getPosition();
        Vector2D prevPos = e.getPrevPos();
        Vector2D dir = e.getDirection();

        // Initialize bug orientations (using a hacky way to get the total number of bugs)
        // Each bug should start with a unique orientation, higher chance of exploring the whole map faster

        // Set the class's guardCount variable to the highest bug's ID (since they're updated one after the other)
        if (initializationStep == 0) {
            guardCount = e.getID();
            initializationStep = 1;
            return;

        // Set the initial goalDirection depending on what the bug count was
        } else if (initializationStep == 1) {
            goalDirection = (double)e.getID()/(double)guardCount * 2.0 * Math.PI;
            initializationStep = 2;
        }

        ArrayList<Ray> vision = e.getFov();

        // Average distance from the closest wall
        double averageDistance = 0;
        // Average distance to the left of the bug, average distance to the right of the bug
        double leftSideAverage = 0;
        double rightSideAverage = 0;

        double i = 0;
        for (Ray ray : vision) {
            Vector2D rayVector = ray.getDirection();
            averageDistance += Vector2D.length(rayVector);

            // Not sure if this is needed...? It gives the corner-vision rays more weight than middle rays
            double sideScaler = Math.abs((i/(vision.size()/2.0)) - 1);

            if (i < vision.size()/2.0)
                leftSideAverage += Vector2D.length(rayVector) * sideScaler;
            else if (i > vision.size()/2.0)
                rightSideAverage += Vector2D.length(rayVector) * sideScaler;

            i += 1;
        }

        // Choose which direction the bug should crawl around a wall, depending on which side has the most freedom
        // If the bug is trying to get out of a corner, keep current preference (prevent infinite of switching preference)
        if (!tryingToUnstuck) {
            if (leftSideAverage > rightSideAverage) {
                wallRotationPreference = -1;
            } else if (leftSideAverage < rightSideAverage) {
                wallRotationPreference = 1;
            }
        }

        // Calculate the total closeness to a wall
        double distanceToWall = averageDistance / vision.size();
        wallCloseness = (1 - distanceToWall/e.getFovDepth());

        // Square the wall closeness (the closer the bug is to the wall, the harder the bug steers away from it)
        wallCloseness = Math.pow(wallCloseness, 2);

        // Set the bug's state
        crawlingAlongWall = (wallCloseness > wallClosenessConditionPercentage);

        // Calculate general goal direction for intruder
        if (entityInstance instanceof Intruder) {
            Vector2D targetDir = ((Intruder) entityInstance).calculateTargetDirection();
            if (targetDir != null)
                goalDirection = Math.atan2(targetDir.getY(),targetDir.getX());
        }


        double theta = Math.atan2(dir.getY(),dir.getX());
        double angleDifference = goalDirection - theta;
        angleDifference = (angleDifference + Math.PI) % (2*Math.PI) - Math.PI;

        double pivotToGoal =  goalPivotWeight * (1-wallCloseness) * angleDifference;
        double pivotFromWall = wallPivotWeight * wallCloseness * wallRotationPreference;

          if (crawlingAlongWall)
              dir.pivot(pivotFromWall);
          else
              dir.pivot(pivotToGoal);

        /*
         Everything to do with the bug getting stuck somewhere
         stuckTimer calculates how long the distance between the bug's current & previous position has been < 0.05.
         If the bug has been in the same region for a certain amount of time, the goal direction gets reset.
         However, to make sure the bug actually gets out of the corner, first the goal direction is flipped.
         Only after another short interval of time, the bug sets its new goal direction.
         */
        if (!tryingToUnstuck && prevPos != null) {
            stuckTracker = Vector2D.distance(pos, prevPos);
            if (stuckTracker < 0.05) {
                stuckTimer++;
                if (stuckTimer > 50) {
                    //goalDirection = theta + Math.PI;
                    getGoalDirectionAfterUnstuck = newGoalDirection(goalDirection);
                    goalDirection += Math.PI;
                    tryingToUnstuck = true;
                }
            } else {
                stuckTimer = 0;
            }
        } else if (tryingToUnstuck) {
            stuckTimer--;
            if (stuckTimer == 0) {
                goalDirection = getGoalDirectionAfterUnstuck;
                tryingToUnstuck = false;
            }
        }

        // Move
        double velocity = Entity.baseSpeedGuard;
        e.getDirection().normalize();
        e.setPrevPos(e.getPosition());
        e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity)));
    }

    /**
     * Flips the agent's goal when stuck (rotate 180 degrees and randomly add +-90 degrees).
     * @param goalDir
     * @return
     */
    private double newGoalDirection(double goalDir) {
        Random random = new Random();
        return goalDir + Math.PI + (Math.PI) * (random.nextDouble() - .5);
    }
}
