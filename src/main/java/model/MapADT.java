package model;

import java.util.List;

/**
 * This class represents a Map where the simulation occurs.
 */
public class MapADT {

    // Variables
    int gameMode;
    int height;
    int width;
    double scaling;
    int numGuards;
    int numIntruders;
    double baseSpeedIntruder;
    double sprintSpeedIntruder;
    double baseSpeedGuard;
    double timeStep;
    int[] targetArea;
    int[] spawnAreaIntruders;
    int[] spawnAreaGuards;
    List<int[]> walls;
    int[] teleport;
    List<int[]> shadedAreas;
    int[] texture;

    public MapADT() {
        staticInit();
    }

    /**
     * Test-method to init a Map
     */
    public void staticInit() {
        gameMode = 0;
        height = 80;
        width = 120;
        scaling = 0.1;
        numGuards = 3;
        numIntruders = 0;

    }
}
