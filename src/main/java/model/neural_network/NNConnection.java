package model.neural_network;

import java.util.Random;

public class NNConnection {
    
    final double initBound = 2;
    public static int globalInnovation = 1;

    private int in;
    private int out;
    private double weight;
    private boolean enabled;
    private int innovationCount;

    //i = input node, 0 = output node, w = weight, e = enabled? yes/no, iC = innovation count
    public NNConnection(int i, int o, double w, boolean e, int iC)//constructor used for copying
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
        innovationCount = NNConnection.globalInnovation;
        NNConnection.globalInnovation++;
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

    //give the weight of the connection a completely new random value
    public void resetWeight() {
        this.weight = new Random().nextDouble() * 2 * initBound - initBound;
    }

    //change the value of the weight randomly
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

    //returns a copy of the connection
    public NNConnection copy()
    {
        return new NNConnection(in,out,weight,enabled,innovationCount);
    }

    @Override
    public String toString() {
        return "|"+ getIn() +"->" + getOut() +", w=" +getWeight()+", e="+isEnabled()+", i="+getInnovationCount()+"| ";
    }
}
