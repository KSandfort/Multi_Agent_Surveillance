package agents;

import Enities.Entity;

/**
 * This is the abstract class of any agent.
 */
public abstract class AbstractAgent {

    protected Entity entityInstance;

    public void setEntityInstance(Entity entity) {
        this.entityInstance = entity;
    }

    public abstract void addControls();
}
