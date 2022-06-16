package controller;

import model.*;

import java.util.Arrays;
import java.util.Random;

/**
 * Class to generate random maps
 */
public final class MapGenerator {

    // Variables
    private final Random random;
    private final GameMap map;
    private final int sizeY;
    private final int sizeX;
    private boolean[][] mapPoints;
    private int[] guardsSpawnPoint;
    private int[] intrudersSpawnPoint;

    /**
     * Constructor
     * @param map
     */
    public MapGenerator(GameMap map) {
        this.map = map;
        sizeX = map.getSizeX();
        sizeY = map.getSizeY();
        random = new Random();
    }

    /**
     * Generates a new map and saves it to the instance variable "map".
     */
    public void generateMap() {
        mapPoints = new boolean[sizeX][sizeY];
        createBorderWalls();
        createSpawnArea(15, 16, true);
        createSpawnArea(15, 16, false);
        for (int i = 0; i < random.nextInt(3) + 3; i++)
            createWalledWDArea(3, 30);

        for (int i = 0; i < random.nextInt(3) + 3; i++)
            createWalledArea(3, 30);

        for (int i = 0; i < random.nextInt(3) + 1; i++)
            createTargetArea(10, 20);

        for (int i = 0; i < random.nextInt(3) + 1; i++)
            createShadedArea(10, 30);

        for (int i = 0; i < random.nextInt(3) + 1; i++)
            createSentryTower(5, 10);
        map.populateMap(GameController.amountOfGuards, GameController.amountOfIntruders);
    }

    private void createShadedArea(int min, int max) {
        var area = getRandomArea(min, max);
        var shadedArea = new ShadedArea(area[0], area[1], area[2], area[3]);
        map.addToMap(shadedArea);
    }

    private void createSpawnArea(int min, int max, boolean forGuards) {
        var area = getRandomArea(min, max);
        var spawnArea = new SpawnArea(forGuards, area[0], area[1], area[2], area[3]);
        if (forGuards) {
            map.setSpawnAreaGuards(spawnArea);
        }
        else {
            map.setSpawnAreaIntruders(spawnArea);
        }
        int[] centerPoint = {(area[0] + area[2]) / 2, (area[1] + area[3]) / 2};
        if (forGuards) {
            guardsSpawnPoint = centerPoint;
        } else {
            intrudersSpawnPoint = centerPoint;
        }
    }

    private void createTargetArea(int min, int max) {
        var area = getRandomArea(min, max);
        var targetArea = new TargetArea(area[0], area[1], area[2], area[3]);
        map.addToMap(targetArea);
    }

    private void createSentryTower(int min, int max) {
        var area = getRandomArea(min, max);
        var sentryTower = new SentryTower(area[0], area[1], area[2], area[3]);
        map.addToMap(sentryTower);
    }

    private void createWalledWDArea(int min, int max) {
        var area = getRandomArea(min, max);
        int x1 = area[0];
        int y1 = area[1];
        int x2 = area[2];
        int y2 = area[3];

        var northWall = new Wall(x1, y1, x2, y1 + 1);
        var southWall = new Wall(x1, y2, x2, y2 - 1);
        var westWall = new WallWithDoor(x1, y1, x1 + 1, y2, true);
        var eastWall = new WallWithWindow(x2, y1, x2 - 1, y2, true);
        map.addToMap(northWall);
        map.addToMap(southWall);
        map.addToMap(westWall);
        map.addToMap(eastWall);
    }

    private void createWalledArea(int min, int max) {
        var area = getRandomArea(min, max);
        int x1 = area[0];
        int y1 = area[1];
        int x2 = area[2];
        int y2 = area[3];

        var northWall = new Wall(x1, y1, x2, y1 );
        var southWall = new Wall(x1, y2, x2, y2);
        var westWall = new Wall(x1, y1, x2, y1+3);
        var eastWall = new Wall(x1, y2, x2, y2+3);
        map.addToMap(northWall);
        map.addToMap(southWall);
        map.addToMap(westWall);
        map.addToMap(eastWall);
    }

    private void createBorderWalls() {
        var northWall = new Wall(-2, -2, sizeX + 2, 0);
        var southWall = new Wall(-2, sizeY + 2, sizeX + 2, sizeY);
        var eastWall = new Wall(sizeX, -2, sizeX + 2, sizeY + 2);
        var westWall = new Wall(-2, -2, 0, sizeY + 2);
        map.addToMap(northWall);
        map.addToMap(southWall);
        map.addToMap(westWall);
        map.addToMap(eastWall);
    }

    private int[] getRandomArea(int min, int max) {
        int x1, y1, x2, y2;
        do {
            x1 = random.nextInt(sizeX - max);
            y1 = random.nextInt(sizeY - max);
            x2 = random.nextInt(max - min) + x1 + min;
            y2 = random.nextInt(max - min) + y1 + min;
        } while (areaNotEmpty(x1, y1, x2, y2));
        paintArea(x1, y1, x2, y2);
        return new int[]{x1, y1, x2, y2};
    }

    private boolean areaNotEmpty(int x1, int y1, int x2, int y2) {
        for (int x = x1; x <= x2; x++)
            for (int y = y1; y <= y2; y++)
                if (mapPoints[x][y]) return true;
        return false;
    }

    private void paintArea(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++)
                mapPoints[i][j] = true;
    }

}
