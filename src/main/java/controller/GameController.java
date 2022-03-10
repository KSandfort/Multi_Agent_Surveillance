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
    boolean hasWonGame = false; // todo change this to 0,1,2 ???
    double coverageThreshold;

    // TEMPORARY - for testing purpose of the title screen
    static public int amountOfGuards;
    static public int amountOfIntruders;

    /**
     * Constructor
     * @param gui
     */
    public GameController(SimulationGUI gui) {
        this.simulationGUI = gui;
        GameMap map = new GameMap(this);
        this.map = map;
        //TEMPORARY -- for testing purposes
        this.map.initTestGameMap();
        this.map.populateMap(amountOfGuards, amountOfIntruders, 1, 0);
    }

    /**
     * Does the update magic.
     */
    public void update() {
        ArrayList<MapItem> items = map.getMovingItems();
        for(MapItem item : items) {
            item.update(map.getStaticItems());
        }
        updateWinningCondition();
    }

    // TODO
    /**
     * for gameMode 0 the winning condition will be determined based on the exploration
     * factor, how much of the map the agents have explored
     * in gameMode 1, the intruders win, if all of them reach the target area
     * the guards win, if they manage to capture the intruders before they win
     */
    public void updateWinningCondition(){
        int gameMode = simulationGUI.getGameMode(); // 0 = exploration, 1 = guards vs intruders

        if(gameMode == 0) {
            if (computeCoverage() >= coverageThreshold){
                hasWonGame = true;
            }
        } else{
            // TODO get winning condition from entities
        }

    }


    public double computeCoverage(){
        //TODO compute total coverage of the agent
        return 0;
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
        for (MapItem item : map.getSolidBodies()) {
            for (Node n : item.getComponents()) {
                nodes.add(n);
            }
        }
        layout.getCanvas().getChildren().addAll(nodes);
    }

    /**
     * Draws all the moving items of the map.
     * @param layout
     */
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

    public SimulationGUI getSimulationGUI() {
        return simulationGUI;
    }

}
