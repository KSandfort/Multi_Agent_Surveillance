package model.neural_network;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class NeuralNetwork {

    //is the initial network empty (true) or fully connected (false)
    final boolean START_FROM_NOTHING = true;

    //coefficients for compatibility distance calculation
    final static double c1 =1.3;
    final static double c2 =1.3;
    final static double c3 =4.0;

    //mutation probabilities
    final static double disabledChance = 0.75;  // Chance that a disabled gene is inherited
    final double wMutationSelectionP = 0.25;    // Chance a network is selected to mutate the weights
    final double wMutationP = 0.8;              // Chance for each weight to be changed
    final double wResetP = 0.9;                 // Chance that the value of a weight is reset instead of just changed
    final double wActivationP = 0.25;           // Chance that the value of a weight is reset instead of just changed
    final double addNodeP = 0.03;               // Chance that a node is added to the network
    final double addConnP = 1.0;                // Chance that a connection is added to the network
    final double maxTryCount = 10;
    private List<NNConnection> connections;
    double fitness;

    //hardcode these variables because its easier this way
    public static int inputNum = 18 + 1; //including bias node
    public static int outputNum = 7; //number of outputs
    public static int maxNeurons = inputNum + outputNum + 1;//total possible number of neurons

    private static List<NNConnection> newConnections = new ArrayList<>();//the new connections of the new generation

    //Hashmap storing the innovation number of each connection that has been used to create a new node
    //this to prevent the creation of too many new nodes when the same node is evolved multiple times
    private static HashMap<Integer,Integer> newNodes = new HashMap<>();

    Random random;

    public NeuralNetwork()
    {
        connections = new ArrayList<NNConnection>();
        random = new Random(System.nanoTime());
    }

    //function to initialize the neural network
    //it only connects the input nodes with a connection to each output node, no hidden inputs are created
    public void init()
    {
        if(!START_FROM_NOTHING){
            for(int i = 0; i < inputNum;i++)
            {
                for(int o = inputNum; o < inputNum + outputNum;o++)
                {
                    addConnection(i,o,null,0);
                }
            }
        }
        mutate();
    }

    //returns a copy of the network
    public NeuralNetwork copy()
    {
        NeuralNetwork copy = new NeuralNetwork();

        List<NNConnection> newConnections = copy.getConnections();

        for(NNConnection c : connections)
        {
            newConnections.add(c.copy());//add copies of the connection to the new network
        }

        copy.setFitness(fitness);//copy the fitness value
        return copy;
    }

    //function to mutate the network
    //it mutates the network in three ways:
    //  changes the weights of the connections in the network
    //  adds a node to the network by replacing an existing connection in the network
    //  adds a connection to connect two previously unconnected nodes
    public void mutate()
    {

        if(random.nextDouble() < wMutationSelectionP)
        {
            mutateWeights();
        }

        if(random.nextDouble() < addNodeP)
        {
            addNode();
        }

        if(random.nextDouble() < addConnP) {
            addConnection(0);
        }
    }

    //calculates the outputs of the neural network given the input
    //NOTE: this only works if no cycles are present in the network
    //so when adding edges, this will need to be checked
    public double[] evaluate(double[] input)
    {
        double[] output = new double[outputNum];
        double[] neuronValues = new double[maxNeurons + outputNum];
        List<NNConnection>[] graph = orderConnectionsByNode(false);

        if(input.length + 1 != inputNum)
        {
            System.out.println("Incorrect number of inputs ");
            return null;
        }

        double[] biasedInput = new double[input.length + 1];
        biasedInput[0] = 1;//bias node
        System.arraycopy(input,0,biasedInput,1,input.length);

        for (int i = 0; i < outputNum; i++) {
            output[i] = getValue(inputNum + i,graph,biasedInput);
        }

        return output;
    }

    //recursive method for evaluating the network
    //starts from output nodes and moves backwards through the network calculating the values of each node
    //not the most efficient since we recalculate some values when evaluating multiple outputs
    //but it is the first implementation im confident actually is usable
    private double getValue(int i, List<NNConnection>[] graph,double[] input )
    {
        if(i < inputNum)
        {
            return input[i];
        }

        double value = 0;
        List<NNConnection> incoming = graph[i];
        for(NNConnection c : incoming)
        {
            if(c.isEnabled())
                value += getValue(c.getIn(),graph,input) * c.getWeight();
        }
        value = sigmoid(value);
        return value;
    }

    //sigmoid function copied from the paper
    public double sigmoid(double x)
    {
        return 1/(1 + Math.exp(-4.9*x));
    }

    //function to calculate the result of crossover between two networks
    //it assumes the connections of both networks are sorted in ascending order based on innovation count
    //it constructs a new network by combining connections from both networks
    //if both networks have a connection with the same innovation count, one of the two connection is selected at random
    //if one connection has a innovation count that is not in the other network,
    //  only the connections of the fittest network are copied to the new network
    public static NeuralNetwork crossOver(NeuralNetwork first, NeuralNetwork second)
    {
        NeuralNetwork a;
        NeuralNetwork b;
        //always make sure a is the fitter network
        if(first.getFitness() < second.getFitness())
        {
            a = first;
            b = second;
        }else
        {
            a = second;
            b = first;
        }

        NeuralNetwork nn = new NeuralNetwork();
        List<NNConnection> aConn = a.getConnections();
        List<NNConnection> bConn = b.getConnections();
        List<NNConnection> newConnections = new ArrayList<NNConnection>();

        //indices for connections of both network to be able to iterate over both lists;
        int i = 0;
        int k = 0;
        Random random = new Random();
        while(i < aConn.size() || k < bConn.size())
        {
            NNConnection aC;
            NNConnection bC;
            int aCount;//innovation count of 'a' connection
            int bCount;//innovation count of 'b' connection
            try {
                aC = aConn.get(i);
                aCount = aC.getInnovationCount();
            }catch (Exception e)
            {
                break;
            }

            try {
                bC = bConn.get(k);
                bCount = bC.getInnovationCount();
            }catch (Exception e)
            {
                bC = null;
                bCount = Integer.MAX_VALUE;
            }

            if(aCount == bCount)
            {
                NNConnection inherited;
                boolean enabled = true;
                if(!aC.isEnabled() || !bC.isEnabled())
                {
                    enabled = false;
                }
                //System.out.println(aCount +"-"+ bCount);
                if(random.nextBoolean())//if genes match randomly select one of the two genes
                {
                    //System.out.println("Selecting a");
                    inherited = aC.copy();
                }else
                {
                    //System.out.println("Selecting b");
                    inherited = bC.copy();
                }

                inherited.setEnabled(true);
                if(!enabled && random.nextDouble() < disabledChance)
                {
                    inherited.setEnabled(false);
                }
                newConnections.add(inherited);
                i++;
                k++;
            }
            else if(aCount < bCount)
            {
                NNConnection newConn = aC.copy();
                newConn.setEnabled(true);

                if(!aC.isEnabled() && random.nextDouble() < disabledChance)
                    newConn.setEnabled(false);

                newConnections.add(newConn);//only copy excess/disjoint genes from fittest genome
                i++;
            }
            else
            {
                k++;
            }
        }

        nn.setConnections(newConnections);
        return nn;
    }

    //distance measure between two networks
    //this function calculates how different two networks are from each other
    //it uses the innovation count of each network connection to figure this out
    //it considers the average weight difference of the connections that have matching innovation counts
    //and it considers the number of connections with non matching innovation counts
    //using the number of connections in total and the predefined coefficients it calculates the distance
    public static double distance(NeuralNetwork a, NeuralNetwork b)
    {
        //we assume a is the larger network
        int n = a.getConnections().size();

        //officially disjoint and excess genes are evaluated separately, in practise they are given the same coefficient
        // and thus we combine them
        int W = 0;//sum of weight differences between matching genes
        int D = 0;//number of non matching genes
        int w= 0;//number of matching genes

        //indices for iterating
        int i = 0;
        int k = 0;

        while(i < a.getConnections().size() || k < b.getConnections().size())
        {
            if(i >= a.getConnections().size())
            {
                D += b.getConnections().size() - k;
                break;
            }
            if(k >= b.getConnections().size())
            {
                D += a.getConnections().size() - i;
                break;
            }

            if(a.getConnections().get(i).getInnovationCount() == b.getConnections().get(k).getInnovationCount())
            {
                W+= Math.abs(a.getConnections().get(i).getWeight() - b.getConnections().get(k).getWeight());
                w++;
                i++;
                k++;
            }else if(a.getConnections().get(i).getInnovationCount() > b.getConnections().get(k).getInnovationCount())
            {
                D++;
                k++;
            }else
            {
                D++;
                i++;
            }
        }

        return c1*D/n + c3*W/w;
    }

    //function to mutate the weights of the network connections
    //for each weight there is a chance for it to change
    //if the weight changes it can either permute the weight of assign a completely new value
    public void mutateWeights()
    {
        for (NNConnection c : connections) {
            if(random.nextDouble() > wMutationP)
                continue;
            if(random.nextDouble() > wResetP)
            {
                c.resetWeight();
            }else
            {
                c.permuteWeight();
            }
            if(!c.isEnabled() && random.nextDouble() > wActivationP)
            {
                c.setEnabled(true);
            }
        }
    }

    //function to add a node to the network
    //it will randomly choose one connection in the network
    //this connection will be disabled, and replaced with two new connections
    //one connection from the original input to a new neuron with a weight of one
    //and one connection from the new neuron to the original output with the same weight as the original connection
    //this will insure that the network will have the exact same function as than before the adding of the node
    public void addNode()
    {
        if(connections.size() == 0)
            return;

        NNConnection gene = connections.get(random.nextInt(connections.size()));

        if(!gene.isEnabled())
            return;

        gene.setEnabled(false);

        int neuronIndex = maxNeurons;

        if(newNodes.containsKey(gene.getInnovationCount()))
        {
            neuronIndex = newNodes.get(gene.getInnovationCount());//neuron has been made before so use that neuron count
        }else
        {
            newNodes.put(gene.getInnovationCount(), maxNeurons);
            maxNeurons++;//neuron does not exist yet so make a new one
        }

        addConnection(gene.getIn(),neuronIndex,1.0,0);
        addConnection(neuronIndex,gene.getOut(),gene.getWeight(),0);

    }

    //function to add a new connection to the network
    //it will select two new neurons and add a connection to the network
    public void addConnection(int tryCount)
    {
        if(tryCount > maxTryCount)
            return;

        int neuron1 = randomNeuron(connections,true);
        int neuron2 = randomNeuron(connections,false);

        if(neuron1 == neuron2) {//cant make a connection if the neurons are the same
            addConnection(tryCount + 1);
            return;
        }
        addConnection(neuron1,neuron2,null, tryCount);
    }

    //function to add  a new connection to the network
    //it insures that if the connection exists in another network it will use the correct innovation number
    public void addConnection(int i, int o,Double weight,int tryCount)
    {
        boolean isAllowed = true;

        for (NNConnection c : connections) {//check if the two neurons are unconnected
            if((c.getIn() == i && c.getOut() == o) || (c.getIn() == o && c.getOut() == i))
                isAllowed = false;
        }

        if(checkCycle(i,o))//check if due to adding the edge a cycle is formed in the network
        {
            isAllowed = false;
        }

        if(!isAllowed) {
            addConnection(tryCount + 1);
            return;
        }

        for(NNConnection c : newConnections) {//check if the connection has evolved before

            if (c.getIn() == i && c.getOut() == o){
                NNConnection newConn = c.copy();
                if(weight != null)
                    newConn.setWeight(weight);
                else
                    newConn.resetWeight();
                addConnectionInOrder(newConn);
                return;
            }
        }

        NNConnection newConn = new NNConnection(i, o, 1, true);
        if(weight != null)
            newConn.setWeight(weight);
        else
            newConn.resetWeight();

        newConnections.add(newConn);
        addConnectionInOrder(newConn);
    }

    public boolean checkCycle(int i ,int o) {
        List<NNConnection>[] graph = orderConnectionsByNode(true);
        NNConnection toAdd = new NNConnection(i,o,1,true,0);//innovation count does not matter right now
        graph[i].add(toAdd);

        //start DFS from input node
        //we know the graph does not contain any cycles
        //thus after adding the edge, the only cycles that can be formed will use the new edge
        boolean[] visited = new boolean[maxNeurons];
        return DFS(i,graph,i,false);
    }

    private boolean DFS(int node, List<NNConnection>[] graph,int input, boolean foundInput)
    {
        if(node == input && foundInput)
            return true;

        foundInput = true;

        for(NNConnection c : graph[node])
        {
            if(DFS(c.getOut(),graph,input,foundInput))
                return true;
        }

        return false;
    }

    //function to add a new connection to the list of connections such that the list stays sorted
    public void addConnectionInOrder(NNConnection newConn)
    {
        for (int j = 0; j < connections.size(); j++) {
            if(newConn.getInnovationCount() < connections.get(j).getInnovationCount())
            {
                connections.add(j,newConn);
                return;
            }
        }
        connections.add(newConn);
    }

    //function to select a random neuron, it only selects neurons that are present in the network
    public int randomNeuron(List<NNConnection> genes, boolean canBeInput)
    {
        //default value of boolean is false so we dont need to initialize the values of the array, since we want them to be false
        boolean[] possibleNeurons = new boolean[maxNeurons + outputNum];

        //either include the input neurons in the selectable neurons or the output neurons to make it possible to select two neurons without selecting two output or input neurons
        if(canBeInput)
        {
            for (int i = 0; i < inputNum; i++) {
                possibleNeurons[i] = true;
            }
        }else
        {
            for(int i = inputNum;i < inputNum + outputNum;i++) {
                possibleNeurons[i] = true;
            }
        }

        for (int i = inputNum+outputNum; i < maxNeurons; i++) //only select neurons that are actually in the network
        {
            for(NNConnection c : connections)
            {
                if(c.getIn() == i || c.getOut() == i)
                {
                    possibleNeurons[i] = true;
                }
            }
        }

        int n;//randomly selected neuron
        do
        {
            n = random.nextInt(maxNeurons);
        }
        while(!possibleNeurons[n]);

        return n;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public List<NNConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<NNConnection> connections) {
        this.connections = connections;
    }

    private List<NNConnection>[] orderConnectionsByNode(boolean sortByInput)
    {
        List<NNConnection>[] graph = new List[maxNeurons];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new ArrayList<NNConnection>();
        }

        for(NNConnection c : connections)
        {
            if(sortByInput)
                graph[c.getIn()].add(c);
            else
                graph[c.getOut()].add(c);
        }

        return graph;
    }

    public int getInputNum() {
        return inputNum;
    }

    public int getOutputNum() {
        return outputNum;
    }

    public void saveNetwork(String file)
    {
        try
        {
            FileWriter fw = new FileWriter(file);
            String text = "f=" + fitness;//first line is fitness
            //next print connections
            for(NNConnection c : connections)
            {
                text += "\nc=" + c.toString();
            }

            fw.write(text);
            fw.flush();
            fw.close();
        }
        catch (IOException exception)
        {
            System.out.println("Error writing to file:\n" + exception);
        }
    }

    public void saveGlobals(String file)
    {
        try
        {
            FileWriter fw = new FileWriter(file);
            String text = "\nmn=" + maxNeurons;//first line is maxNeurons
            //print stored connections
            System.out.println("connections : " + newConnections.size());
            for(NNConnection c : newConnections)
            {
                text += "\nnc=" + c.toString();
            }
            text += "\n-----------------------";//add separation between connection lists and hashmap
            for(Integer i : newNodes.keySet())
            {
                text += "\nnn=" + i + "," + newNodes.get(i);
            }

            fw.write(text);
            fw.flush();
            fw.close();
        }
        catch (IOException exception)
        {
            System.out.println("Error writing to file:\n" + exception);
        }
    }

    public static void readGlobals(String file)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String record = "";

            while ((record = br.readLine()) != null)
            {
                if( record.startsWith("mn="))//read maxneurons from file
                {
                    NeuralNetwork.maxNeurons = Integer.parseInt(record.substring(3));
                }

                if(record.startsWith("nc="))//add connections to arrays
                {
                    NeuralNetwork.newConnections.add(NNConnection.readConnectionFromString(record.substring(3)));
                }

                if(record.startsWith("nn="))//add connections to arrays
                {
                    char[] text = record.toCharArray();
                    int commaIndex = -1;
                    for (int i = 0; i < text.length; i++) {
                        if(text[i] == ',')
                        {
                            commaIndex = i;
                        }
                    }

                    int key = Integer.parseInt(record.substring(3,commaIndex));
                    int value = Integer.parseInt(record.substring(commaIndex+1));
                    NeuralNetwork.newNodes.put(key,value);
                }
            }
            br.close();
        }
        catch (IOException exception)
        {
            System.out.println("Error reading file:\n" + exception);
        }
    }

    public static NeuralNetwork readNetwork(String file)
    {
        NeuralNetwork nn = new NeuralNetwork();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String record = "";

            while ((record = br.readLine()) != null)
            {
                if( record.startsWith("f="))//read fitness from file
                {
                    nn.setFitness(Double.parseDouble(record.substring(2)));
                }

                if(record.startsWith("c="))//add connections to arrays
                {
                    nn.connections.add(NNConnection.readConnectionFromString(record.substring(2)));
                }
            }
            br.close();
        }
        catch (IOException exception)
        {
            System.out.println("Error reading file:\n" + exception);
        }
        return nn;
    }


    @Override
    public String toString() {
        String output = "NeuralNetwork : " + "[fitness: " + fitness + "]";
        List<NNConnection> conn = getConnections();
        for (NNConnection c : conn) {
            output += c.toString();
        }
        return output;
    }
}
