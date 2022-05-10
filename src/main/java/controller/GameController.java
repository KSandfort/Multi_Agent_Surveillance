package controller;

import Enities.*;
import gui.SimulationGUI;
import gui.sceneLayouts.MainLayout;
import javafx.scene.Group;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;
import model.GameMap;
import model.MapItem;
import utils.MapReader;

import java.io.FileWriter;
import java.util.ArrayList;

/**
 * This class acts as the heart of the game. It controls all the parts
 * and is the point where GUI and back end come together.
 */
@Getter
@Setter
public class GameController {

    // Variables
    private GameMap map;
    private SimulationGUI simulationGUI;
    private int hasWonGame = 0; // 0 for game is not won, 1 for Intruders have won, 2 for guards have won
    private boolean[][] coverageMatrix; // 0 = not explored, 1 = explored
    private double maximumCoveragePossible; // how much of the map can actually be explored by the agents
    private double coveragePercent; // Coverage value in percent (from 0 to 1)
    private double previousCoveragePercent; // One time-step earlier
    private int coverageNumerator; // Amount of explored cells
    private int coverageDenominator; // Total amount of cells
    private int noCoverageProgressSince; // Indicates for how many time-steps there hasn't been progress in the coverage
    private int coverageThreshold = 500; // Tolerance of not making progress in coverage until game stops.
    private Group notChangingNodes; // Walls etc.
    private Group changingNodes; // Entities, markers, etc.
    private int gameMode;
    // Static
    public static int amountOfGuards;
    public static int amountOfIntruders;
    public static int guardAgentType = 0;
    public static int intruderAgentType = 0;
    // 0 = random, 1 = remote, ...

    private ArrayList<Double> explorationOverTime = new ArrayList<>();

    /**
     * Constructor
     * @param gui Simulation GUI
     * @param mapCode The unique identifier for a specific map type (0 = test map, 1 = text file map, 2 = random)
     *
     */
    public GameController(SimulationGUI gui, int mapCode) {
        this.simulationGUI = gui;
        GameMap map = new GameMap(this);
        switch (mapCode) { // 0 = test map, 1 = read from file
            case 0: {
                map.initTestGameMap();
                map.populateMap(amountOfGuards, amountOfIntruders);
                break;
            }
            case 1: {
                String selectedMap = (String) gui.getStartLayout().getMapListBox().getValue();
                map = MapReader.readMapFromFile("src/main/resources/maps/" + selectedMap, this);
                break;
            }
            case 2: {
                map.setSizeX(120);
                map.setSizeY(80);
                MapGenerator mapGenerator = new MapGenerator(map);
                mapGenerator.generateMap();
            }
            default: {
                System.out.println("ERROR! No map generated!");
                System.exit(1);
                break;
            }
        }

        coverageMatrix = new boolean[map.getSizeX()][map.getSizeY()];
        // denominator = amount of units that can be explored
        coverageDenominator = map.calculateMaximalPossibleCoverage();

        notChangingNodes = new Group();
        changingNodes = new Group();

        this.map = map;
    }

    // TODO: Probably needs more arguments like agent algorithms
    /**
     * Simulates a run without GUI
     * @param steps Maximum number of steps that the simulation should run
     * @param numGuards Amount of guards
     * @param numIntruders Amount of intruders
     * @param mapCode ID of the map to be used in the simulation
     * @param gameMode Game mode to be simulated (0 = exploration, 1 = guards vs intruders)
     */
    public static GameController simulate(int steps, int numGuards, int numIntruders, int mapCode, int gameMode) {
        amountOfGuards = numGuards;
        amountOfIntruders = numIntruders;

        GameController controller = new GameController(null, mapCode);
        controller.setGameMode(gameMode);

        boolean finished = false;
        int step = 0;

        while (!finished){
            controller.update();

            // Abort simulation upon win
            if (controller.hasWonGame > 0 || step > steps)
                finished = true;

            step++;
        }

        return controller;
    }

    /**
     * Does the update magic.
     */
    public void update() {
        ArrayList<MapItem> items = map.getMovingItems();

        // use static & dynamic objects when updating
        ArrayList<MapItem> itemsToCheck = map.getStaticItems();
        itemsToCheck.addAll(items);

        for(MapItem item : items) {
            item.update(itemsToCheck);
        }

        ArrayList<Marker> toRemove = new ArrayList<>();
        for(Marker marker : map.getMarkers()){
            // TODO update marker intensity
                if(marker.getIntensity() < 0.0001){
                    toRemove.add(marker);
                }else{
                    marker.setIntensity(marker.getIntensity() * 0.95);
                }
        }
        map.getMarkers().removeAll(toRemove);

        updateWinningCondition(); //TODO stop game if winning condition hasWonGame is not 0

        explorationOverTime.add(coveragePercent);

        previousCoveragePercent = coveragePercent;
    }

    public void updateAgentTargetDirection(){

    }

    /**
     * Updates the total coverage depending on the knowledge gain of a guard.
     * @param x x-pos
     * @param y y-pos
     * @param explored 1 if the cell is marked as explored
     */
    public void updateCoverage(int x, int y, boolean explored) {
        if (!coverageMatrix[x][y]) {
            coverageMatrix[x][y] = explored;
            coverageNumerator++;
            // Paint to coverage canvas

            if (simulationGUI != null)
                simulationGUI.getMainLayout().addCoveragePoint(x, y, explored);
        }
        // Calculate percentage
        coveragePercent = (double) coverageNumerator / (double) coverageDenominator;

        if (simulationGUI != null){
            simulationGUI.getMainLayout().getCoverageBar().setProgress(coveragePercent);
            simulationGUI.getMainLayout().getCoverageText().setText(Math.round(coveragePercent*10000) / 100.0 + " %");
        }


    }

    /**
     * For debug purposes:
     * Prints the coverage to the terminal
     */
    private void printCoverage() {
        System.out.println("--- Game Controller Guard Coverage: ---");
        for (int i = 0; i < coverageMatrix[0].length; i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < coverageMatrix.length; j++) {
                char currChar = '-';
                if (coverageMatrix[j][i]) {
                    currChar = 'X';
                }
                System.out.print(currChar);
            }
            System.out.println();
        }
    }

    /**
     * for gameMode 0 the winning condition will be determined based on the exploration
     * factor, how much of the map the agents have explored
     * in gameMode 1, the intruders win, if all of them reach the target area
     * the guards win, if they manage to capture the intruders before they win
     */
    public void updateWinningCondition(){
        if (simulationGUI != null){
            gameMode = simulationGUI.getStartLayout().getGameMode(); // 0 = exploration, 1 = guards vs intruders
        }

        if (coveragePercent == previousCoveragePercent) {
            noCoverageProgressSince++;
        }
        else {
            noCoverageProgressSince = 0;
        }

        if(gameMode == 0) {
            if (noCoverageProgressSince >= coverageThreshold){
                hasWonGame = 1;
            }
        } else{
            ArrayList<MapItem> entities = map.getMovingItems();
            for (MapItem entity : entities){
                Entity currentEntity = (Entity) entity;
                if (currentEntity instanceof Guard){
                    if(currentEntity.checkWinningCondition()){
                        hasWonGame = 2; return;
                    }
                } if (currentEntity instanceof Intruder){
                    if(! currentEntity.checkWinningCondition()){
                        hasWonGame = 0; return;
                    }
                }
            }
            hasWonGame = 1;
        }
        if (hasWonGame == 1) {
            System.out.println("Game Over. Maximum coverage reached! " + coveragePercent);

            // Write exploration over time to file
            try {
                // TODO - remove this (or put it somewhere cleaner in Phase 2)
                String s = "Random";
                if (GameController.guardAgentType == 1)
                    s = "Remote";
                else if (GameController.guardAgentType == 2)
                    s = "Bug";

                FileWriter writer = new FileWriter("output/coverage_output_" + s + ".txt");
                int i = 0;
                for (Double percent : explorationOverTime) {
                    writer.write(i++ + " " + percent + System.lineSeparator());
                }
                writer.close();
            } catch(Exception e) {
                System.out.println("Write error");
            }
            if (simulationGUI != null){
                simulationGUI.pauseSimulation();
            }
        }
    }

    /**
     * Draws fixed components on the map.
     * @param layout
     */
    public void drawFixedItems(MainLayout layout) {
        for (MapItem item : map.getStaticItems()) {

            for (Node n : item.getComponents()) {
                notChangingNodes.getChildren().add(n);
            }
        }
        for (MapItem item : map.getSolidBodies()) {
            // Don't draw entities as solid objects despite being marked as solid
            // (otherwise the GUI shows static copies of agents)
            if (item instanceof Guard || item instanceof Intruder)
                continue;

            for (Node n : item.getComponents()) {
                notChangingNodes.getChildren().add(n);
            }

        }
        layout.getCanvas().getChildren().add(notChangingNodes);
        layout.getCanvas().getChildren().add(changingNodes);
    }

    /**
     * Draws all the moving items of the map.
     * @param layout
     */
    public void drawMovingItems(MainLayout layout){
        // Clear old moving nodes
        changingNodes.getChildren().clear();
        // Add new ones
        for (MapItem item : map.getMovingItems()) {
            changingNodes.getChildren().addAll(item.getComponents());
        }
        for (Marker m : map.getMarkers()) {
            changingNodes.getChildren().addAll(m.getComponents());
        }
    }


    public static void main(String [] args){
        // Pass Integer.MAX_VALUE as the "steps" parameter for indefinite simulation (terminates upon game over)
        GameController.simulate(Integer.MAX_VALUE,3,2,0,0);
    }
}
