package Enities;

import gui.SimulationGUI;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import model.Vector2D;

public class Ray {
    Vector2D origin;
    Vector2D direction;

    public Ray(Vector2D origin, Vector2D direction){
        this.origin = origin;
        this.direction = direction;
    }

    public Vector2D getPoint(){
        return Vector2D.add(origin,direction);
    }

    public void cast(Vector2D c, Vector2D d){
        Vector2D a = origin;
        Vector2D b = getPoint();
        double a1 = b.getY() - a.getY();
        double b1 = a.getX() - b.getX();
        double c1 = a1*(a.getX()) + b1*(a.getY());

        double a2 = d.getY() - c.getY();
        double b2 = c.getX() - d.getX();
        double c2 = a2*(c.getX())+ b2*(c.getY());

        double determinant = a1*b2 - a2*b1;
        if (determinant != 0){
            double x = (b2*c1 - b1*c2)/determinant;
            double y = (a1*c2 - a2*c1)/determinant;
            Vector2D point = new Vector2D(x,y);
            direction = Vector2D.subtract(point, origin);
        }
    }

    public static double det(Double[] a, Double [] b){
        return a[0]*b[1] - a[1] * b[0];
    }

    public Node getComponent(){
        double sf = SimulationGUI.SCALING_FACTOR;
        int offset = SimulationGUI.CANVAS_OFFSET;
        Line line = new Line(
                origin.getX()*sf + offset,
                origin.getY()*sf + offset,
                getPoint().getX()*sf + offset,
                getPoint().getY()*sf + offset);
        return line;
    }
}
