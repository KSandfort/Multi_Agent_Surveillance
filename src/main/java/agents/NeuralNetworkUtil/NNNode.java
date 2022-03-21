package agents.NeuralNetworkUtil;

public class NNNode {
    int index;
    int function;//input, hidden, output
    static int globalIndex = 0;
    public NNNode(int f)
    {
        globalIndex++;
        index = globalIndex;
        function = f;
    }

    public NNNode(int f, int i)
    {
        index = i;
        function = f;
    }

    public NNNode copy()
    {
        return new NNNode(function,index);
    }
}
