package agents;

import Enities.Entity;
import model.Area;
import model.MapItem;
import model.Vector2D;
import model.Wall;

import java.util.ArrayList;
import java.util.Random;


public class AntAgent extends AbstractAgent{
    Vector2D pos;
    Vector2D prevPos;
    Vector2D dir;
    double velocity;
    double explorationFactor = 0.1;
    double timeStuck = 0;

    @Override
    public void addControls() {
        // Does nothing because no controls are involved.
    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        Entity e = entityInstance;
        setAgentParameters(e);

        double angle = getNewDirection();

        e.getDirection().pivot(angle);
        if (e.getMap().getGameController().getCurrentStep() % 10 == 0) {
            e.placeMarker(0);
        }

        e.getDirection().normalize();
        e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity)));
    }

    private double getNewDirection() {
        double angle = getDirectionWithRandomness();

        if (isStuck()){
            double dirSound = Vector2D.shortestAngle(entityInstance.getDirection(), entityInstance.getListeningDirection(entityInstance.getMap().getMovingItems(), entityInstance.getMap().getStaticItems()));
            while(Math.abs(dirSound) > entityInstance.getFovAngle()){
                dirSound = dirSound/2;
            }
            double maxAngle;
            double minAngle;
            // if agent stays stuck, move towards direction without sound
            if (timeStuck > 20){
                maxAngle = entityInstance.getFovAngle()*2 * explorationFactor;
                minAngle = dirSound;
            }
            // directing agent away from the wall
            else if (dirSound<0){
                // agent moves to the left
                maxAngle = 30;
                minAngle = entityInstance.getFovAngle()*2 * -explorationFactor;

            }else{
                maxAngle = entityInstance.getFovAngle()*2 * explorationFactor;
                minAngle = -30;
            }
            angle = (Math.random() * (maxAngle-minAngle)) + minAngle;

        } else if ((angle == 0 && getMaxPheromone(entityInstance.getMarkerSensing()) == 0)) {
            double maxAngle = entityInstance.getFovAngle() * explorationFactor;
            double minAngle = entityInstance.getFovAngle() * -explorationFactor;
            angle = (Math.random() * (maxAngle - minAngle)) + minAngle;
        }
        return angle;
    }


    /**
     * checks if agent is currently stuck at an object or another agent
     * @return
     */
    private boolean isStuck(){
        if (stuckAtWall() || stuckAtAgent()){
            timeStuck++;
        }else {
            timeStuck = 0;
        }
        return timeStuck>3;
    }

    private boolean stuckAtWall(){
        boolean stuckAtWall = false;
        // checks for being stuck at walls
        for (MapItem item: entityInstance.getMap().getSolidBodies()){
            if (item instanceof Area){
                if(((Area) item).isAgentInsideArea(entityInstance)){
                    stuckAtWall = true;
                }
            }
        }
        return stuckAtWall;
    }

    private boolean stuckAtAgent(){
        boolean stuckAtAgent = false;

        // checks for being stuck with other agents
        for (MapItem item: entityInstance.getMap().getMovingItems()){
            if(item instanceof Entity) {
                if (((Entity) item).getID() != entityInstance.getID()) {
                    Vector2D[] corners = ((Entity) item).getHitBox().getCornerPoints();
                    Area tempArea = new Wall(corners[1].getX(), corners[1].getY(), corners[3].getX(), corners[3].getY());
                    if (tempArea.isAgentInsideArea(entityInstance)) {
                        stuckAtAgent = true;
                    }
                }
            }
        }
        return stuckAtAgent;
    }

    private double getDirectionWithRandomness(){
        double angle;
        // Define random angle
        double randomDir = (new Random().nextDouble()*180 - 90)*explorationFactor;
        // Get avg pheromone angle
        double pheromoneDir = getPheromoneDirection(entityInstance.getMarkerSensing());

        // Combine angles in a weighted sum
        double randomness = 0.4;
        if (pheromoneDir != 0) {
            angle = (randomDir * randomness) + (pheromoneDir * (1 - randomness));
        }
        else {
            angle = randomDir;
        }
        return angle;
    }

    private double getPheromoneDirection(double[][] markers){
        double angle = 0;
        double maxPheromone = 0;
        if(markers != null) {
            for (int i = 0; i < markers.length; i++) {
                if (markers[i][2] > maxPheromone) {
                    angle = markers[i][1];
                    maxPheromone = markers[i][2];
                }
            }
        }
        return angle;
    }

    private double getMaxPheromone(double[][] markers){
        double maxPheromone = 0;
        if(markers != null) {
            for (int i = 0; i < markers.length; i++) {
                if (markers[i][2] > maxPheromone) {
                    maxPheromone = markers[i][2];
                }
            }
        }
        return maxPheromone;
    }

    private void setAgentParameters(Entity e) {
        e.setPrevPos(e.getPosition());
        pos = e.getPosition();
        prevPos = e.getPrevPos();
        dir = e.getDirection();
        velocity = Entity.baseSpeedGuard;
    }
}