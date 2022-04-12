package agents.NeuralNetworkUtil;

import agents.NeuralNetworkUtil.NEAT.GenePool;

import java.util.*;

public class NNTest {
    public static void main(String[] args) {
        /*NeuralNetwork nn = new NeuralNetwork(2,1);
        List<NNConnection> conn = nn.getConnections();
        conn.add(new NNConnection(0, 3, -15, true));
        conn.add(new NNConnection(1, 3, 20, true));
        conn.add(new NNConnection(2, 3, 20, true));
        conn.add(new NNConnection(4, 3, -40, true));
        conn.add(new NNConnection(0, 4, -15, true));
        conn.add(new NNConnection(1, 4, 10, true));
        conn.add(new NNConnection(2, 4, 10, true));

        NeuralNetwork.maxNeurons = 5;

        System.out.println(Arrays.toString(nn.evaluate(new double[]{0, 0})));
        System.out.println(Arrays.toString(nn.evaluate(new double[]{1, 0})));
        System.out.println(Arrays.toString(nn.evaluate(new double[]{0, 1})));
        System.out.println(Arrays.toString(nn.evaluate(new double[]{1, 1})));*/

        GenePool pool = new GenePool(2,1);
        pool.init();
        for (int i = 0; i < 30; i++) {
            pool.newGeneration();
        }

        NeuralNetwork nn = pool.bestNetwork;

        System.out.println(Arrays.toString(nn.evaluate(new double[]{0, 0})));
        System.out.println(Arrays.toString(nn.evaluate(new double[]{1, 0})));
        System.out.println(Arrays.toString(nn.evaluate(new double[]{0, 1})));
        System.out.println(Arrays.toString(nn.evaluate(new double[]{1, 1})));

        System.out.println(nn);
    }
}
