package Enities;

import agents.AbstractAgent;
import agents.RemoteAgent;
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
    AbstractAgent agent;
    static int intruderCount = 0;
    boolean isInTargetArea = false; //TODO

    public Intruder(double x, double y, GameMap currentMap) {
        super(x, y, currentMap);
        intruderCount++;
        this.ID = intruderCount;
    }

    @Override
    public boolean isIntruder() {
        return true;
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
            Text text= new Text("Intruder " + ID);
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
            ArrayList<Ray> rays = FOV();
            for (Ray ray : rays){
                components.add(ray.getComponent());
            }
            components.addAll(hitBox.getComponents());
            return components;
        }
        return null;
    }


    public void setRemote() {
        this.agent = new RemoteAgent();
        this.agent.setEntityInstance(this); // Agent needs to be able to access the Entity (this class).
        agent.addControls();
    }



}

