package agents;

import Enities.Entity;
import Enities.Intruder;
import Enities.Ray;
import model.MapItem;
import model.Vector2D;
import model.neural_network.NeuralNetwork;
import java.util.Arrays;
import java.util.ArrayList;

/**
 *
 */
public class NeatAgent extends AbstractAgent {

    // Variables
    static NeuralNetwork nn = null;

    /**
     * Constructor
     */
    public NeatAgent() {
        super();
        if(nn == null){
            nn = new NeuralNetwork();
            nn.init();
            nn.readNetwork("bestNetwork.txt");
        }

    }

    public static void setNn(NeuralNetwork nn) {
        NeatAgent.nn = nn;
    }

    @Override
    public void addControls() {

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
                10: wall side (avg)
                11: wall distance (avg)
                12: team-mate vision count
                13: team-mate vision angle
                14: enemy vision count
                15: enemy vision angle
                16: sound volume
                17: target direction (intruders only)

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
        double[] input = new double[18];
        double[] entitySensing = entitySensing();
        double[] wallSensing = wallSensing();//this boi is a problem

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
        input[10] = wallSensing[0];
        input[11] = wallSensing[1];
        input[12] = entitySensing[0];
        input[13] = entitySensing[2];
        input[14] = entitySensing[1];
        input[15] = entitySensing[3];

        input[16] = Vector2D.shortestAngle(e.getDirection(), e.getListeningDirection(e.getMap().getMovingItems(), e.getMap().getStaticItems()));//this one is a problem sometimes

        if (e instanceof Intruder) {
            Vector2D targetDir = ((Intruder) e).calculateTargetDirection();
            input[17] = Math.atan2(targetDir.getY(),targetDir.getX());
        } else
            input[17] = 1;

        // --- Step 2: Do the NN magic ---
        double[] output = nn.evaluate(input);

        // --- Step 3: Apply output results ---

        // Velocity
        double velocity = 0;
        if (output[0] > 0.5) {
            velocity = sprintVelocity;
        } else {
            velocity = baseVelocity;
        }

        // Direction (Turning)
        double angle = (output[1] - 0.5) * 2 * maxAngle;

        // Marker placing
        double[] markers = Arrays.copyOfRange(output, 2, 6);
        int markerPriority = 0;
        for (int i = 0; i < markers.length; i++) {
            if (i > markerPriority) {
                markerPriority = i;
            }
        }

        if (e.getMap().getGameController().getCurrentStep() % 20 == 0) {
            e.placeMarker(markerPriority);
        }

        e.getDirection().pivot(angle); // Turn
        e.getDirection().normalize();
        e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), velocity))); // Move
    }

    /**
     *
     * @return
     */
    private double[] wallSensing() {
        ArrayList<Ray> vision = this.entityInstance.getFov();

        double averageDistance = 0;
        // Average distance to the left of the bug, average distance to the right of the bug
        double leftSideAverage = 0;
        double rightSideAverage = 0;
        double i = 0;
        for (Ray ray : vision) {
            Vector2D rayVector = ray.getDirection();
            averageDistance += Vector2D.length(rayVector);

            // Not sure if this is needed...? It gives the corner-vision rays more weight than middle rays
            double sideScaler = Math.abs((i / (vision.size() / 2.0)) - 1);

            if (i < vision.size() / 2.0)
                leftSideAverage += Vector2D.length(rayVector) * sideScaler;
            else if (i > vision.size() / 2.0)
                rightSideAverage += Vector2D.length(rayVector) * sideScaler;
            i += 1;
        }
        return new double[]{averageDistance, rightSideAverage - leftSideAverage};
    }

    /**
     *
     * @return
     */
    private double[] entitySensing() {
        double mateCount = 0;
        double enemyCount = 0;
        double mateDirectionSum = 0;
        double enemyDirectionSum = 0;
        Entity e = this.entityInstance;
        ArrayList<Entity> detectedEntities = e.getDetectedEntities();
        if (detectedEntities.size() != 0){
            for (Entity otherEntity : detectedEntities) {
                if (otherEntity.isIntruder() == e.isIntruder()) { // If in own team
                    mateCount++;
                    mateDirectionSum += Vector2D.shortestAngle(otherEntity.getPosition(), e.getPosition());
                }
                else { // if in opposite team
                    enemyCount++;
                    enemyDirectionSum += Vector2D.shortestAngle(otherEntity.getPosition(), e.getPosition());
                }
            }
        }
        double mateDirection = 0;
        double enemyDirection = 0;

        if (mateCount != 0) {
            mateDirection = mateDirectionSum / mateCount;
        }
        if (enemyCount != 0) {
            enemyDirection = enemyDirectionSum / enemyCount;
        }

        return new double[]{mateCount, enemyCount, mateDirection, enemyDirection};
    }
}
