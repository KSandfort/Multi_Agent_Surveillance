package utils;

import controller.GameController;
import gui.SimulationGUI;
import model.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Translates a text file to a map object.
 */
public class MapReader {

    // Variables
    public static final boolean MAP_READER_DEBUG = true;

    /**
     * Returns a new map based on the properties given by a .txt file
     * @param path path to input file
     * @return new map instance
     */
    public static GameMap readMapFromFile(String path, GameController controller) {
        if (MAP_READER_DEBUG) System.out.println("--- Map Reader Debug: ---");
        GameMap gameMap = new GameMap(controller);
        int numGuards = 0;
        int numIntruders = 0;

        try {
            File mapFile = new File(path);
            Scanner sc = new Scanner(mapFile);
            while (sc.hasNextLine()) {
                String currentLine = sc.nextLine();
                String[] words = currentLine.split(" ");
                switch (words[0]) {
                    case "name": {
                        // TODO: Add name to GameMap
                        break;
                    }
                    case "gameMode": {
                        // TODO: Set gameMode automatically
                        if (MAP_READER_DEBUG) System.out.println("To be done!");
                        break;
                    }
                    case "height": {
                        gameMap.setSizeY(Integer.parseInt(words[2]));
                        if (MAP_READER_DEBUG) System.out.println("Height: " + gameMap.getSizeY());
                        break;
                    }
                    case "width": {
                        gameMap.setSizeX(Integer.parseInt(words[2]));
                        if (MAP_READER_DEBUG) System.out.println("Width: " + gameMap.getSizeX());
                        break;
                    }
                    case "scaling": {
                        SimulationGUI.SCALING_FACTOR = 1 / Double.parseDouble(words[2]);
                        if (MAP_READER_DEBUG) System.out.println("Scaling: " + SimulationGUI.SCALING_FACTOR);
                        break;
                    }
                    case "numGuards": {
                        numGuards = Integer.parseInt(words[2]);
                        break;
                    }
                    case "numIntruders": {
                        numIntruders = Integer.parseInt(words[2]);
                        break;
                    }
                    case "spawnAreaGuards": {
                        double x1 = Double.parseDouble(words[2]);
                        double y1 = Double.parseDouble(words[3]);
                        double x2 = Double.parseDouble(words[4]);
                        double y2 = Double.parseDouble(words[5]);
                        gameMap.setSpawnAreaGuards(new SpawnArea(true, x1, y1, x2, y2));
                        break;
                    }
                    case "spawnAreaIntruders": {
                        double x1 = Double.parseDouble(words[2]);
                        double y1 = Double.parseDouble(words[3]);
                        double x2 = Double.parseDouble(words[4]);
                        double y2 = Double.parseDouble(words[5]);
                        gameMap.setSpawnAreaIntruders(new SpawnArea(false, x1, y1, x2, y2));
                        break;
                    }
                    case "wall": {
                        gameMap.addToMap(new Wall(
                                Double.parseDouble(words[2]),
                                Double.parseDouble(words[3]),
                                Double.parseDouble(words[4]),
                                Double.parseDouble(words[5])));
                        break;
                    }
                    case "teleport": {
                        double x1 = Double.parseDouble(words[2]);
                        double y1 = Double.parseDouble(words[3]);
                        double x2 = Double.parseDouble(words[4]);
                        double y2 = Double.parseDouble(words[5]);
                        double xt = Double.parseDouble(words[6]);
                        double yt = Double.parseDouble(words[7]);
                        double angle;
                        
                        Teleport tp = new Teleport(x1, y1, x2, y2, xt, yt, 1, 0);
                        //TODO: correct angle
                        gameMap.addToMap(tp);
                        if (MAP_READER_DEBUG) System.out.println("Teleport added!");
                        break;
                    }
                    case "shaded": {
                        double x1 = Double.parseDouble(words[2]);
                        double y1 = Double.parseDouble(words[3]);
                        double x2 = Double.parseDouble(words[4]);
                        double y2 = Double.parseDouble(words[5]);
                        ShadedArea shadedArea = new ShadedArea(x1, y1, x2, y2);
                        gameMap.addToMap(shadedArea);
                        if (MAP_READER_DEBUG) System.out.println("Shaded area added!");
                        break;
                    }
                    case "texture": {
                        //TODO: add after implementing texture area
                        if (MAP_READER_DEBUG) System.out.println("Texture areas not yet implemented!");
                        break;
                    }
                    default: {
                        if (MAP_READER_DEBUG) System.out.println("Keyword " + words[0] + " is not defined.");
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find map file!");
            e.printStackTrace();
        }
        gameMap.populateMap(numGuards, numIntruders);
        gameMap.createBorderWalls();
        return gameMap;
    }
}
