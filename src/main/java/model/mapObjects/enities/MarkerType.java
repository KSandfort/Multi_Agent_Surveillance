package model.mapObjects.enities;

public enum MarkerType {
    Red(1),Green(2),Blue(3),Yellow(4),Purple(5);

    int index;

    MarkerType(int index)
    {
        this.index = index;

    }

    public int getIndex() {
        return index;
    }
}
