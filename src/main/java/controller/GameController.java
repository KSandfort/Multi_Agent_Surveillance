package controller;

import gui.SimulationGUI;
import gui.sceneLayouts.MainLayout;
import javafx.scene.Node;
import model.GameMap;
import model.MapItem;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.util.ArrayList;

/**
 * This class acts as the heart of the game. It controls all the parts
 * and is the point where GUI and back end come together.
 */
public class GameController {

    // Variables
    GameMap map;
    SimulationGUI simulationGUI;
    int updatesPerSecond;
    int step;

    public GameController() {
        GameMap map = new GameMap();
        this.map = map;
        this.map.initTestGameMap();
        simulationGUI = new SimulationGUI();
        simulationGUI.setController(this);
    }

    // do the update magic...
    public void update() {
        ArrayList<MapItem> items = map.getMapItems();
        for(MapItem item : items) {
            item.update();
        }
    }

    public void drawMap(MainLayout layout){
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<MapItem> items = map.getMapItems();
        for (MapItem item : items){
            nodes.add(item.getComponent());
        }
        layout.getChildren().addAll(nodes);
    }

    public void setMap(GameMap map){
        this.map = map;
    }

    /**
     * Executes the loop to run the simulation
     */

}
