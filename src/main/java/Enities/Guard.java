package Enities;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Guard extends Entity{

    public Guard(int x, int y){
        super(x, y);
    }

    @Override
    public ArrayList<Node> getComponents() {
        ArrayList<Node> components = new ArrayList<>();
        Circle circle = new Circle();
        circle.setCenterX(getPosition().getX());
        circle.setCenterY(getPosition().getY());
        circle.setRadius(20);
        components.add(circle);
        return components;
    }
}
