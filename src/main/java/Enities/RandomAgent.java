package Enities;

import javafx.scene.Node;

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
    public boolean isIntruder() {
        return false;
    }
}
