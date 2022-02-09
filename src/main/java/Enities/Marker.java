package Enities;

import model.*;

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
}
