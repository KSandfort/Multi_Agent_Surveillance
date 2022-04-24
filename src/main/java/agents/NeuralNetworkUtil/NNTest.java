package agents.NeuralNetworkUtil;

import agents.NeuralNetworkUtil.NEAT.GenePool;

import java.util.*;

public class NNTest {
    public static void main(String[] args) {
        //testNetworkOperations();
        testNEAT();
        //testNetworkEvaluation();
        //checkDFS();
        //checkDistance();
    }

    private static void checkDistance() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.init();
        NeuralNetwork nn2 = nn.copy();
        nn.addNode();
        System.out.println(nn);
        System.out.println(nn2);
        System.out.println("Distance to smaller version: " + NeuralNetwork.distance(nn,nn2));
        System.out.println("Distance to itself: " + NeuralNetwork.distance(nn,nn));
    }

    private static void testNetworkOperations() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.init();
        nn.addNode();

        NeuralNetwork nn2 = new NeuralNetwork();
        nn2.init();
        nn2.addNode();

        NeuralNetwork nn4 = new NeuralNetwork();
        nn4.init();
        System.out.println(nn4);

        nn.addNode();
        nn2.addNode();
        nn.addNode();
        nn.addNode();
        nn2.addNode();
        System.out.println(nn);
        System.out.println(nn2);
        System.out.println(nn2.copy());

        NeuralNetwork nn3 = NeuralNetwork.crossOver(nn2,nn);
        testNN(nn3);
    }

    public static void testNetworkEvaluation()
    {
        NeuralNetwork nn = new NeuralNetwork();
        List<NNConnection> conn = nn.getConnections();
        conn.add(new NNConnection(0, 3, -15, true));
        conn.add(new NNConnection(1, 3, 20, true));
        conn.add(new NNConnection(2, 3, 20, true));
        conn.add(new NNConnection(4, 3, -40, true));
        conn.add(new NNConnection(0, 4, -15, true));
        conn.add(new NNConnection(1, 4, 10, true));
        conn.add(new NNConnection(2, 4, 10, true));

        NeuralNetwork.maxNeurons = 5;

        testNN(nn);
    }

    public static void testNEAT()
    {
        double epsilon = 0.1;
        GenePool pool = new GenePool(2,1);
        pool.init();
        for (int i = 0; i < 240; i++) {
            if(i > 0 && 16 - calculateFilteredFitnessValue(pool.bestNetwork) < epsilon)
                break;
            pool.newGeneration();
        }

        NeuralNetwork nn = pool.bestNetwork;

        testNN(nn);
    }

    public static double calculateFilteredFitnessValue(NeuralNetwork nn)
    {
        double[] expected = {0,1,1,0};
        double res1 = nn.evaluate(new double[]{0, 0})[0] >= 0.5 ? 1 : 0;
        double res2 = nn.evaluate(new double[]{1, 0})[0] >= 0.5 ? 1 : 0;
        double res3 = nn.evaluate(new double[]{0, 1})[0] >= 0.5 ? 1 : 0;
        double res4 = nn.evaluate(new double[]{1, 1})[0] >= 0.5 ? 1 : 0;
        double[] result = {res1,res2,res3,res4};

        double error = 0;
        for (int i = 0; i < expected.length; i++) {
            error += (expected[i] - result[i])*(expected[i] - result[i]);//squared error
        }
        error = 4 - error;//make lowest error highest fitness
        error *= error;//square error to make difference bigger
        return error;
    }

    public static double calculateFitnessValue(NeuralNetwork nn)
    {
        double[] expected = {0,1,1,0};
        double res1 = nn.evaluate(new double[]{0, 0})[0];
        double res2 = nn.evaluate(new double[]{1, 0})[0];
        double res3 = nn.evaluate(new double[]{0, 1})[0];
        double res4 = nn.evaluate(new double[]{1, 1})[0];
        double[] result = {res1,res2,res3,res4};

        double error = 0;
        for (int i = 0; i < expected.length; i++) {
            error += (expected[i] - result[i])*(expected[i] - result[i]);//squared error
        }
        error = 4 - error;//make lowest error highest fitness
        error *= error;//square error to make difference bigger
        return error;
    }

    public static void checkDFS()
    {
        //NN without cycle
        NeuralNetwork nn = new NeuralNetwork();
        List<NNConnection> conn = nn.getConnections();
        conn.add(new NNConnection(0, 3, -15, true));
        conn.add(new NNConnection(1, 3, 20, true));
        conn.add(new NNConnection(2, 3, 20, true));
        conn.add(new NNConnection(0, 4, -15, true));
        conn.add(new NNConnection(1, 4, 10, true));
        conn.add(new NNConnection(2, 4, 10, true));

        //NN with cycle
        NeuralNetwork nn2 = new NeuralNetwork();
        List<NNConnection> conn2 = nn2.getConnections();
        conn2.add(new NNConnection(0, 4, -15, true));
        conn2.add(new NNConnection(2, 5, -15, true));
        conn2.add(new NNConnection(1, 6, -15, true));
        conn2.add(new NNConnection(4, 3, -15, true));
        conn2.add(new NNConnection(5, 3, -15, true));
        conn2.add(new NNConnection(6, 4, -15, true));
        conn2.add(new NNConnection(4, 5, -15, true));

        NeuralNetwork.maxNeurons = 7;

        System.out.println(nn.checkCycle(4,3));
        System.out.println(nn2.checkCycle(5,6));
    }

    public static void testNN(NeuralNetwork nn)
    {
        System.out.println((nn.evaluate(new double[]{0, 0})[0] ));
        System.out.println((nn.evaluate(new double[]{1, 0})[0] ));
        System.out.println((nn.evaluate(new double[]{0, 1})[0] ));
        System.out.println((nn.evaluate(new double[]{1, 1})[0] ));
        System.out.println(nn);
    }
}
