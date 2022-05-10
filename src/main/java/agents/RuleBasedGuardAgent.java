package agents;

import Enities.Entity;
import model.MapItem;
import model.Vector2D;

import java.util.ArrayList;
import java.util.Random;

public class RuleBasedGuardAgent extends AbstractAgent{
    double explorationFactor = 0.2;

    @Override
    public void addControls() {

    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        Entity e = entityInstance;
        double velocity = 0;
        if (e.isIntruder()) {
            velocity = Entity.baseSpeedIntruder;
        }
        else {
            velocity = Entity.baseSpeedGuard;
        }
        ArrayList<Entity> detectedEntities = e.getDetectedEntities();
        if (detectedEntities.size() == 0){
            Vector2D prevPos = e.getPosition();
            e.setPrevPos(prevPos);

            e.getDirection().pivot((new Random().nextDouble()*180 - 90)*explorationFactor);
            e.getDirection().normalize();
            e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity)));
        }
        else {
            for (Entity otherEntity : detectedEntities) {
                if (otherEntity.isIntruder()) {
                    Vector2D intruderDirection = Vector2D.subtract(otherEntity.getPosition(), e.getPosition());
                    e.setDirection(intruderDirection);
                    e.getDirection().normalize();
                    e.setSprinting(true);
                    if (e.isSprinting()){
                        velocity = Entity.sprintSpeedGuard;
                    }
                }
                e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity)));
            }
        }
    }
}
