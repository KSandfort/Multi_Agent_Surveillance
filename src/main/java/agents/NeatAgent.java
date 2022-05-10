package agents;

import Enities.Entity;
import model.MapItem;
import model.Vector2D;
import model.neural_network.NeuralNetwork;

import java.util.ArrayList;

public class NeatAgent extends AbstractAgent {

    NeuralNetwork nn;

    public NeatAgent() {
        super();
        nn = new NeuralNetwork();
    }

    @Override
    public void addControls() {
        nn.init();
    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        /*
            Input array:
                0: marker type 1 count
                1: marker type 2 count
                2: marker type 3 count
                3: marker type 4 count
                4: marker type 5 count
                5: marker type 1 angle
                6: marker type 2 angle
                7: marker type 3 angle
                8: marker type 4 angle
                9: marker type 5 angle
                10: wall angle
                11: team-mate vision count
                12: enemy vision count
                13:

            Output array:
                0: speed (0 to 1/3: stand, 1/3 to 2/3: walk, 2/3 to 1: sprint)
                1: direction (0 = max left, 0.5 = straight, 1 = max right)
                2: place marker 1
                3: place marker 2
                4: place marker 3
                5: place marker 4
                6: place marker 5

         */
        Entity e = entityInstance;

        // --- Step 1: Define inputs ---
        double[] input = new double[5];

        // --- Step 2: Do the NN magic ---
        double[] output = nn.evaluate(input);

        // --- Step 3: Apply output results ---

        // Velocity
        double velocity = 0;
        if (output[0] > (double) 1/3) {
            if (output[0] > (double) 2/3) {
                velocity = Entity.sprintSpeedGuard; //TODO: make dynamic for guard or agent
            }
            else {
                velocity = Entity.baseSpeedGuard;
            }
        }

        // Direction (Turning)
        double angle = 0;


        e.getDirection().pivot(angle); // Turn
        e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity))); // Move
    }
}
