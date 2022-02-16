package Enities;

import javafx.scene.Node;
import model.Vector2D;

import java.util.ArrayList;

public class RandomAgent extends Entity{

    public RandomAgent(int x, int y){
        super(x,y);
        explorationFactor = 1;

    }

    @Override
    public ArrayList<Node> getComponents() {
        return null;
    }

    @Override
    public boolean isSolidBody() {
        return false;
    }

    @Override
    public boolean isDynamicObject() {
        return true;
    }

    @Override
    public boolean isStaticObject() {
        return false;
    }

    @Override
    public boolean isIntruder() {
        return false;
    }
}
