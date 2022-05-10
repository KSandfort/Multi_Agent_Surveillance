package Enities;

import agents.*;
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
    private double fovAngle = 90;
    private double fovDepth = 20;
    protected Vector2D direction;
    private boolean isSprinting = false;
    private ArrayList<Ray> fov;
    private double turnSpeed; //rotation in degrees/sec
    private double radius = 1; //width of the entity
    private boolean leftSpawn = false; // has the agent left spawn already? used for guard on guard collision
    protected int ID;
    HitBox hitBox;
    protected AbstractAgent agent;
    protected Vector2D prevPos;
    protected double[][] markerSensing; // row = marker type [0, ..., 4],
                                        // column 0 = amount, column 1 = avg angle from direction (positive = right)
    public double stamina = maxStamina;
    // Static
    public static double maxStamina = 100;
    public static double sprintConsumption = 8;
    public static double staminaRegeneration = 5;
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
        entityKnowledge = new EntityKnowledge(currentMap, !isIntruder());
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

        if (isSprinting){
            if (stamina <= 0){
                setSprinting(false);
            }
            else{
                stamina -= sprintConsumption;
            }
        }
        else if (!isSprinting && stamina < maxStamina){
            stamina += staminaRegeneration;
        }

        // Check collision detection
        if(!isInSpecialArea(items)){
            resetEntityParam();
        }
        // Update HitBox
        this.getHitBox().transform(this);

        // Update agent knowledge
        entityKnowledge.setCell(1, previousPos); // Remove current position marker
        // Add new position to internal knowledge
        entityKnowledge.setCell(2, getPosition());

        // detect markers
        markerSensing = new double[5][2];
        for (Marker marker : map.getMarkers()) {
            // check if it is in fov range and of its own team
            if (Vector2D.distance(this.getPosition(), marker.getPosition()) <= fovDepth && this.isIntruder() == marker.isFromIntruder()) {
                // check if it is in fov angel
                Vector2D markerDir = Vector2D.subtract(marker.getPosition(), this.getPosition());
                double angle = Vector2D.shortestAngle(this.getDirection(), markerDir); // angle between entity direction and marker
                if (Math.abs(angle) < 0.5*fovAngle) {
                    // check if there is a wall blocking the line between
                    boolean lineIsFree = true;
                    for (MapItem wall : map.getSolidBodies()) {
                        outerLoop:
                        if (wall instanceof Wall) {
                            for (int i = 0; i < 4; i++) {
                                if (fovDepth >= Vector2D.distance(this.getPosition(), marker.getPosition(), wall.getCornerPoints()[i], wall.getCornerPoints()[(i + 1) % 4])) {
                                    lineIsFree = false;
                                    break outerLoop;
                                }
                            }
                        }
                    }
                    if (lineIsFree) { // This means that a marker can be seen from the field of view
                        int previousCount = (int) markerSensing[marker.getMarkerType()][0];
                        double previousAngle = markerSensing[marker.getMarkerType()][1];
                        markerSensing[marker.getMarkerType()][0] = previousCount + marker.getIntensity()/Vector2D.distance(getPosition(), marker.getPosition());
                        markerSensing[marker.getMarkerType()][1] = previousAngle + (angle / markerSensing[marker.getMarkerType()][0]);
                    }
                }
            }
        }
        // System.out.println("Marker: " + markerSensing[0][0] + " " + markerSensing[0][1]);
    }

    public boolean isInSpecialArea(ArrayList<MapItem> items){
        return (getCurrentArea(items) != null);
    }

    /**
     *
     * @param items list of static items of the map
     * @return the area that the entity is currently residing in, null if it is not residing in a special srea type
     */
    public Area getCurrentArea(ArrayList<MapItem> items){
        Area insideArea = null;

        boolean isInSpawn = false;

        for(MapItem item : items) {
            if (item == this)
                continue;

            // Agent/Agent collision (only if the agent has already left spawn)
            if (item instanceof Guard || item instanceof Intruder) {
                if (!leftSpawn)
                    continue;

                Vector2D[] corners = ((Entity)item).hitBox.getCornerPoints();

                Area tempArea = new Wall(corners[1].getX(), corners[1].getY(), corners[3].getX(), corners[3].getY());
                if (tempArea.isAgentInsideArea(this)) {
                    tempArea.onAgentCollision(this);
                    insideArea = tempArea;
                }
            }

            // Agent/Area collision
            else if (((Area) item).isAgentInsideArea(this)){

                if (item instanceof SpawnArea)
                    isInSpawn = true;

                Area areaItem = (Area) item;
                areaItem.onAgentCollision(this);
                insideArea = areaItem;
            }
        }

        if (!isInSpawn && !leftSpawn)
            leftSpawn = true;

        return insideArea;
    }

    public void onAgentCollision(Entity entity)
    {
        Vector2D pos = entity.getPosition();
        entity.setPosition(entity.getPrevPos());
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
                break;
            }
            case 3: { // Intruder Destroyer
                agent = new RuleBasedGuardAgent();
                agent.setEntityInstance(this);
                break;
            }
            case 4: { // Ant Agent
                agent = new AntAgent();
                agent.setEntityInstance(this);
                break;
            }
            case 5: { // NEAT Agent
                agent = new NeatAgent();
                agent.setEntityInstance(this);
                break;
            }
            default: {
                System.out.println("No agent defined!");
            }
        }
    }

    public void resetEntityParam(){
        this.setFovAngle(90);
        this.setFovDepth(20);
    }

    /**
     * Defines if an entity is an intruder or a guard, if the return value is false.
     * @return
     */
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
        for (double i = -0.5 * fovAngle; i <= 0.5 * fovAngle; i+= 3){
            Vector2D direction = new Vector2D(
                    getDirection().getX()*Math.cos(Math.toRadians(i)) - getDirection().getY()*Math.sin(Math.toRadians(i)),
                    getDirection().getX()*Math.sin(Math.toRadians(i)) + getDirection().getY()*Math.cos(Math.toRadians(i))
            );
            Ray ray = new Ray(getPosition(), Vector2D.resize(direction, fovDepth));
            GameMap map = this.map;
            double minDistance = fovDepth;
            // Scan all fixed items on the map
            for (MapItem item: map.getSolidBodies()) {
                if (item instanceof Entity) {
                    continue;
                }
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

    /**
     * Places a marker on the current position of the map.
     * @param type
     */
    public void placeMarker(int type) {
        Marker marker = new Marker(type, this.getPosition(), isIntruder());
        map.addToMap(marker);
    }

    /**
     * Checks the winning condition for the game
     * TODO: Implement
     * @return
     */
    public boolean checkWinningCondition(){
        return false;
    }

    /**
     * entities are audible based on their speed and the proximity to other entities
     * gets a volume factor for an entity based on its speed, current area and yelling status
     * @param entity
     * @param items list of static items of the current map
     * @return
     */
    public double calculateAudibleFactor(Entity entity, ArrayList<MapItem> items){
        double dist = Vector2D.distance(this.getPosition(), entity.getPosition());
        double speedVolume = 1; double yellVolume = 1; double areaVolume = 1;
        Area curArea = entity.getCurrentArea(items);
        if (curArea != null){
            areaVolume = curArea.getAreaSoundVolume();
        }

        if (entity instanceof Guard){
            if (((Guard)entity).isYelling()){
                yellVolume = ((Guard)entity).getYellingFactor();
            }
            if (entity.isSprinting()){
                speedVolume = sprintSpeedGuard;
            }else{
                speedVolume = baseSpeedGuard;
            }
        } else{
            if (entity.isSprinting()){
                speedVolume = sprintSpeedIntruder;
            }else{
                speedVolume = baseSpeedIntruder;
            }
        }

        // want closer entities to be heard better
        return ((1/dist) * speedVolume * yellVolume * areaVolume);
    }

    /**
     * gets the direction from which the agent hears a sound
     *
     * entity will hear the entity that has the highest audible factor
     * the direction will be determined based on the positions of the entities, their velocity and what area they move in
     * @return a vector for the direction from which the sound is most audible
     */
    public Vector2D getListeningDirection(ArrayList<MapItem> entities, ArrayList<MapItem> items){
        double audibilityFactor = 0;
        double newAudibleFactor;
        Vector2D listeningDir = new Vector2D(0,0);
        for (MapItem entity : entities){
            if (!(this == entity)){
                newAudibleFactor = this.calculateAudibleFactor((Entity) entity, items);
                if ((newAudibleFactor > audibilityFactor)){
                    listeningDir = Vector2D.subtract(entity.getPosition(), this.getPosition());
                    audibilityFactor = newAudibleFactor;
                }
            }
        }
        return listeningDir; //TODO add uncertainty
    }

    public ArrayList <Entity> getDetectedEntities(){
        ArrayList<MapItem> entities = this.getMap().getMovingItems();
        ArrayList<Ray> fov = FOV();
        ArrayList<Entity> detectedEntities = new ArrayList<>();
        for (MapItem mapItem :entities){
            Entity entity = (Entity) mapItem;
            for (Ray ray : fov) {
                for (Entity e: ray.getDetectedEntities(this)){
                    if (!Ray.contains(detectedEntities, e)){
                        detectedEntities.add(e);
                    }
                }

            }
        }
        return detectedEntities;
    }

    public void setSprinting(boolean sprint){
        if (sprint && stamina <= 0){
            isSprinting = false;
        }
        else{
            isSprinting = sprint;
        }
    }


    @Override
    public boolean isSolidBody() {
        return true;
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
