package agents.NeuralNetworkUtil;

import java.util.Random;

public class NNConnection {
    
    final double initBound = 2;
    public static int globalInnovation = 1;

    private int in;
    private int out;
    private double weight;
    private boolean enabled;
    private int innovationCount;

    public NNConnection(int i, int o, double w, boolean e, int iC)
    {
        in = i;
        out = o;
        weight = w;
        enabled = e;
        innovationCount = iC;
    }

    public NNConnection(int i, int o, double w, boolean e)
    {
        in = i;
        out = o;
        weight = w;
        enabled = e;
        NNConnection.globalInnovation++;
        innovationCount = NNConnection.globalInnovation;
    }
    public int getIn() {
        return in;
    }
    public int getOut() {
        return out;
    }
    
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) { this.weight = weight; }

    public void resetWeight() {
        this.weight = new Random().nextDouble() * 2 * initBound - initBound;
    }

    public void permuteWeight() {
        this.weight += new Random().nextDouble() * 2 * initBound - initBound;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getInnovationCount() {
        return innovationCount;
    }

    public NNConnection copy()
    {
        return new NNConnection(in,out,weight,enabled,innovationCount);
    }

    @Override
    public String toString() {
        return "|"+ getIn() +"->" + getOut() +", w=" +getWeight()+", e="+isEnabled()+"| ";
    }
}
