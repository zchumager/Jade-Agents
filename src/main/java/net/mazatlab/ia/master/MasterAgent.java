package net.mazatlab.ia.master;

import jade.core.Agent;
import net.mazatlab.ia.behaviours.MasterBehaviour;

/**
 *
 * @author zchumager
 */

public class MasterAgent extends Agent{
    
    @Override
    protected void setup() { 
        addBehaviour(new MasterBehaviour(this));
    }
}

/*
    https://www.youtube.com/watch?v=aNh0RXivCwI
*/