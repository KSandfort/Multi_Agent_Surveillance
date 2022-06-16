package agents;

import Enities.Entity;
import model.MapItem;
import java.util.ArrayList;

/**
 * This is the abstract class of any agent.
 */
public abstract class AbstractAgent {

    // Variables
    protected Entity entityInstance;
    protected double baseVelocity = 0;
    protected double sprintVelocity = 0;
    protected double maxAngle = 20;

    public void setEntityInstance(Entity entity) {
        this.entityInstance = entity;
        if (this.entityInstance.isIntruder()) {
            baseVelocity = Entity.baseSpeedIntruder;
            sprintVelocity = Entity.sprintSpeedIntruder;
        }
        else {
            baseVelocity = Entity.baseSpeedGuard;
            sprintVelocity = Entity.sprintSpeedGuard;
        }
    }

    public abstract void addControls();

    public abstract void changeMovement(ArrayList<MapItem> items);
}
