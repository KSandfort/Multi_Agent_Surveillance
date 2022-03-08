package Enities;

import gui.ExplorationStage;
import model.Vector2D;

/**
 * This class represents the knowledge that an agent has about the current map.
 * It moves to a discrete space of 1x1 units. The vision of an agent constantly updates
 * the mapRepresentation.
 */
public class EntityKnowledge {

    // Variables
    int[][] mapRepresentation; // 0 = not discovered, 1 = empty, 2 = self, 3 = wall
    int relativePosX;
    int relativePosY;
    ExplorationStage explorationStage;

    public EntityKnowledge() {
        relativePosX = 0;
        relativePosY = 0;
        mapRepresentation = new int[241][161]; //TODO: Make values dynamic
        // Fill with 0s;
        for (int i = 0; i < mapRepresentation.length; i++) {
            for (int j = 0; j < mapRepresentation[i].length; j++) {
                mapRepresentation[i][j] = i + j;
            }
        }

        relativePosX = (mapRepresentation.length / 2) + 1;
        relativePosY = (mapRepresentation[0].length / 2) + 1;
    }

    public int[][] getMapRepresentation() {
        return mapRepresentation;
    }

    public void setMapRepresentation(int[][] mapRepresentation) {
        this.mapRepresentation = mapRepresentation;
    }

    public int getRelativePosX() {
        return relativePosX;
    }

    public void setRelativePosX(int relativePosX) {
        this.relativePosX = relativePosX;
    }

    public int getRelativePosY() {
        return relativePosY;
    }

    public void setRelativePosY(int relativePosY) {
        this.relativePosY = relativePosY;
    }

    /**
     * Sets an entry in the mapRepresentation.
     * @param value
     * @param position
     */
    public void setCell(int value, Vector2D position) {
        int targetX = translateX((int) Math.floor(position.getX()));
        int targetY = translateY((int) Math.floor(position.getY()));
        mapRepresentation[targetX][targetY] = value;
        // Update on screen
        if (this.explorationStage != null) {
            explorationStage.drawCell(value, targetX, targetY);
        }
    }

    public int translateX(int x) {
        return x + 120;
    }

    public int translateY(int y) {
        return y + 80;
    }

    public void setExplorationStage(ExplorationStage explorationStage) {
        this.explorationStage = explorationStage;
    }
}
