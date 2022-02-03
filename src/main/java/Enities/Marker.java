package Enities;

public class Marker {
    boolean isFromIntruder;
    MarkerType markerType;

    public Marker(MarkerType markerType, boolean isFromIntruder)
    {
        this.markerType = markerType;
        this.isFromIntruder = isFromIntruder;
    }

    public boolean compareMarker(boolean isIntruder, MarkerType type)
    {
        return (isIntruder == isFromIntruder && type == markerType);
    }
}
