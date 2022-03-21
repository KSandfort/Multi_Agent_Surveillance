package agents;

import Enities.Entity;
import Enities.EntityKnowledge;
import agents.NeuralNetworkUtil.NeuralNetwork;
import model.MapItem;
import model.Vector2D;

import java.util.ArrayList;

public class NEATAgent extends AbstractAgent
{
    private NeuralNetwork network;

    public NEATAgent(NeuralNetwork nn)
    {
        network = nn;
    }

    @Override
    public void addControls() {
        //not needed here
    }

    @Override
    public void changeMovement(ArrayList<MapItem> items)
    {
        Entity e = entityInstance;
        EntityKnowledge knowledge = e.getEntityKnowledge();
        int[][] mapKnowledge = knowledge.getMapRepresentation();
        double[] input = new double[mapKnowledge.length * mapKnowledge[0].length];

        int index = 0;
        for (int i = 0; i < mapKnowledge.length; i++) {
            for (int j = 0; j < mapKnowledge[0].length; j++) {
                input[index++] = mapKnowledge[i][j];
            }
        }

        double[] output = network.evaluate(input);
        Vector2D dir = e.getDirection();
        if(output[0] > 0.5)
        {
            e.setPosition(Vector2D.add(e.getPosition(), Vector2D.scalar(e.getDirection(), Entity.baseSpeedGuard)));
        }
        if(output[1] > 0.5)
        {
            dir.pivot(e.getTurnSpeed());
        }
        if(output[2] > 0.5)
        {
            dir.pivot(-e.getTurnSpeed());
        }
    }
}
