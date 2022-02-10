package utils;

import model.GameMap;
import model.MapItem;
import model.Vector2D;

/**
 * This class handles ray tracing.
 */
public class RayTracing {

    /**
     * Determines the length of a ray in the simulation.
     * The length is bounded to a maximum that is given as a parameter.
     * @param position
     * @param direction
     * @param currentMap current map that is evaluated with all the items that it is containing
     * @param maxLength equivalent to the viewing
     * @return
     */
    public static double getRayLength(Vector2D position, Vector2D direction, GameMap currentMap, double maxLength) {
        // Iterate over all items on the map and see if the given ray hits an object.
        // Needs to find the CLOSEST object from the initial position.
        // TODO: implement lol
        return 0;
    }
}
