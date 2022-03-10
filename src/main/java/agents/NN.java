package agents;

import model.Matrix;

public class NN {
    int iSize;//number of input neurons
    int hSize;//number of hidden neurons
    int oSize;//number of output neurons
    Matrix ihWeights;//weights connecting input and hidden neurons
    Matrix hoWeights;//weights connecting hidden and output neurons

    public NN(int iSize, int hSize, int oSize)
    {
        ihWeights = new Matrix(iSize,hSize);
        hoWeights = new Matrix(hSize,oSize);

    }
}
