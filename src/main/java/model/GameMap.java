package model;

import Enities.Guard;
import controller.GameController;
import java.util.ArrayList;

public class GameMap {

    private GameController gameController;
    private int sizeX; //height
    private int sizeY; //width
    private ArrayList<MapItem> staticItems = new ArrayList<>();
    private ArrayList<MapItem> movingItems = new ArrayList<>();
    private ArrayList<MapItem> solidBodies = new ArrayList<>();

/* private double scaling;
     private double timeStep;
     private int gameMode;
     int[] teleport;
     List<int[]> shadedAreas;
     int[] texture; */

    public GameMap(GameController controller) {
        this.gameController = controller;
    }

    public GameMap(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public GameMap(int sizeX, int sizeY, ArrayList<MapItem> staticItems, ArrayList<MapItem> movingItems) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.staticItems = staticItems;
        this.movingItems = movingItems;
    }

    // TODO: move this
    // initializes map for testing purposes
    public void initTestGameMap() {
        sizeX = 120;
        sizeY = 80;
        createBorderWalls();
        addToMap(new SpawnArea(2, 2, 20, 10));
        addToMap(new Wall(50, 60, 55, 63));
        addToMap(new Wall(70, 70, 75, 80));
        addToMap(new Wall(60, 10, 75, 50));
//        movingItems.add(new Intruder(20,20));
        Guard remoteGuard = new Guard(55, 30);
        addToMap(remoteGuard);
        remoteGuard.setRemote();

    }

    public void addToMap(MapItem item){
        if (item.isDynamicObject()){
            addToDynamicItems(item);
        }
        if (item.isSolidBody()){
            addToSolidItems(item);
        }
        if (item.isStaticObject()){
            addToStaticItems(item);
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

    public void createBorderWalls() {
        // Corner positions
        Vector2D c1, c2, c3, c4;
        c1 = new Vector2D(0, 0);
        c2 = new Vector2D(sizeX, 0);
        c3 = new Vector2D(sizeX, sizeY);
        c4 = new Vector2D(0, sizeY);
        staticItems.add(new Wall(new Vector2D(c1.getX(), c1.getY() - 2), new Vector2D(c2.getX(), c2.getY() - 2), c2, c1)); // Top wall
        staticItems.add(new Wall(c2, new Vector2D(c2.getX() + 2, c2.getY()), new Vector2D(c3.getX() + 2, c3.getY()), c3)); // Right wall
        staticItems.add(new Wall(c4, c3, new Vector2D(c3.getX(), c3.getY() + 2), new Vector2D(c4.getX(), c4.getY() + 2))); // Bottom wall
        staticItems.add(new Wall(new Vector2D(c1.getX() - 2, c1.getY()), c1, c4, new Vector2D(c4.getX() - 2, c4.getY()))); // Left wall
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

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
