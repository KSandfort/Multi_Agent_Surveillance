package model;

public class MapItem {
    Vector2D position = new Vector2D();
    GameMap map = new GameMap();
    public Vector2D getPosition(){
        return position;
    };
    public void setPosition(Vector2D position){
        this.position = position;
    };
}
