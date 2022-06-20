package model;

import Enities.Entity;
import gui.SimulationGUI;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * wall containing a window or door in the middle
 */
@Getter
@Setter
public abstract class WallWithItem extends Wall{

    // Variables
    private double areaSoundVolume = 2;
    private Wall leftWall;
    private Wall rightWall;
    private Area item;

    /**
     * Constructor
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     * @param vertical
     */
    public WallWithItem(double xFrom, double yFrom, double xTo, double yTo, boolean vertical) {
        super(xFrom, yFrom, xTo, yTo);
        createWalls(xFrom, yFrom, xTo, yTo, vertical);
        createWallItem(xFrom, yFrom, xTo, yTo, vertical);
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

    public void createWallItem(double xFrom, double yFrom, double xTo, double yTo, boolean vertical) {
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
