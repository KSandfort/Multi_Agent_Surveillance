package Enities;

import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;
import model.*;
import java.util.ArrayList;

/**
 * Class that represents a marker.
 * Markers can be placed on a map by entities.
 */
@Getter
@Setter
public class Marker extends MapItem {

    // Variables
    private boolean isFromIntruder;
    private int markerType;
        // 0: Red
        // 1: Green
        // 2: Blue
        // 3: Yellow
        // 4: Purple
    private Vector2D position;
    private double intensity = 10;
    private int tickPlaced; // The time when it was placed on the map

    public Marker(int markerType,Vector2D position, boolean isFromIntruder)
    {
        this.markerType = markerType;
        this.isFromIntruder = isFromIntruder;
        this.position = position;
    }

    public boolean compareMarker(boolean isIntruder, int type)
    {
        return (isIntruder == isFromIntruder && type == markerType);
    }

    public Color getColor() {
        switch (markerType) {
            case 0: {
                return Color.RED;
            }
            case 1: {
                return Color.GREEN;
            }
            case 2: {
                return Color.BLUE;
            }
            case 3: {
                return Color.YELLOW;
            }
            case 4: {
                return Color.PURPLE;
            }
        }
        return Color.GRAY; // Error
    }

    @Override
    public ArrayList<Node> getComponents() {
        ArrayList<Node> components = new ArrayList<>();
        int offset = SimulationGUI.CANVAS_OFFSET;
        double sf = SimulationGUI.SCALING_FACTOR;

        Circle circle = new Circle(3);
        circle.setCenterX(position.getX() * sf + offset);
        circle.setCenterY(position.getY() * sf + offset);
        circle.setFill(getColor());

        components.add(circle);
        return components;
    }

    @Override
    public boolean isSolidBody() {
        return false;
    }

    @Override
    public boolean isDynamicObject() {
        return true;
    }

    @Override
    public boolean isTransparentObject() {
        return false;
    }

}
