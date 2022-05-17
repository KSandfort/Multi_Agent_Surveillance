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
import model.MapItem;
import model.TargetArea;
import model.Vector2D;


import java.util.ArrayList;

public class Intruder extends Entity{

    boolean isAlive = true;
    AbstractAgent agent;
    static int intruderCount = 0;
    Vector2D targetAreaDirection;
    boolean isDetected = false;

    public Intruder(double x, double y, GameMap currentMap) {
        super(x, y, currentMap);
        intruderCount++;
        this.setID(intruderCount);
    }


    @Override
    public boolean isIntruder() {
        return true;
    }

    public boolean isAlive(){
        return isAlive;
    }

    public void kill(){
        GameMap map = this.getMap();
        isAlive = false;
    }

    @Override
    public void update(ArrayList<MapItem> mapItems){
        super.update(mapItems);
        //updates intruder knowledge on target area direction
        if (getMap().getTargetArea() != null){
            Vector2D targetPosition = getMap().getTargetArea().getPosition();
            this.targetAreaDirection = Vector2D.normalize(Vector2D.subtract(targetPosition, getPosition()));
        }
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
            Text text= new Text("Intruder " + this.getID());
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
        return new ArrayList<>();
    }

    /**
     * @return true if the intruder is alive and in the target area
     */
    public boolean checkWinningCondition(){
        return isAlive() && isInTargetArea();
    }

    /**
     * @return true if the intruder is inside any of the specified target areas, false otherwise
     */
    public boolean isInTargetArea(){
        ArrayList<MapItem> areas = map.getStaticItems();
        for (MapItem target : areas){
            if (target instanceof TargetArea){
                if(((TargetArea) target).isInsideArea(getPosition())){
                    return true;
                }
            }
        }
        return false;
    }

    public void setRemote() {
        this.agent = new RemoteAgent();
        this.agent.setEntityInstance(this); // Agent needs to be able to access the Entity (this class).
        agent.addControls();
    }



}

