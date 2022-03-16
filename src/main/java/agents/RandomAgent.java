package agents;

import Enities.Entity;
import model.MapItem;
import model.Vector2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * A guard that moves around randomly.
 */
public class RandomAgent extends AbstractAgent {

    // Variables
    Vector2D prevPos;
    double explorationFactor = 0.2;

    /**
     * Constructor
     */
    public RandomAgent() {
    }

    @Override
    public void addControls() {
        // Does nothing because no controls are involved.
    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        Entity e = entityInstance;
        prevPos = e.getPosition();
        e.setPrevPos(prevPos);
        double velocity = 0;
        if (e.isIntruder()) {
            velocity = Entity.baseSpeedIntruder;
        }
        else {
            velocity = Entity.baseSpeedGuard;
        }
        e.getDirection().pivot((new Random().nextDouble()*180 - 90)*explorationFactor);
        e.getDirection().normalize();
        e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity)));
    }
}
