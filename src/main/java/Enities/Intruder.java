package Enities;

import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
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
            Text text= new Text("Intruder 1");
            text.setX((getPosition().getX() * sf) + offset - 25);
            text.setY((getPosition().getY() * sf) + offset -12);
            Line line= new Line();
            line.setStrokeWidth(5);
            line.setStartX((getPosition().getX() * sf) + offset);
            line.setStartY((getPosition().getY() * sf) + offset);
            line.setEndX((getPosition().getX() * sf) + offset + 10);
            line.setEndY((getPosition().getY() * sf) + offset);
            line.setStroke(Color.web("#000099", 1));
            components.add(text);
            components.add(circle);
            components.add(line);
            return components;
        }
        return null;
    }
}
