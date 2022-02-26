package Enities;

/**
 * This class represents the knowledge that an agent has about the current map.
 * It moves to a discrete space of 1x1 units. The vision of an agent constantly updates
 * the mapRepresentation.
 */
public class EntityKnowledge {

    // Variables
    int[][] mapRepresentation; // 0 = not discovered, 1 = empty, 2 = wall
    int relativePosX;
    int relativePosY;

    public EntityKnowledge() {
        mapRepresentation = new int[241][161]; //TODO: Make values dynamic
        // Fill with 0s;
        for (int i = 0; i < mapRepresentation.length; i++) {
            for (int j = 0; j < mapRepresentation[i].length; j++) {
                mapRepresentation[i][j] = 0;
            }
        }

        relativePosX = (mapRepresentation.length / 2) + 1;
        relativePosY = (mapRepresentation[0].length / 2) + 1;
    }
}
