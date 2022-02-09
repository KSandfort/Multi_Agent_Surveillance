package Enities;

import gui.sceneLayouts.MainLayout;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

public class Intruder extends Entity{

    boolean isAlive = true;

    public Intruder(int x, int y) {
        super(x, y);
    }

    public boolean isAlive(){
        return isAlive;
    }

    public void kill(){
        isAlive = false;
    }

    @Override
    public Node getComponent() {
        if (isAlive()){
            Circle circle = new Circle();
            circle.setCenterX(getPosition().getX());
            circle.setCenterY(getPosition().getY());
            circle.setRadius(20);
            return circle;
        }
        return null;
    }
}
