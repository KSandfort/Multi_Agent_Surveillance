package Enities;

import javafx.scene.Node;
import model.MapItem;
import model.SpawnArea;
import model.Vector2D;

import java.util.Random;

public abstract class Entity extends MapItem {
    double explorationFactor;
    double speed;
    double fovAngle;
    double fovLength;
    Vector2D fovDirection;
    boolean isIntruder;

    public Entity(int x, int y){
        this.setPosition(new Vector2D(x,y));
    }

    public Entity(SpawnArea spawnArea){
        Random rand = new Random();
        double x = rand.nextDouble()*spawnArea.getWidth() + spawnArea.getPosition().getX();
        double y = rand.nextDouble()*spawnArea.getWidth() + spawnArea.getPosition().getX();
    }
}
