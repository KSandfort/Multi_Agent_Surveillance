package Enities;

import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
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
            double sf = SimulationGUI.SCALING_FACTOR;
            int offset = SimulationGUI.CANVAS_OFFSET;
            ArrayList<Node> components = new ArrayList<>();
            Circle circle = new Circle();
            circle.setFill(Color.web("#FF0000", 1));
            circle.setCenterX((getPosition().getX() * sf) + offset);
            circle.setCenterY((getPosition().getY() * sf) + offset);
            circle.setRadius(1 * sf);
            components.add(circle);
            ArrayList<Ray> rays = this.FOV();
            for (Ray ray : rays){
                components.add(ray.getComponent());
            }
            return components;
        }
        return null;
    }
}
