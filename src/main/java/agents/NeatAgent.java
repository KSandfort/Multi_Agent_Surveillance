package agents;

import Enities.Entity;
import model.MapItem;
import model.Vector2D;
import model.neural_network.NeuralNetwork;
import java.util.Arrays;

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

        input[0] = e.getMarkerSensing()[0][0];
        input[1] = e.getMarkerSensing()[1][0];
        input[2] = e.getMarkerSensing()[2][0];
        input[3] = e.getMarkerSensing()[3][0];
        input[4] = e.getMarkerSensing()[4][0];
        input[5] = e.getMarkerSensing()[0][1];
        input[6] = e.getMarkerSensing()[1][1];
        input[7] = e.getMarkerSensing()[2][1];
        input[8] = e.getMarkerSensing()[3][1];
        input[9] = e.getMarkerSensing()[4][1];
        input[10] = 0; // TODO: Wall angle sensing
        input[11] = 0; // TODO: Teammate sensing
        input[12] = 0; // TODO: Enemy sensing

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

        // Marker placing
        double[] markers = Arrays.copyOfRange(output, 2, 6);
        int markerPriority = 0;
        for (int i = 0; i < markers.length; i++) {
            if (i > markerPriority) {
                markerPriority = i;
            }
        }

        if (e.getMap().getGameController().getSimulationGUI().getCurrentStep() % 20 == 0) {
            e.placeMarker(markerPriority);
        }

        e.getDirection().pivot(angle); // Turn
        e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity))); // Move
    }
}
