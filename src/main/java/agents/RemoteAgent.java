package agents;

import Enities.Entity;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Area;
import model.MapItem;
import model.Vector2D;

import java.util.ArrayList;
import java.util.Random;

/**
 * This guard can be remotely controlled by W, A, S, D.
 */
public class RemoteAgent extends AbstractAgent {

    // Variables
    Vector2D prevPos;

    public RemoteAgent() {
    }

    /**
     * Adds controls to a remote agent
     */
    public void addControls() {
        // Get the remote agent
        this.entityInstance.getMap().getGameController().getSimulationGUI().getMainScene().addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()== KeyCode.W) {
                entityInstance.setVelocity(0.1);
            }
            if(key.getCode()== KeyCode.S) {
                entityInstance.setVelocity(0);
            }
        });
    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        Entity e = entityInstance;
        prevPos = e.getPosition();
        e.setPrevPos(prevPos);
        boolean inSpecialArea = false;
        e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), e.getVelocity())));
        e.getDirection().pivot(0); // Need to add direction
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
