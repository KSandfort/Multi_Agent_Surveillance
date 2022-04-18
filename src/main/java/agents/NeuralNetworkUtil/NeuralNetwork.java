package agents.NeuralNetworkUtil;

import java.util.*;

public class NeuralNetwork {
    //coefficients for compatibility distance calculation
    final static double c1 =1.3;
    final static double c2 =1.3;
    final static double c3 =1;

    //mutation probabilities
    final static double disabledChance = 0.75;//chance that a disabled gene is inherited
    final double wMutationSelectionP = 0.25;//chance a network is selected to mutate the weights
    final double wMutationP = 0.8;//chance for each weight to be changed
    final double wResetP = 0.9;//chance that the value of a weight is reset instead of just changed
    final double addNodeP = 0.03;//chance that a node is added to the network
    final double addConnP = 0.05;//chance that a connection is added to the network

    private List<NNConnection> connections;
    double fitness;

    //hardcode these variables because its easier this way
    public static int inputNum = 2 + 1;//including bias node
    public static int outputNum = 1;//number of outputs
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
        for(int i = 0; i < inputNum;i++)
        {
            for(int o = inputNum; o < inputNum + outputNum;o++)
            {
                addConnection(i,o,null);
            }
        }

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

        if(random.nextDouble() < addConnP)
        {
            addConnection();
        }
    }

    //calculates the outputs of the neural network given the input
    //TODO: change the implementation of this function
    // this is a difficult thing to calculate and I am pretty sure the current implementation is not a proper one
    // currently it calculates the value of the hidden neurons in order of index
    // this works for small scale networks
    // but for larger ones this will probably create problems if the network structure evolves to become larger
    public double[] evaluate(double[] input)
    {
        double[] output = new double[outputNum];
        double[] neuronValues = new double[maxNeurons];
        if(input.length + 1 != inputNum)
        {
            System.out.println("Incorrect number of inputs ");
            return null;
        }

        neuronValues[0] = 1;//bias node
        System.arraycopy(input,0,neuronValues,1,input.length);

        //calculate values of hidden layers in order of neurons
        for(int i = inputNum + outputNum; i < maxNeurons;i++)
        {
            for(NNConnection c : connections)
            {
                if(c.getOut() != i)
                {
                    continue;
                }
                if(c.isEnabled())
                    neuronValues[i] += neuronValues[c.getIn()] * c.getWeight();
            }
            neuronValues[i] = sigmoid(neuronValues[i]);//sigmoid activation function
        }
        for(int i = inputNum; i < inputNum + outputNum;i++)//calculate output last
        {
            for(NNConnection c : connections)
            {
                if(c.getOut() != i)
                {
                    continue;
                }
                if(c.isEnabled())
                neuronValues[i] += neuronValues[c.getIn()] * c.getWeight();
            }
            neuronValues[i] = sigmoid(neuronValues[i]);
        }

        System.arraycopy(neuronValues,inputNum,output,0,outputNum);
        return output;
        //return neuronValues;
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
                if(!newConn.isEnabled() && random.nextDouble() < disabledChance)
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
        int i = 0;
        int k = 0;

        List<NNConnection> aConn = a.getConnections();
        List<NNConnection> bConn = b.getConnections();

        int e = 0;//number of excess genes
        int d = 0;//number of disjoint genes
        int m = 0;//number of matching genes
        int dW = 0;// total difference in weight between matching genes
        boolean isExcess = false;

        while(i < aConn.size() || k < bConn.size())
        {
            int aIn;
            int bIn;
            try{
                aIn = aConn.get(i).getInnovationCount();
            }catch(Exception ex)
            {
                isExcess = true;
                aIn = Integer.MAX_VALUE;
            }
            
            try{
                bIn = bConn.get(k).getInnovationCount();
            }catch(Exception ex)
            {
                isExcess = true;
                bIn = Integer.MAX_VALUE;
            }

            if(aIn == bIn)
            {
                dW += Math.abs(aConn.get(i).getWeight() + bConn.get(k).getWeight());
                m++;
                i++;
                k++;
                continue;
            }
            if(isExcess)
            {
                e++;
            }else
            {
                d++;
            }
            if(aIn < bIn)
            {
                i++;
            }
            else
            {
                k++;
            }
        }

        //int n = 1;
        int n = Math.max(aConn.size(), bConn.size());


        return c1*e/n + c2*d/n + c3*dW/m;
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

        addConnection(gene.getIn(),neuronIndex,1.0);
        addConnection(neuronIndex,gene.getOut(),gene.getWeight());

    }

    //function to add a new connection to the network
    //it will select two new neurons and add a connection to the network
    public void addConnection()
    {
        //TODO: we need a more advanced way of selecting neurons,
        // neurons can be selected in such a way that it can cause a cycle in the graph
        // this will make it impossible to compute the output of the network accurately
        int neuron1 = randomNeuron(connections,true);
        int neuron2 = randomNeuron(connections,false);

        if(neuron1 == neuron2)//cant make a connection if the neurons are the same
            return;

        addConnection(neuron1,neuron2,null);
    }

    //function to add  a new connection to the network
    //it insures that if the connection exists in another network it will use the correct innovation number
    public void addConnection(int i, int o,Double weight)
    {
        for (NNConnection c : connections) {//check if the two neurons are unconnected
            if((c.getIn() == i && c.getOut() == o) || (c.getIn() == o && c.getOut() == i))
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


    public int getInputNum() {
        return inputNum;
    }

    public int getOutputNum() {
        return outputNum;
    }
    @Override
    public String toString() {
        String output = "NeuralNetwork : ";
        List<NNConnection> conn = getConnections();
        for (NNConnection c : conn) {
            output += c.toString();
        }
        return output;
    }
}
