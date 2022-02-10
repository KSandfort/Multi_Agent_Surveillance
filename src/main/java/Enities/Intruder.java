package Enities;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import model.GameMap;

import java.util.ArrayList;

public class Intruder extends Entity{

    boolean isAlive = true;

    public Intruder(double x, double y) {
        super(x, y);
    }

    public boolean isAlive(){
        return isAlive;
    }

    public void kill(){
        isAlive = false;
    }

    @Override
    public ArrayList<Node> getComponents() {
        if (isAlive()){
            ArrayList<Node> components = new ArrayList<>();
            Circle circle = new Circle();
            circle.setCenterX(getPosition().getX());
            circle.setCenterY(getPosition().getY());
            circle.setRadius(20);
            components.add(circle);
            return components;
        }
        return null;
    }
}
