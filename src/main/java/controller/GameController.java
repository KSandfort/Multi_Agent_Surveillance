package controller;

import Enities.*;
import gui.SimulationGUI;
import gui.sceneLayouts.MainLayout;
import gui.sceneLayouts.TrainLayout;
import javafx.scene.Group;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;
import model.GameMap;
import model.MapItem;
import model.Vector2D;
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
    private int currentStep;
    private GameMap map;
    private SimulationGUI simulationGUI;
    private int hasWonGame = 0; // 0 for game is not won, 1 for Guards have won, 2 for Intruders have won

    // whether all intruders need to have visited the target area to win
    private boolean allIntrudersMode = true;
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
    public static boolean terminalFeedback = false; // Displays information about the simulation in the terminal

    // Public
    public double fitnessGuards = 0;
    public double fitnessIntruders = 0;

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
            case 3: { // Direct map file
                map = MapReader.readMapFromFile(SimulationGUI.bypassPath, this);

                break;
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

        while (!finished) {
            controller.update();

            // Abort simulation upon win
            if (controller.hasWonGame > 0 || step > steps) {
                finished = true;
                if (controller.hasWonGame == 1) {
                    TrainLayout.guardWins++;
                } else if (controller.hasWonGame == 2) {
                    TrainLayout.intruderWins++;
                } else {
                    TrainLayout.draws++;
                }
            }
            step++;

        }

        TrainLayout.gameCount++;
        controller.fitnessGuards = controller.getFitnessGuards();
        controller.fitnessIntruders = controller.getFitnessIntruders();

        return controller;
    }

    /**
     * Does the update magic.
     */
    public void update() {
        ArrayList<MapItem> items = map.getMovingItems();
        ArrayList<Intruder> toKill = new ArrayList<>();

        ArrayList<MapItem> itemsToCheck = (ArrayList<MapItem>) map.getStaticItems().clone();
        itemsToCheck.addAll(map.getSolidBodies());

        for(MapItem item : items) {
            item.update(itemsToCheck);

            // kill intruder
            if (item instanceof Intruder){
                if (!((Intruder) item).isAlive()){
                    toKill.add((Intruder)item);
                }
            }
        }
        items.removeAll(toKill);

        ArrayList<Marker> toRemove = new ArrayList<>();
        for(Marker marker : map.getMarkers()){
                if(marker.getIntensity() < 0.0001){
                    toRemove.add(marker);
                } else {
                    marker.setIntensity(marker.getIntensity() * 0.95);
                }
        }
        map.getMarkers().removeAll(toRemove);
        updateWinningCondition();
        explorationOverTime.add(coveragePercent);
        previousCoveragePercent = coveragePercent;
        // Print to terminal if wanted
        if (terminalFeedback && getCurrentStep() % 60 == 0) {
            System.out.println("Simulation is running at step: " + getCurrentStep() + " (" + getCurrentStep()/60 + ")");
            System.out.println(map.getMarkers().size() + " markers");
        }
        if (TrainLayout.active) {
            TrainLayout.currentStep.setValue((double) currentStep);
        }
        currentStep++;
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
    public void updateWinningCondition() {
        if (simulationGUI != null) {
            if (simulationGUI.getStartLayout() != null) {
                gameMode = simulationGUI.getStartLayout().getGameMode(); // 0 = exploration, 1 = guards vs intruders
            }
            else {
                gameMode = 1;
            }
        }

        if (coveragePercent == previousCoveragePercent) {
            noCoverageProgressSince++;
        } else {
            noCoverageProgressSince = 0;
        }

        if (gameMode == 0) {
            if (noCoverageProgressSince >= coverageThreshold) {
                hasWonGame = 1;
            }
        } else {
            ArrayList<MapItem> entities = map.getMovingItems();
            for (MapItem entity : entities) {
                Entity currentEntity = (Entity) entity;
                if (currentEntity instanceof Guard) {
                    if (currentEntity.checkWinningCondition()) {
                        hasWonGame = 1;
                        break;
                    }
                }
                if (currentEntity instanceof Intruder) {
                    if (currentEntity.checkWinningCondition()) {
                        hasWonGame = 2;
                        break;
                    }
                }
            }
        }

        if (hasWonGame == 0) {
            return;
        }
        if (gameMode == 0) {
            if (terminalFeedback)
                System.out.println("Game Over. Maximum coverage reached! " + coveragePercent);
            // Write exploration over time to file
            try {
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
            } catch (Exception e) {
                System.out.println("Write error");
            }
        } else if (gameMode == 1) {
            String winner = (hasWonGame == 1 ? "Guards" : "Intruders");
            // System.out.println(winner + " won!");


        }
        if (terminalFeedback) {
            System.out.println("Fitness Guards:    " + getFitnessGuards());
            System.out.println("Fitness Intruders: " + getFitnessIntruders());
        }
        if (simulationGUI != null)
            simulationGUI.stopSimulation();
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
     * Computes the fitness for training guard agents with the following factors:
     *
     *  - Exploration coverage
     *  - Distance paced by the guards
     *  - Number of intruders caught
     *  - Game won
     *
     *  All these attributes are condensed into a [0, 1] fitness score.
     *
     * @return [0, 1] value for the fitness
     */
    public double getFitnessGuards() {
        double fitness;

        int fitnessIntrudersKilled = 0;
        for (MapItem item : map.getMovingItems()) {
            if (item instanceof Guard) {
                fitnessIntrudersKilled += ((Guard) item).getKillCount();
            }
        }

        double fitnessWon = (hasWonGame == 1 ? 1 : 0);
        // Sum the fitness attributes
        fitness = (
            getCoveragePercent() +
            fitnessIntrudersKilled +
            fitnessWon
        );
        return fitness;
    }

    /**
     * Computes the fitness for training intruder agents with the following factors:
     *
     *  - Average distance (of all intruders) to the target
     *  - Minimum distance to the target (whichever intruder is closest)
     *  - Game won
     *
     *  All these attributes are condensed into a [0, 1] fitness score.
     *
     * @return [0, 1] value for the fitness
     */
    public double getFitnessIntruders() {
        double fitness, fitnessWon, fitnessAvgDistance, fitnessMinDistance;

        double mapNormalizationFactor = Vector2D.distance(new Vector2D(0, 0), new Vector2D(map.getSizeX(), map.getSizeY()));

        fitnessAvgDistance = 0;
        fitnessMinDistance = mapNormalizationFactor;

        double fitnessIntrudersKilled = 0;

        for (MapItem item : map.getMovingItems()) {
            if (item instanceof Intruder) {
                double distance = Vector2D.distance(item.getPosition(),map.getTargetArea().getPosition());
                fitnessMinDistance = Math.min(fitnessMinDistance, distance);

                fitnessAvgDistance += distance;
            }

            if (item instanceof Guard) {
                fitnessIntrudersKilled += ((Guard) item).getKillCount();
            }
        }
        fitnessAvgDistance /= amountOfIntruders;
        fitnessIntrudersKilled = 1 - fitnessIntrudersKilled/amountOfIntruders;
        fitnessAvgDistance = 1 - (fitnessAvgDistance / mapNormalizationFactor);
        fitnessMinDistance = 1 - (fitnessMinDistance / mapNormalizationFactor);
        fitnessWon = (hasWonGame == 2 ? 1 : 0);
        // Add & normalize the fitness attributes
        fitness = (
            fitnessAvgDistance +
            fitnessMinDistance +
            fitnessIntrudersKilled +
            fitnessWon
        ) / 4;
        return fitness;
    }

    /**
     * Returns the fitness.
     * @param agent_type
     * @return
     */
    public double getFitness(int agent_type) {
        if (agent_type == 0)    // guard
            return getFitnessGuards();
        if (agent_type == 1)
            return getFitnessIntruders();
        return 0;
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

    /**
     * Run to start a simulation with given parameters and without any GUI.
     * @param args
     */
    public static void main(String [] args){
        // Pass Integer.MAX_VALUE as the "steps" parameter for indefinite simulation (terminates upon game over)
        GameController.simulate(2000,3,2,0,1);
    }
}
