package model;

import Enities.Entity;
import Enities.Guard;
import controller.GameController;

import java.util.ArrayList;

public class GameMap {

    private GameController gameController;
    private int sizeX; //height
    private int sizeY; //width
    private ArrayList<MapItem> fixedItems = new ArrayList<>();
    private ArrayList<MapItem> movingItems = new ArrayList<>();

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

    public GameMap(int sizeX, int sizeY, ArrayList<MapItem> fixedItems, ArrayList<MapItem> movingItems) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.fixedItems = fixedItems;
        this.movingItems = movingItems;
    }

    // TODO: move this
    // initializes map for testing purposes
    public void initTestGameMap() {
        sizeX = 1000;
        sizeY = 1000;
        MapItem spawnAreaIntruders = new SpawnArea(2, 2, 20, 10);
        MapItem wall1 = new Wall(50, 60, 55, 63);
        MapItem wall2 = new Wall(70, 80, 75, 90);
        items = new ArrayList<>(Arrays.asList(
                new SpawnArea(2, 2, 20, 10),
                new Wall(50, 60, 55, 63),
                new Wall(70, 80, 75, 90),
                new Wall(70, 80, 75, 90),
                new Intruder(20,20),
                new Guard(300,300)
                ));
    }
  
    public void addToFixedItems(MapItem item) {
        fixedItems.add(item);
    }

    public void addToMovingItems(Entity entity) {
        movingItems.add(entity);
    }

    // TODO: move this
    // initializes map for testing purposes
    public void initTestGameMap() {
        sizeX = 120;
        sizeY = 80;
        createBorderWalls();
        MapItem testWall = new Wall(new Vector2D(20, 20), new Vector2D(25, 27), new Vector2D(28, 21), new Vector2D(24, 18));
        this.fixedItems.add(testWall);

        Guard guard = new Guard(30, 30);
        guard.map = this;
        guard.setRemote();
        this.movingItems.add(guard);
    }

    public void createBorderWalls() {
        // Corner positions
        Vector2D c1, c2, c3, c4;
        c1 = new Vector2D(0, 0);
        c2 = new Vector2D(sizeX, 0);
        c3 = new Vector2D(sizeX, sizeY);
        c4 = new Vector2D(0, sizeY);
        fixedItems.add(new Wall(new Vector2D(c1.getX(), c1.getY() - 2), new Vector2D(c2.getX(), c2.getY() - 2), c2, c1)); // Top wall
        fixedItems.add(new Wall(c2, new Vector2D(c2.getX() + 2, c2.getY()), new Vector2D(c3.getX() + 2, c3.getY()), c3)); // Right wall
        fixedItems.add(new Wall(c4, c3, new Vector2D(c3.getX(), c3.getY() + 2), new Vector2D(c4.getX(), c4.getY() + 2))); // Bottom wall
        fixedItems.add(new Wall(new Vector2D(c1.getX() - 2, c1.getY()), c1, c4, new Vector2D(c4.getX() - 2, c4.getY()))); // Left wall
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

    public ArrayList<MapItem> getFixedItems() {
        return fixedItems;
    }

    public void setFixedItems(ArrayList<MapItem> fixedItems) {
        this.fixedItems = fixedItems;
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
