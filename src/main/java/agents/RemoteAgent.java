package agents;

import Enities.Entity;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.MapItem;
import model.Vector2D;
import java.util.ArrayList;


/**
 * This guard can be remotely controlled by W, A, S, D.
 */
public class RemoteAgent extends AbstractAgent {

    // Variables
    Vector2D prevPos;
    boolean moving = false;

    public RemoteAgent() {
    }

    /**
     * Adds controls to a remote agent
     */
    public void addControls() {
        // Get the remote agent
        this.entityInstance.getMap().getGameController().getSimulationGUI().getMainScene().addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()== KeyCode.W) {
                moving = true;
            }
            if(key.getCode()== KeyCode.S) {
                moving = false;
            }
        });
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
        if (moving) {
            e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity)));
        }
        e.getDirection().pivot(0);
        e.getDirection().normalize();
    }

}
