package controller;

import Enities.Entity;
import Enities.EntityKnowledge;
import Enities.Guard;
import Enities.Intruder;
import gui.SimulationGUI;
import gui.sceneLayouts.MainLayout;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;
import model.GameMap;
import model.MapItem;
import utils.MapReader;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;

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
    private double coveragePercent; // Coverage value in percent (from 0 to 1)
    private double previousCoveragePercent; // One time-step earlier
    private int coverageNumerator; // Amount of explored cells
    private int coverageDenominator; // Total amount of cells
    private int noCoverageProgressSince; // Indicates for how many time-steps there hasn't been progress in the coverage
    private int coverageThreshold = 500; // Tolerance of not making progress in coverage until game stops.

    // Static
    public static int amountOfGuards;
    public static int amountOfIntruders;
    public static int guardAgentType = 0;
    public static int intruderAgentType = 0;
    // 0 = random, 1 = remote, ...

    private ArrayList<Double> explorationOverTime = new ArrayList<>();

    /**
     * Constructor
     * @param gui
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
        coverageDenominator = map.getSizeX() * map.getSizeY();

        this.map = map;
    }

    /**
     * Does the update magic.
     */
    public void update() {
        ArrayList<MapItem> items = map.getMovingItems();
        for(MapItem item : items) {
            item.update(map.getStaticItems());
        }
        updateWinningCondition(); //TODO stop game if winning condition hasWonGame is not 0

        explorationOverTime.add(coveragePercent);

        previousCoveragePercent = coveragePercent;
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
            simulationGUI.getMainLayout().addCoveragePoint(x, y, explored);
        }
        // Calculate percentage
        coveragePercent = (double) coverageNumerator / (double) coverageDenominator;
        simulationGUI.getMainLayout().getCoverageBar().setProgress(coveragePercent);
        simulationGUI.getMainLayout().getCoverageText().setText(Math.round(coveragePercent*10000) / 100.0 + " %");

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
        int gameMode = simulationGUI.getStartLayout().getGameMode(); // 0 = exploration, 1 = guards vs intruders

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
            System.out.println("Game Over. Maximum coverage reached!");

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

            simulationGUI.pauseSimulation();
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

}
