package model;

import Enities.Guard;
import Enities.Intruder;

import java.util.Arrays;
import java.util.Random;

public final class MapGenerator {

    private final Random random;
    private final GameMap map;
    private final int sizeY;
    private final int sizeX;
    private boolean[][] mapPoints;
    private int spawnX;
    private int spawnY;

    public MapGenerator(GameMap map) {
        this.map = map;
        sizeX = map.getSizeX();
        sizeY = map.getSizeY();
        random = new Random();
    }

    public void generateMap() {
        mapPoints = new boolean[sizeX][sizeY];
        createBorderWalls();

        //createSpawnArea(15, 16);

        for (int i = 0; i < random.nextInt(1, 4); i++)
            createWalledArea(15, 30);

      //  for (int i = 0; i < random.nextInt(1, 3); i++)
      //      createTargetArea(10, 20);

        for (int i = 0; i < random.nextInt(1, 3); i++)
            createShadedArea(10, 30);

        for (int i = 0; i < random.nextInt(1, 3); i++)
            createSentryTower(5, 10);

        generateEntities(3, 2);
    }

    private void generateEntities(int guards, int intruders) {
        for (int i = 0; i <= guards; i++) {
            int[] point = getRandomPoint();
            map.addToMap(new Guard(point[0], point[1]));
        }
        for (int i = 0; i <= intruders; i++) {
            map.addToMap(new Intruder(spawnX, spawnY));
        }
    }

    private void createShadedArea(int min, int max) {
        var area = getRandomArea(min, max);
        var shadedArea = new ShadedArea(area[0], area[1], area[2], area[3]);
        map.addToMap(shadedArea);
    }
    /**
    private void createSpawnArea(int min, int max) {
        var area = getRandomArea(min, max);
        var spawnArea = new SpawnArea(area[0], area[1], area[2], area[3]);
        spawnX = (area[2] + area[0]) / 2;
        spawnY = (area[3] + area[1]) / 2;
        System.out.println(Arrays.toString(area));
        map.addToMap(spawnArea);
    }
    **/
/**
    private void createTargetArea(int min, int max) {
        var area = getRandomArea(min, max);
        var targetArea = new TargetArea(area[0], area[1], area[2], area[3]);
        map.addToMap(targetArea);
    }**/

    private void createSentryTower(int min, int max) {
        var area = getRandomArea(min, max);
        var sentryTower = new SentryTower(area[0], area[1], area[2], area[3]);
        map.addToMap(sentryTower);
    }

//    private void createTeleportArea(int min, int max) {
//        var pos = getRandomArea(min, max);
//        var teleportArea = new TeleportArea(pos[0], pos[1], pos[2], pos[3]);
//        map.addToMap(teleportArea);
//    }

    private void createWalledArea(int min, int max) {
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

            x2 = random.nextInt(x1 + min, x1 + max);
            y2 = random.nextInt(y1 + min, y1 + max);
        } while (areaNotEmpty(x1, y1, x2, y2));
        paintArea(x1, y1, x2, y2);

        return new int[]{x1, y1, x2, y2};
    }

    private int[] getRandomPoint() {
        int x, y;
        do {
            x = random.nextInt(sizeX);
            y = random.nextInt(sizeY);
        } while (areaNotEmpty(x, y, x, y));
        return new int[]{x, y};
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

    private void printPoints() {
        for (boolean[] row : mapPoints)
            System.out.println(Arrays.toString(row));
    }
}
