package model;

import Enities.Guard;
import Enities.Intruder;

import java.util.ArrayList;
import java.util.Arrays;

public class GameMap {

    private int sizeX; //height
    private int sizeY; //width
    private ArrayList<MapItem> items = new ArrayList<>();
    /* private double scaling;
     private double timeStep;
     private int gameMode;
     int[] teleport;
     List<int[]> shadedAreas;
     int[] texture; */

    public GameMap() {
    }

    public GameMap(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public GameMap(int sizeX, int sizeY, ArrayList<MapItem> items) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.items = items;
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

    public ArrayList<MapItem> getMapItems() {
        return items;
    }

    public void setMapItemList(ArrayList<MapItem> items) {
        this.items = items;
    }

    public void removeMapItem(MapItem item) {items.remove(item);}

    public void addMapItem(MapItem newItem){
        items.add(newItem);
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


}
