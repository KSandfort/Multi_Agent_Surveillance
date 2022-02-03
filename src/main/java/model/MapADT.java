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
    double timeStep;
    int[] targetArea;

    // Guards
    int numGuards;
    double baseSpeedGuard;
    int[] spawnAreaGuards;

    // Intruders
    int numIntruders;
    double baseSpeedIntruder;
    double sprintSpeedIntruder;
    int[] spawnAreaIntruders;


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
        baseSpeedIntruder = 14.0;
        sprintSpeedIntruder = 20.0;
        baseSpeedGuard = 14.0;
        timeStep = 0.1;
        targetArea = new int[]{20, 40, 25, 45};
        spawnAreaIntruders = new int[]{2, 2, 20, 10};
        spawnAreaGuards = new int[]{2, 2, 20, 10};
    }

    // Getters and Setters

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getScaling() {
        return scaling;
    }

    public void setScaling(double scaling) {
        this.scaling = scaling;
    }

    public double getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }

    public int[] getTargetArea() {
        return targetArea;
    }

    public void setTargetArea(int[] targetArea) {
        this.targetArea = targetArea;
    }

    public int getNumGuards() {
        return numGuards;
    }

    public void setNumGuards(int numGuards) {
        this.numGuards = numGuards;
    }

    public double getBaseSpeedGuard() {
        return baseSpeedGuard;
    }

    public void setBaseSpeedGuard(double baseSpeedGuard) {
        this.baseSpeedGuard = baseSpeedGuard;
    }

    public int[] getSpawnAreaGuards() {
        return spawnAreaGuards;
    }

    public void setSpawnAreaGuards(int[] spawnAreaGuards) {
        this.spawnAreaGuards = spawnAreaGuards;
    }

    public int getNumIntruders() {
        return numIntruders;
    }

    public void setNumIntruders(int numIntruders) {
        this.numIntruders = numIntruders;
    }

    public double getBaseSpeedIntruder() {
        return baseSpeedIntruder;
    }

    public void setBaseSpeedIntruder(double baseSpeedIntruder) {
        this.baseSpeedIntruder = baseSpeedIntruder;
    }

    public double getSprintSpeedIntruder() {
        return sprintSpeedIntruder;
    }

    public void setSprintSpeedIntruder(double sprintSpeedIntruder) {
        this.sprintSpeedIntruder = sprintSpeedIntruder;
    }

    public int[] getSpawnAreaIntruders() {
        return spawnAreaIntruders;
    }

    public void setSpawnAreaIntruders(int[] spawnAreaIntruders) {
        this.spawnAreaIntruders = spawnAreaIntruders;
    }

    public List<int[]> getWalls() {
        return walls;
    }

    public void setWalls(List<int[]> walls) {
        this.walls = walls;
    }

    public int[] getTeleport() {
        return teleport;
    }

    public void setTeleport(int[] teleport) {
        this.teleport = teleport;
    }

    public List<int[]> getShadedAreas() {
        return shadedAreas;
    }

    public void setShadedAreas(List<int[]> shadedAreas) {
        this.shadedAreas = shadedAreas;
    }

    public int[] getTexture() {
        return texture;
    }

    public void setTexture(int[] texture) {
        this.texture = texture;
    }
}
