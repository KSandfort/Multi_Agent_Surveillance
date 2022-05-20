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
        double opacity = Math.pow(intensity * 0.1, (double) 1/3);
        switch (markerType) {
            case 0: {
                return Color.rgb(255, 0, 0, opacity);
            }
            case 1: {
                return Color.rgb(0, 255, 0, opacity);
            }
            case 2: {
                return Color.rgb(0, 0, 255, opacity);
            }
            case 3: {
                return Color.rgb(255, 255, 0, opacity);
            }
            case 4: {
                return Color.rgb(255, 0, 255, opacity);
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
    public void onAgentCollision(Entity entity) {

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
