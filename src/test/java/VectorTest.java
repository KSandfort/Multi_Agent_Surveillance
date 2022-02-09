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

}
