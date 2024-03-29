package Enities;

import agents.ExplorerBugAgent;
import agents.RemoteAgent;
import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import model.GameMap;
import model.MapItem;
import model.Vector2D;

import java.util.ArrayList;

/**
 * This class represents a guard on the board.
 */
@Getter
@Setter
public class Guard extends Entity {

    // Variables
    static int guardCount = 0;
    private boolean isYelling = false;
    private double yellingFactor = 10;
    public int killCount = 0;

    // Static
    public static double killDistance = 3;

    /**
     * Constructor
     * @param x
     * @param y
     */
    public Guard(double x, double y, GameMap currentMap){
        super(x, y, currentMap);
        guardCount++;
        this.setID(guardCount);
    }

    @Override
    public boolean isIntruder() {
        return false;
    }

    public ArrayList<Intruder> detected(){
        ArrayList<Intruder> detected = new ArrayList<>();
        ArrayList<Ray> rays = FOV();
        for (Ray ray : rays){
            for (MapItem item : ray.getDetectedItems(this)){
                if (item instanceof Intruder){
                   detected.add((Intruder) item);
                }
            }
        }
        return detected;
    }

    public void kill(Intruder intruder){
        if (Vector2D.distance(getPosition(), intruder.getPosition()) < killDistance){
            intruder.kill();
            killCount++;
        }
    }
    /**
     * @return false if any intruder is still alive, true otherwise
     */
    public boolean checkWinningCondition(){
        boolean won = true;
        ArrayList<MapItem> entities = map.getMovingItems();
        for (MapItem entity : entities){
            if (entity instanceof Intruder){
                if (Vector2D.distance(position, entity.getPosition()) < killDistance)
                    kill((Intruder) entity);

                if(((Intruder) entity).isAlive){
                    won = false;
                }
            }
        }
        return won;
    }

    public void chase(Vector2D position){
        Vector2D difference = Vector2D.subtract(position, getPosition());
        this.direction = difference;
    }

    public boolean isYelling(){
        return isYelling;
    }

    /**
     * Needs to be called if a Guard should be controlled via user input.
     */
    public void setRemote() {
        this.agent = new RemoteAgent();
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
        Text text= new Text("Guard " + this.getID());
        text.setX((getPosition().getX() * sf) + offset -20);
        text.setY((getPosition().getY() * sf) + offset -12);
        components.add(text);
        components.add(circle);

        ArrayList<Ray> rays = this.getFov();
        for (Ray ray : rays){
            components.add(ray.getComponent());
        }
        components.addAll(hitBox.getComponents());
        return components;
    }
}
