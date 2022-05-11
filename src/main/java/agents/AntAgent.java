package agents;

import Enities.*;
import model.Area;
import model.MapItem;
import model.Vector2D;
import model.Wall;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

public class AntAgent extends AbstractAgent{
    Vector2D pos;
    Vector2D prevPos;
    Vector2D dir;
    double velocity;
    // TODO, need to choose a good value for q0
    double q0 = 0.4; //proportion of occasions when the greedy selection technique is used
    public double pherEvapRateLocal = 0.2; // rate at which pheromones evaporate, rho in formula
    public double pherEvapRateGlobal = 0.2; // rate at which pheromones evaporate, rho in formula
    double explorationFactor = 0.1;
    double timeStuck = 0;

    @Override
    public void addControls() {
        // Does nothing because no controls are involved.
    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        Entity e = entityInstance;
        Vector2D prevPos = e.getDirection();

        setAgentParameters(e);
        double q = Math.random();

        double[][] markers = entityInstance.getMarkerSensing();

        double angle = getNewDirection(markers);
        e.getDirection().pivot(angle);
        if (e.getMap().getGameController().getSimulationGUI().getCurrentStep() % 5 == 0) {
            dropPheromone();
        }

        e.getDirection().normalize();
        e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity)));
    }

    private double getNewDirection(double[][] markers) {
        double maxPheromone = 0;
        double angle = 0;

        if(markers != null) {
            for (int i = 0; i < markers.length; i++) {
                if (markers[i][2] > maxPheromone) {
                    angle = markers[i][1];
                    maxPheromone = markers[i][2];
                }
            }
        }
        if (isStuck()){

            double dir = Vector2D.shortestAngle(entityInstance.getDirection(), entityInstance.getListeningDirection(entityInstance.getMap().getMovingItems(), entityInstance.getMap().getStaticItems()));
            double maxAngle;
            double minAngle;
            if (timeStuck > 20){
                maxAngle = entityInstance.getFovAngle()*2 * explorationFactor;
                minAngle = dir;
            }
            else if (dir>0){
                // agent moves to the left
                maxAngle = -30;
                minAngle = entityInstance.getFovAngle()*2 * -explorationFactor;
            }else{
                maxAngle = entityInstance.getFovAngle()*2 * explorationFactor;
                minAngle = 30;
            }
            angle = (Math.random() * (maxAngle-minAngle)) + minAngle;
        } else if ((angle == 0 && maxPheromone == 0)) {
            double maxAngle = entityInstance.getFovAngle() * explorationFactor;
            double minAngle = entityInstance.getFovAngle() * -explorationFactor;
            angle = (Math.random() * (maxAngle - minAngle)) + minAngle;
        }

        //double newX = Math.cos(angle * entityInstance.getDirection().getX()) - Math.sin(angle * entityInstance.getDirection().getY());
        //double newY = Math.sin(angle * entityInstance.getDirection().getX()) + Math.cos(angle * entityInstance.getDirection().getY());

        return angle; //new Vector2D(newX, newY);
    }


    /**
     * dropping markers after finding new direction
     */
    private void dropPheromone() {
        //use formula 3
        entityInstance.placeMarker(1);
        // TODO implement evaporation
        //pher(i, j) = (1 - pherEvapRateLocal) * pher(i, j)
    }


    private void getDestination(double q) {
        // instead of this we try marker array
        if (q <= q0){

        }else{

        }
        //use formula 1 and 2
    }

    private boolean isStuck(){
        boolean stuckAtObject = false;
        for (MapItem item: entityInstance.getMap().getSolidBodies()){
            if (item instanceof Area){
                if(((Area) item).isAgentInsideArea(entityInstance)){
                    stuckAtObject = true;
                }
            }
        }
        for (MapItem item: entityInstance.getMap().getMovingItems()){
            if(item instanceof Entity) {
                if (((Entity) item).getID() != entityInstance.getID()) {
                    Vector2D[] corners = ((Entity) item).getHitBox().getCornerPoints();
                    Area tempArea = new Wall(corners[1].getX(), corners[1].getY(), corners[3].getX(), corners[3].getY());
                    if (tempArea.isAgentInsideArea(entityInstance)) {
                        stuckAtObject = true;
                    }
                }
            }
        }

        if (stuckAtObject == true){
            timeStuck++;
        }else {
            timeStuck = 0;
        }
        return timeStuck>3;
    }

    private void setAgentParameters(Entity e) {
        e.setPrevPos(e.getPosition());
        pos = e.getPosition();
        prevPos = e.getPrevPos();
        dir = e.getDirection();
        velocity = Entity.baseSpeedGuard;
    }




}
