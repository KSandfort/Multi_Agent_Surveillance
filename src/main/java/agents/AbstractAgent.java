package agents;

import Enities.Entity;
import model.MapItem;
import java.util.ArrayList;

/**
 * This is the abstract class of any agent.
 */
public abstract class AbstractAgent {

    protected Entity entityInstance;

    public void setEntityInstance(Entity entity) {
        this.entityInstance = entity;
    }

    public abstract void addControls();

    public abstract void changeMovement(ArrayList<MapItem> items);
}
