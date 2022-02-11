package Enities;

import javafx.scene.Node;
import model.*;

import java.util.ArrayList;

public class Marker extends MapItem {
    boolean isFromIntruder;
    MarkerType markerType;
    Vector2D position;
    public Marker(MarkerType markerType,Vector2D position, boolean isFromIntruder)
    {
        this.markerType = markerType;
        this.isFromIntruder = isFromIntruder;
        this.position = position;
    }

    public boolean compareMarker(boolean isIntruder, MarkerType type)
    {
        return (isIntruder == isFromIntruder && type == markerType);
    }

    @Override
    public ArrayList<Node> getComponents() {
        return null;
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
    public boolean isStaticObject() {
        return false;
    }

}
