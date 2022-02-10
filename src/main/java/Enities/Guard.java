package Enities;

import agents.AbstractAgent;
import agents.GuardRemote;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.GameMap;

import java.util.ArrayList;

/**
 * This class represents a guard on the board.
 */
public class Guard extends Entity {

    // Variables
    AbstractAgent agent;

    /**
     * Constructor
     * @param x
     * @param y
     */
    public Guard(double x, double y){
        super(x, y);
    }

    /**
     * Needs to be called if a Guard should be controlled via user input.
     */
    public void setRemote() {
        this.agent = new GuardRemote();
        this.agent.setEntityInstance(this); // Agent needs to be able to access the Entity (this class).
        agent.addControls();
    }

    @Override
    public ArrayList<Node> getComponents() {
        double sf = SimulationGUI.SCALING_FACTOR;
        int offset = SimulationGUI.CANVAS_OFFSET;
        ArrayList<Node> components = new ArrayList<>();
        Circle circle = new Circle();
        circle.setFill(Color.web("#0000FF", 1));
        circle.setCenterX((getPosition().getX() * sf) + offset);
        circle.setCenterY((getPosition().getY() * sf) + offset);
        circle.setRadius(1 * sf);
        components.add(circle);
        return components;
    }
}
