package Enities;

import gui.ExplorationStage;
import lombok.Getter;
import lombok.Setter;
import model.GameMap;
import model.Vector2D;

/**
 * This class represents the knowledge that an agent has about the current map.
 * It moves to a discrete space of 1x1 units. The vision of an agent constantly updates
 * the mapRepresentation.
 */
@Getter
@Setter
public class EntityKnowledge {

    // Variables
    private int[][] mapRepresentation; // 0 = not discovered, 1 = empty, 2 = self, 3 = wall
    private int relativePosX;
    private int relativePosY;
    private ExplorationStage explorationStage;
    private Vector2D positionOffset; // So that the agent doesn't know its absolute position.
    private GameMap map;
    private int width;
    private int height;
    private boolean isGuard;

    /**
     * Constructor
     */
    public EntityKnowledge(GameMap gameMap, boolean isGuard) {
        this.isGuard = isGuard;
        relativePosX = 0;
        relativePosY = 0;
        this.map = gameMap;
        width = gameMap.getSizeX();
        height = gameMap.getSizeY();
        mapRepresentation = new int[width * 2 + 1][height * 2 + 1];
        // Fill with 0s;
        for (int i = 0; i < mapRepresentation.length; i++) {
            for (int j = 0; j < mapRepresentation[i].length; j++) {
                mapRepresentation[i][j] = 0;
            }
        }
        relativePosX = (mapRepresentation.length / 2) + 1;
        relativePosY = (mapRepresentation[0].length / 2) + 1;
    }

    /**
     * Sets an entry in the mapRepresentation.
     * @param value numeric value corresponding to the type of cell
     * @param position Vector2D of the target position
     */
    public void setCell(int value, Vector2D position) {
        int targetX = translateX((int) Math.floor(position.getX()));
        int targetY = translateY((int) Math.floor(position.getY()));
        mapRepresentation[targetX][targetY] = value;
        // Update on screen
        if (this.explorationStage != null) {
            explorationStage.drawCell(value, targetX, targetY);
        }
        // Update map coverage of the game controller
        if (isGuard && value != 0) {
            int x = (int) Math.floor(position.getX());
            int y = (int) Math.floor(position.getY());
            // Detect boundaries:
            if (x >= 0 && x < map.getSizeX() && y >= 0 && y < map.getSizeY())
            map.getGameController().updateCoverage(x, y, true);
        }
    }

    /**
     * Translates a continuous x-coordinate into a discrete cell x-position for the knowledge array.
     * @param x
     * @return
     */
    public int translateX(int x) {
        return x + width - (int) positionOffset.getX();
    }

    /**
     * Translates a continuous y-coordinate into a discrete cell y-position for the knowledge array.
     * @param y
     * @return
     */
    public int translateY(int y) {
        return y + height - (int) positionOffset.getY();
    }

}
