package controller;

import gui.SimulationGUI;
import gui.sceneLayouts.MainLayout;
import javafx.scene.Node;
import model.GameMap;
import model.MapItem;

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

    public GameController(SimulationGUI gui) {
        this.simulationGUI = gui;
        GameMap map = new GameMap(this);
        this.map = map;
        //TEMPORARY -- for testing purposes
        this.map.initTestGameMap();
    }

    // do the update magic...
    public void update() {
        ArrayList<MapItem> items = map.getMovingItems();
        for(MapItem item : items) {
            item.update();
        }
    }

    /**
     * Draws fixed components on the map.
     * @param layout
     */
    public void drawFixedItems(MainLayout layout) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (MapItem item : map.getStaticItems()) {
            for (Node n : item.getComponents()) {
                nodes.add(n);
            }
        }
        layout.getCanvas().getChildren().addAll(nodes);
    }


    public void drawMovingItems(MainLayout layout){
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<MapItem> items = map.getMovingItems();
        for (MapItem item : items){
            for (Node n : item.getComponents()) {
                if (simulationGUI.getCurrentStep() != 0) {
                    layout.getCanvas().getChildren().remove(layout.getCanvas().getChildren().size() - 1); // Remove old node
                }
                nodes.add(n);
            }
        }
        layout.getCanvas().getChildren().addAll(nodes);
    }

    public void setMap(GameMap map){
        this.map = map;
    }


    public GameMap getMap(){
        return this.map;
    }
    /**
     * Executes the loop to run the simulation
     */

    public SimulationGUI getSimulationGUI() {
        return simulationGUI;
    }

    public void setSimulationGUI(SimulationGUI simulationGUI) {
        this.simulationGUI = simulationGUI;
    }

}
