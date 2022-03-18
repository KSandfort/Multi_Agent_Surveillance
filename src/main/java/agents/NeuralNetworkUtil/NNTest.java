package agents.NeuralNetworkUtil;

import java.util.*;

public class NNTest {
    public static void main(String[] args) {
        NeuralNetwork nn = new NeuralNetwork(2,1);
        List<NNConnection> conn = nn.getConnections();
        conn.add(new NNConnection(0, 4, -15, true));
        conn.add(new NNConnection(1, 4, 20, true));
        conn.add(new NNConnection(2, 4, 20, true));
        conn.add(new NNConnection(3, 4, -40, true));
        conn.add(new NNConnection(0, 3, -15, true));
        conn.add(new NNConnection(1, 3, 10, true));
        conn.add(new NNConnection(2, 3, 10, true));

        NeuralNetwork.maxNeurons = 4;

        System.out.println(Arrays.toString(nn.evaluate(new double[]{0, 0})));
        System.out.println(Arrays.toString(nn.evaluate(new double[]{1, 0})));
        System.out.println(Arrays.toString(nn.evaluate(new double[]{0, 1})));
        System.out.println(Arrays.toString(nn.evaluate(new double[]{1, 1})));
    }
}
