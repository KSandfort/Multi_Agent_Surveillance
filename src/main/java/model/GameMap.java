package model;

import Enities.Guard;
import Enities.Intruder;
import controller.GameController;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class that represents a map state of the simulation
 */
public class GameMap {

    // Variables
    private GameController gameController;
    private int sizeX; //height
    private int sizeY; //width
    private ArrayList<MapItem> staticItems = new ArrayList<>();
    private ArrayList<MapItem> movingItems = new ArrayList<>();
    private ArrayList<MapItem> solidBodies = new ArrayList<>();
    private ArrayList<MapItem> transparentItems = new ArrayList<>();
    private SpawnArea spawnAreaGuards;
    private SpawnArea spawnAreaIntruders;

    /**
     * Constructor
     * @param controller
     */
    public GameMap(GameController controller) {
        this.gameController = controller;
    }

    public GameMap(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    /**
     * Constructor
     * @param sizeX
     * @param sizeY
     * @param staticItems
     * @param movingItems
     */
    public GameMap(int sizeX, int sizeY, ArrayList<MapItem> staticItems, ArrayList<MapItem> movingItems) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.staticItems = staticItems;
        this.movingItems = movingItems;
    }

    /**
     * Initializes map for testing purposes
     */
    public void initTestGameMap() {
        sizeX = 120;
        sizeY = 80;
        createBorderWalls();
        setSpawnAreaGuards(new SpawnArea(true, 2, 2, 20, 10));
        setSpawnAreaIntruders(new SpawnArea(false, 2, 65, 20, 75));
        addToMap(new Wall(50, 60, 55, 63));
        addToMap(new Wall(70, 70, 75, 80));
        addToMap(new Wall(60, 10, 75, 50));
        addToMap(new WallWithWindow(40, 20, 10, 40, true));
        addToMap(new Teleport(30, 60, 40, 50, 90, 40, 5,50));
    }

    /**
     * Populates the map with guards and intruders.
     * @param guards number of guards
     * @param intruders number of intruders
     * @param agentTypeGuard agent type of the guards
     * @param agentTypeIntruder agent type of the intruders
     */
    public void populateMap(int guards, int intruders, int agentTypeGuard, int agentTypeIntruder) {
        addGuards(guards);
        addIntruders(intruders);
    }

    /**
     * Adds items to the map.
     * It automatically puts the item in the corresponding list.
     * - DynamicItems
     * - StaticItems
     * - SolidItems
     * @param item item to put on the map
     */
    public void addToMap(MapItem item){
        if (item.isDynamicObject()){
            addToDynamicItems(item);
        }else{
            addToStaticItems(item);
        }
        if (item.isSolidBody()){
            if (item instanceof WallWithItem){
                addToSolidItems(((WallWithItem) item).getWalls().get(0));
                addToSolidItems(((WallWithItem) item).getWalls().get(1));
            }if (!(item instanceof WallWithWindow)){
                addToSolidItems(item);
            }
        }
        if (item.isTransparentObject()){
            addToTransparentItems(item);
        }
    }

    /**
     * Add guards in the intruder spawn-area.
     * @param numGuards number of guards to spawn
     */
    public void addGuards(int numGuards){
        for (int i = 0; i < numGuards; i++){
            Guard guard;
            if (spawnAreaGuards == null) {
                guard = new Guard(55, 30);
            }
            else {
                int randomX = ThreadLocalRandom.current().nextInt((int) spawnAreaGuards.x1, (int) spawnAreaGuards.x2 + 1);
                int randomY = ThreadLocalRandom.current().nextInt((int) spawnAreaGuards.y1, (int) spawnAreaGuards.y2 + 1);
                guard = new Guard(randomX, randomY);
            }
            addToMap(guard);
            // remoteGuard.setRemote();
        }
    }

    /**
     * Add intruders in the intruder spawn-area.
     * @param numIntruders number of intruders to spawn
     */
    public void addIntruders(int numIntruders){
        for (int i = 0; i < numIntruders; i++){
            Intruder intruder;
            if (spawnAreaGuards == null) {
                intruder = new Intruder(55, 30);
            }
            else {
                int randomX = ThreadLocalRandom.current().nextInt((int) spawnAreaIntruders.x1, (int) spawnAreaIntruders.x2 + 1);
                int randomY = ThreadLocalRandom.current().nextInt((int) spawnAreaIntruders.y1, (int) spawnAreaIntruders.y2 + 1);
                intruder = new Intruder(randomX, randomY);
            }
            addToMap(intruder);
            // remoteIntruder.setRemote();
        }
    }

    public void addToStaticItems(MapItem item) {
        staticItems.add(item);
        item.setMap(this);
    }

    public void addToDynamicItems(MapItem item) {
        movingItems.add(item);
        item.setMap(this);
    }

    public void addToSolidItems(MapItem item) {
        solidBodies.add(item);
        item.setMap(this);
    }

    public void addToTransparentItems(MapItem item) {
        transparentItems.add(item);
        item.setMap(this);
    }

    public void createBorderWalls() {
        // Corner positions
        Vector2D c1, c2, c3, c4;
        c1 = new Vector2D(0, 0);
        c2 = new Vector2D(sizeX, 0);
        c3 = new Vector2D(sizeX, sizeY);
        c4 = new Vector2D(0, sizeY);

        addToMap(new Wall(new Vector2D(c1.getX(), c1.getY() - 2), new Vector2D(c2.getX(), c2.getY() - 2), c2, c1)); // Top wall
        addToMap(new Wall(c2, new Vector2D(c2.getX() + 2, c2.getY()), new Vector2D(c3.getX() + 2, c3.getY()), c3)); // Right wall
        addToMap(new Wall(c4, c3, new Vector2D(c3.getX(), c3.getY() + 2), new Vector2D(c4.getX(), c4.getY() + 2))); // Bottom wall
        addToMap(new Wall(new Vector2D(c1.getX() - 2, c1.getY()), c1, c4, new Vector2D(c4.getX() - 2, c4.getY()))); // Left wall
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public ArrayList<MapItem> getStaticItems() {
        return staticItems;
    }

    public void setStaticItems(ArrayList<MapItem> staticItems) {
        this.staticItems = staticItems;
    }

    public ArrayList<MapItem> getMovingItems() {
        return movingItems;
    }

    public void setMovingItems(ArrayList<MapItem> movingItems) {
        this.movingItems = movingItems;
    }

    public ArrayList<MapItem> getSolidBodies() {
        return solidBodies;
    }

    public void setSolidBodies(ArrayList<MapItem> solidBodies) {
        this.solidBodies = solidBodies;
    }

    public ArrayList<MapItem> getTransparentItems() {
        return transparentItems;
    }

    public void setTransparentItems(ArrayList<MapItem> transparentItems) {
        this.transparentItems = transparentItems;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public SpawnArea getSpawnAreaGuards() {
        return spawnAreaGuards;
    }

    public void setSpawnAreaGuards(SpawnArea spawnAreaGuards) {
        this.spawnAreaGuards = spawnAreaGuards;
        addToMap(spawnAreaGuards);
    }

    public SpawnArea getSpawnAreaIntruders() {
        return spawnAreaIntruders;
    }

    public void setSpawnAreaIntruders(SpawnArea spawnAreaIntruders) {
        this.spawnAreaIntruders = spawnAreaIntruders;
        addToMap(spawnAreaIntruders);
    }
}
