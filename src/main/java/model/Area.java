package model;

import Enities.BaseEntity;

public class Area extends MapItem{
    private int length;
    private int width;

    public Area(int length, int width) {
        this.length = length;
        this.width = width;
    }

    public Area(int posX, int posY, int posXend, int posYend) {
        position = new Vector2D(posX, posY);
        this.length = posXend-posX;
        this.width = posYend-posY;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isInsideArea(Vector2D pos){
        boolean xInArea = position.getX() <= pos.getX() && (position.getX() + length) >= pos.getX();
        boolean yInArea = position.getY() <= pos.getY() && (position.getY() + width) >= pos.getY();

        return (xInArea && yInArea);
    }

    public boolean isAgentInsideArea(BaseEntity agent)
    {
        return isInsideArea(agent.getPosition());
    }

    public void onAgentCollision(BaseEntity agent)
    {
        System.out.println("Entered area");
    }

}
