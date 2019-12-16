/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mazatlab.ia.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author zchumager
 */
public class SubstractionAgent extends Agent {
    protected void setup() {        
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                
                if(msg != null) {
                    String content = msg.getContent();
                    
                    //System.out.println(content);
                    
                    String[] numbers = content.split(",");
                    
                    int c = Integer.parseInt(numbers[0]) - Integer.parseInt(numbers[1]);
                    
                    ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                    reply.addReceiver(msg.getSender());
                    reply.setContent(String.valueOf(c));
                    send(reply);
                }
                
                block();
            }
        });
    }
    
}
