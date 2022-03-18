package Enities;

import agents.AbstractAgent;
import agents.ExplorerBugAgent;
import agents.RandomAgent;
import agents.RemoteAgent;
import lombok.Getter;
import lombok.Setter;
import model.*;
import java.util.ArrayList;


/**
 * Abstract class of an entity on the map.
 */
@Getter
@Setter
public abstract class Entity extends MapItem {

    // Variables
    private EntityKnowledge entityKnowledge;
    private double fovAngle = 30;
    private double fovDepth = 20;
    protected Vector2D direction;
    private boolean isIntruder;
    private boolean isSprinting = true;
    private ArrayList<Ray> fov;
    private double turnSpeed; //rotation in degrees/sec
    private double radius = 1; //width of the entity
    protected int ID;
    HitBox hitBox;
    protected AbstractAgent agent;
    protected Vector2D prevPos;

    // Static
    public static double baseSpeedGuard = 0.2;
    public static double sprintSpeedGuard = 0.4;
    public static double baseSpeedIntruder = 0.2;
    public static double sprintSpeedIntruder = 0.4;

    /**
     * Constructor
     * @param x
     * @param y
     * @param currentMap
     */
    public Entity(double x, double y, GameMap currentMap) {
        setMap(currentMap);
        entityKnowledge = new EntityKnowledge(currentMap);
        this.setPosition(new Vector2D(x,y));
        this.direction = Vector2D.randomVector();
        Vector2D c1 = Vector2D.add(getPosition(), new Vector2D(-radius,-radius));
        Vector2D c2 = Vector2D.add(getPosition(), new Vector2D(radius,-radius));
        Vector2D c3 = Vector2D.add(getPosition(), new Vector2D(-radius,radius));
        Vector2D c4 = Vector2D.add(getPosition(), new Vector2D(radius,radius));
        hitBox = new HitBox(c1,c2,c3,c4);
        entityKnowledge.setPositionOffset(getPosition());
    }

    /**
     * Setter
     * @param map
     */
    public void setMap(GameMap map){
        this.map = map;
    }

    /**
     * Gives the Entity a new position, based on the agent.
     */
    public void update(ArrayList<MapItem> items) {
        Vector2D previousPos = new Vector2D(getPosition().getX(), getPosition().getY());
        if (this.agent != null) {
            agent.changeMovement(items);
        }
        // Check collision detection
        boolean inSpecialArea = false;
        for(MapItem item : items) {
            if (((Area) item).isAgentInsideArea(this)){
                Area areaItem = (Area) item;
                areaItem.onAgentCollision(this);
                inSpecialArea = true;
            }
        }
        if(!inSpecialArea){
            resetEntityParam();
        }
        // Update HitBox
        this.getHitBox().transform(this);

        // Update agent knowledge
        entityKnowledge.setCell(1, previousPos); // Remove current position marker
        // Add new position marker
        entityKnowledge.setCell(2, getPosition());
    }

    /**
     * Sets the agent of a guard.
     * 0 = random agent
     * 1 = remote agent
     * @param type
     */
    public void setAgent(int type) {
        switch (type) {
            case 0: { // Random Agent
                agent = new RandomAgent();
                agent.setEntityInstance(this);
                break;
            }
            case 1: { // Remote Agent
                agent = new RemoteAgent();
                agent.setEntityInstance(this);
                agent.addControls();
                break;
            }
            case 2: { // Bug Agent
                agent = new ExplorerBugAgent();
                agent.setEntityInstance(this);
            }
            default: {
                System.out.println("No agent defined!");
            }
        }
    }

    public void resetEntityParam(){
        this.setFovAngle(30);
        this.setFovDepth(20);
    }

    public abstract boolean isIntruder();

    public Vector2D [] getCornerPoints(){
        return hitBox.getCornerPoints();
    }

    /**
     * Creates a field of view for an entity.
     * @return
     */
    public ArrayList<Ray> FOV() {
        ArrayList<Ray> rays = new ArrayList<>();
        // Create all the rays
        for (double i = -0.5 * fovAngle; i <= 0.5 * fovAngle; i+= 1){
            Vector2D direction = new Vector2D(
                    getDirection().getX()*Math.cos(Math.toRadians(i)) - getDirection().getY()*Math.sin(Math.toRadians(i)),
                    getDirection().getX()*Math.sin(Math.toRadians(i)) + getDirection().getY()*Math.cos(Math.toRadians(i))
            );
            Ray ray = new Ray(getPosition(), Vector2D.resize(direction, fovDepth));
            GameMap map = this.map;
            double minDistance = fovDepth;
            // Scan all fixed items on the map
            for (MapItem item: map.getSolidBodies()) {
                Area area = (Area) item;
                // Find the closest object to avoid "seeing through walls"
                for (int j = 0; j < area.getCornerPoints().length; j++){
                    double currentDistance = Vector2D.distance(ray.getOrigin(), ray.getPoint(), area.getCornerPoints()[j], area.getCornerPoints()[(j + 1) % 4]);
                    if (currentDistance < minDistance && currentDistance > 0) {
                        minDistance = currentDistance;
                    }
                }
            }
            // Set the length of the ray accordingly.
            ray.setDirection(Vector2D.resize(ray.getDirection(), minDistance));
            rays.add(ray);
            addVisionKnowledge(ray);
        }
        return rays;
    }

    /**
     * Adds knowledge to entityKnowledge based on what the ray observes
     * @param ray ray that observes
     */
    public void addVisionKnowledge(Ray ray) {
        //TODO: Implement actual vision!
        double rayLength = Vector2D.length(ray.getDirection());
        int rayLengthInt = (int) Math.floor(rayLength);
        int detailLevel = 2; // Increase to 2 or more, in case there are too many empty spots in the vision
        for (int i = 1*detailLevel; i < rayLengthInt*detailLevel - detailLevel; i++) {
            Vector2D currentTarget = Vector2D.add(ray.origin, Vector2D.resize(ray.direction, i/detailLevel));
            entityKnowledge.setCell(1, currentTarget);
        }
        double epsilon = 0.01; // Since the distance correction has some round-off errors, add a small buffer
        if (rayLength + epsilon < fovDepth) { // Display walls in vision
            entityKnowledge.setCell(3, ray.getPoint());
        }
    }

    public boolean checkWinningCondition(){
        return false;
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
    public boolean isTransparentObject() {
        return false;
    }
}
