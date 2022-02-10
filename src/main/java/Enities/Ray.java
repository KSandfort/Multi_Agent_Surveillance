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

    public Vector2D intersectionPoint(Vector2D c, Vector2D d){
        Vector2D a = this.origin;
        Vector2D b = Vector2D.add(a, direction);
        Vector2D e = Vector2D.subtract(b, a);
        Vector2D f = Vector2D.subtract(d, c);
        Vector2D p = new Vector2D(-e.getY(), e.getX());
        if (Vector2D.dotProduct(f, p) == 0) {
            return b;
        }
        double h = Vector2D.dotProduct(Vector2D.subtract(a, c), p) / Vector2D.dotProduct(f, p);
        if (h > 0 && h < 1) {
            return Vector2D.add(Vector2D.scalar(direction, h), this.origin);
        }
        return b;
    }

    public Node getComponent(){
        double sf = SimulationGUI.SCALING_FACTOR;
        int offset = SimulationGUI.CANVAS_OFFSET;
        Line line = new Line(origin.getX()*sf + offset, origin.getY()*sf + offset, getPoint().getX()*sf + offset, getPoint().getY()*sf + offset);
        return line;
    }
}
