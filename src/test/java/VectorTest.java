import model.Vector2D;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests all the functionalities of a Vector2D.
 */
public class VectorTest {

    @Test
    /**
     * Tests addition of two vectors.
     */
    public void testAddition() {
        Vector2D a = new Vector2D(2, 3);
        Vector2D b = new Vector2D(4, 7);
        Vector2D c = Vector2D.add(a, b);
        assertEquals(c.getX(), 6);
        assertEquals(c.getY(), 10);
    }

    @Test
    /**
     * Tests subtraction of two vectors.
     */
    public void testSubtraction() {
        Vector2D a = new Vector2D(2, 3);
        Vector2D b = new Vector2D(4, 7);
        Vector2D c = Vector2D.subtract(a, b);
        assertEquals(c.getX(), -2);
        assertEquals(c.getY(), -4);
    }

    @Test
    /**
     * Tests the length computation of a vector.
     */
    public void testLength() {
        Vector2D a = new Vector2D(3, 4);
        double length = Vector2D.length(a);
        assertEquals(length, 5);
    }

    @Test
    /**
     * Tests dot product of two vectors.
     */
    public void testDotProduct() {
        Vector2D a = new Vector2D(2, 3);
        Vector2D b = new Vector2D(4, 7);
        double dotProduct = Vector2D.dotProduct(a, b);
        assertEquals(dotProduct, 29);
    }

    @Test
    /**
     * Tests the intersection computing algorithm.
     */
    public void testIntersections() {
        Vector2D a, b, c, d;

        // Lines that cross
        a = new Vector2D(1, 1);
        b = new Vector2D(10, 9);
        c = new Vector2D(12, 1);
        d = new Vector2D(1, 13);
        assertEquals(Vector2D.doTwoLinesCross(a, b, c, d), true);

        // Lines that don't cross
        a = new Vector2D(1, 1);
        b = new Vector2D(1, 10);
        c = new Vector2D(12, 1);
        d = new Vector2D(5, 5);
        assertEquals(Vector2D.doTwoLinesCross(a, b, c, d), false);

        a = new Vector2D(1, 0);
        b = new Vector2D(1, 1);
        c = new Vector2D(1, 3);
        d = new Vector2D(1, 5);
        assertEquals(Vector2D.doTwoLinesCross(a, b, c, d), false);
    }

}
