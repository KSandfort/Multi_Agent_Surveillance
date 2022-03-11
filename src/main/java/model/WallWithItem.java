package model;

import Enities.Entity;
import gui.SimulationGUI;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * wall containing a window or door in the middle
 */
public abstract class WallWithItem extends Wall{

    private Wall leftWall;
    private Wall rightWall;
    private Area item;

    public WallWithItem(double xFrom, double yFrom, double xTo, double yTo, boolean vertical) {
        super(xFrom, yFrom, xTo, yTo);
        createWalls(xFrom, yFrom, xTo, yTo, vertical);
        createWallItem(xFrom, yFrom, xTo, yTo, vertical);
    }

    public WallWithItem(Vector2D pos1, Vector2D pos2, boolean vertical) {
        super(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY());
        createWalls(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), vertical);
        createWallItem(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), vertical);
    }

    /**
     * creates the two walls at the edge of the window or wall
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     * @param vertical
     */
    public void createWalls(double xFrom, double yFrom, double xTo, double yTo, boolean vertical){
        double length;
        if (vertical){
            length = Math.abs(yFrom - yTo);
            leftWall = new Wall (xFrom, yFrom, xTo, (yFrom + (length/3)));
            rightWall = new Wall(xFrom, yFrom + 2*(length/3), xTo, yTo);

        } else {
            length = Math.abs(xFrom - xTo);
            leftWall = new Wall (xFrom, yFrom, (xFrom - (length/3)), yTo);
            rightWall = new Wall(xFrom - 2*(length/3), yFrom, xTo, yTo);
        }
    }

    public void createWallItem(double xFrom, double yFrom, double xTo, double yTo, boolean vertical){
    }

    public Area getItem() {
        return item;
    }

    public void setItem(Area item) {
        this.item = item;
    }

    public Wall getLeftWall() {
        return leftWall;
    }

    public Wall getRightWall() {
        return rightWall;
    }

    public ArrayList<Wall> getWalls(){
        return new ArrayList<>(Arrays.asList(getLeftWall(), getRightWall()));
    }

    @Override
    public void onAgentCollision(Entity entity) {
        if (item.isAgentInsideArea(entity))
        {
            if(leftWall.isAgentInsideArea(entity)){
                leftWall.onAgentCollision(entity);
            }else if (rightWall.isAgentInsideArea(entity)){
                rightWall.onAgentCollision(entity);
            }else{
                item.onAgentCollision(entity);
            }
        }else{
            super.onAgentCollision(entity);
        }
    }

    @Override
    public ArrayList<Node> getComponents() {
        ArrayList<Node> components = new ArrayList<>();
        int offset = SimulationGUI.CANVAS_OFFSET;
        double sf = SimulationGUI.SCALING_FACTOR;
        ArrayList<Node> lWallComp = leftWall.getComponents();
        ArrayList<Node> rWallComp = rightWall.getComponents();
        ArrayList<Node> itemComp = item.getComponents();

        for (int i = 0; i<lWallComp.size();i++){
            components.add(lWallComp.get(i));
            components.add(rWallComp.get(i));
        }
        for (int i = 0; i<4;i++){
            components.add(itemComp.get(i));
        }
        return components;
    }

    @Override
    public boolean isSolidBody() {
        return true;
    }
}
