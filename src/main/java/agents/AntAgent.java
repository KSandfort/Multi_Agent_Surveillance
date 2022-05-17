package agents;

import Enities.Entity;
import Enities.Marker;
import Enities.Ray;
import model.MapItem;
import model.Vector2D;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

/**
 * // extent to shich ants prefer nearby points
 // too high and algorithm will be greedy search
 // too low, will probably stagnate
 public double dstPower = 4;
 // how likely each ant is to follow a certain path as previous ants
 // to high and ants will keep walking on the same path
 // too low and it will search too many paths
 public double pheromonePower = 1;

 public double pheromoneIntensity = 10; // intensity of trail

 public double initPherIntensity = 1; //init pher strength along all paths
 public double pherEvapRate = 0.2; // rate at which phoromones evaporate*/

public class AntAgent extends AbstractAgent{
    Vector2D pos;
    Vector2D prevPos;
    Vector2D dir;
    double explorationFactor = 0.2;
    double velocity;
    // TODO, need to choose a good value for q0
    double q0 = 0.4; //proportion of occasions when the greedy selection technique is used
    public double pherEvapRateLocal = 0.2; // rate at which pheromones evaporate, rho in formula
    public double pherEvapRateGlobal = 0.2; // rate at which pheromones evaporate, rho in formula


    @Override
    public void addControls() {
        // Does nothing because no controls are involved.
    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        Entity e = entityInstance;

        setAgentParameters(e);
        double q = Math.random();

        //getDestination(q); // TODO
        double[][] markers = entityInstance.getMarkerSensing();
        //e.setDirection(getNewDirection(markers));

        double angle;
        angle = 0;

        // Define random angle
        double randomDir = (new Random().nextDouble()*180 - 90)*explorationFactor;
        // Get avg pheromone angle
        double pheromoneDir;
        if (e.getMarkerSensing() == null) {
            pheromoneDir = 0;
        }
        else {
            pheromoneDir = e.getMarkerSensing()[0][1];
        }
        // Combine angles in a weighted sum
        double randomness = 0.4;
        if (pheromoneDir != 0) {
            angle = (randomDir * randomness) + (pheromoneDir * (1 - randomness));
        }
        else {
            angle = randomDir;
        }

        e.getDirection().pivot(angle);
        if (e.getMap().getGameController().getSimulationGUI().getCurrentStep() % 20 == 0) {
            dropPheromone();
        }

        e.getDirection().normalize();
        e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity)));

    }

    private Vector2D getNewDirection(double[][] markers) {
        double maxPheromone = 0;
        double angle = 1;

        if(markers != null) {
            for (int i = 0; i < markers.length; i++) {
                if (markers[i][0] > maxPheromone) {
                    angle = markers[i][1];
                }
            }
        }
        double newX = Math.cos(angle * entityInstance.getDirection().getX()) - Math.sin(angle * entityInstance.getDirection().getY());
        double newY = Math.sin(angle * entityInstance.getDirection().getX()) + Math.cos(angle * entityInstance.getDirection().getY());
        return new Vector2D(newX, newY);
    }


    /**
     * dropping markers after finding new direction
     */
    private void dropPheromone() {
        //use formula 3
        entityInstance.placeMarker(0);
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

    private void setAgentParameters(Entity e) {
        e.setPrevPos(e.getPosition());
        pos = e.getPosition();
        prevPos = e.getPrevPos();
        dir = e.getDirection();
        velocity = Entity.baseSpeedGuard;
    }


}
