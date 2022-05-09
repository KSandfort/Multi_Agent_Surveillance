package agents;

import model.MapItem;
import model.neural_network.NeuralNetwork;

import java.util.ArrayList;

public class NeatAgent extends AbstractAgent {

    NeuralNetwork nn = new NeuralNetwork();

    @Override
    public void addControls() {

    }

    @Override
    public void changeMovement(ArrayList<MapItem> items) {
        System.out.println("I am a neat agent!");
    }
}
