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

    public static NNConnection readConnectionFromString(String input)
    {
        String[] dataSubstrings = new String[5];
        char[] text = input.toCharArray();
        int prevIndex = 1;
        int subStringIndex = 0;
        for (int i = 1; i < text.length; i++)
        {
            if(text[i] == ',' || text[i] == '|')
            {
                dataSubstrings[subStringIndex++] = input.substring(prevIndex,i);
                prevIndex = i+1;
            }
        }
        int in = Integer.parseInt(dataSubstrings[0]);
        int out = Integer.parseInt(dataSubstrings[1]);
        double w = Double.parseDouble(dataSubstrings[2].substring(2));
        boolean e = Boolean.parseBoolean(dataSubstrings[3].substring(2));
        int i = Integer.parseInt(dataSubstrings[4].substring(2));
        return new NNConnection(in,out,w,e,i);
    }

    @Override
    public String toString() {
        return "|"+ getIn() +"," + getOut() +",w=" +getWeight()+",e="+isEnabled()+",i="+getInnovationCount()+"|";
    }
}
