package agents;

import Enities.Entity;
import model.Area;
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

    @Override
    public void addControls() {
        // Does nothing because no controls are involved.
    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        Entity e = entityInstance;
        prevPos = e.getPosition();
        e.setPrevPos(prevPos);
        boolean inSpecialArea = false;
        e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), e.getVelocity())));
        e.getDirection().pivot((new Random().nextDouble()*180 - 90)*explorationFactor);
        e.getDirection().normalize();
        e.getHitBox().transform(e);
        for(MapItem item : items) {
            if (((Area) item).isAgentInsideArea(e)){
                Area areaItem = (Area) item;
                areaItem.onAgentCollision(e);
                inSpecialArea = true;
            }
        }
        if(!inSpecialArea){
            e.resetEntityParam();
        }
    }
}
