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

    /**
     * Constructor
     */
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
            if(key.getCode()== KeyCode.A) {
                entityInstance.getDirection().pivot(-5);
                entityInstance.getDirection().normalize();
            }
            if(key.getCode()== KeyCode.D) {
                entityInstance.getDirection().pivot(5);
                entityInstance.getDirection().normalize();
            }
            if(key.getCode()== KeyCode.Q) {
                entityInstance.setSprinting(true);
            }
            if(key.getCode()== KeyCode.P) {
                System.out.println("Detected Entities: " + entityInstance.getDetectedEntities().size());
            }
            if(key.getCode()== KeyCode.DIGIT1) {
                entityInstance.placeMarker(0);
            }
            if(key.getCode()== KeyCode.DIGIT2) {
                entityInstance.placeMarker(1);
            }
            if(key.getCode()== KeyCode.DIGIT3) {
                entityInstance.placeMarker(2);
            }
            if(key.getCode()== KeyCode.DIGIT4) {
                entityInstance.placeMarker(3);
            }
            if(key.getCode()== KeyCode.DIGIT5) {
                entityInstance.placeMarker(4);
            }
            if(key.getCode()== KeyCode.Q) {
                entityInstance.setSprinting(true);
            }
        }});
    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        Entity e = entityInstance;
        prevPos = e.getPosition();
        e.setPrevPos(prevPos);
        double velocity = 0;
        if (e.isIntruder()) {

            velocity = Entity.baseSpeedIntruder;
            if (e.isSprinting()){
                velocity = Entity.sprintSpeedIntruder;
            }
        }
        else {
            velocity = Entity.baseSpeedGuard;
            if (e.isSprinting()){
                velocity = Entity.sprintSpeedGuard;
            }
        }
        if (moving) {
            e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity)));
        }
        e.getDirection().normalize();
    }

}
