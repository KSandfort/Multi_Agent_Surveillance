package Enities;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

public class Guard extends Entity{

    public Guard(int x, int y){
        super(x, y);
    }

    @Override
    public Node getComponent() {
        Circle circle = new Circle();
        circle.setCenterX(getPosition().getX());
        circle.setCenterY(getPosition().getY());
        circle.setRadius(20);
        return circle;
    }
}
