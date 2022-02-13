package Enities;

import agents.AbstractAgent;
import agents.GuardRemote;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * This class represents a guard on the board.
 */
public class Guard extends Entity {

    // Variables
    AbstractAgent agent;
    static int guardCount = 0;
    /**
     * Constructor
     * @param x
     * @param y
     */
    public Guard(double x, double y){
        super(x, y);
        guardCount++;
        this.ID = guardCount;
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
        Line line = new Line(
                (getPosition().getX() * sf) + offset,
                (getPosition().getY() * sf) + offset,
                (getPosition().getX() * sf) + offset + 10,
                (getPosition().getY() * sf) + offset );
        line.setStroke(Color.web("#C8E1E7", 1));
        line.setStrokeWidth(5);
        Text text= new Text("Guard " + ID);
        text.setX((getPosition().getX() * sf) + offset -20);
        text.setY((getPosition().getY() * sf) + offset -12);
        components.add(text);
        components.add(circle);
        ArrayList<Ray> rays = this.FOV();
        for (Ray ray : rays){
            components.add(ray.getComponent());
        }
        return components;
    }

    @Override
    public boolean isSolidBody() {
        return false;
    }

    @Override
    public boolean isDynamicObject() {
        return true;
    }

    @Override
    public boolean isStaticObject() {
        return false;
    }
}
