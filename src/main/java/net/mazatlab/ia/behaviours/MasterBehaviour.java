/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mazatlab.ia.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import java.util.Stack;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author zchumager
 * 
 *  EXPRESSIONS:
 *      2 + 4 + ( 3 * 4 ) - ( 2 * 5 )
 *      4 + ( 5 * 10 ) / 2
 * 
 */

public class MasterBehaviour extends SimpleBehaviour {
    private Agent agent;
    private ACLMessage userInput;
    private char[] tokens;
        
    // Stack for numbers: 'values'
    Stack<Integer> values = new Stack<>();
 
    // Stack for Operators: 'ops'
    Stack<Character> ops = new Stack<>();
        
    public MasterBehaviour(Agent agent) {
        super(agent);
        this.agent = agent;
    }
        
    @Override
    public void action() {
        //loop method
        
        this.userInput = myAgent.receive();
        
        if(this.userInput != null) {
            String exp = this.userInput.getContent();
            System.out.println("EXPRESION RECIBIDA : " + exp);
            resolve(exp);
        } else {
            System.out.println("NINGUNA EXPRESION RECIBIDA");
            block();
        }
    }
    
    @Override
    public boolean done() {
        //stops action method
        return false;
    }
    

    public void resolve(String expression) {
        
        this.tokens = expression.toCharArray();
        
        char[] tokens = expression.toCharArray();
        
        // Stack for numbers: 'values'
        Stack<Integer> values = new Stack<>();
 
        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<>();
        
        for (int i = 0; i < tokens.length; i++)
        {
            // Current token is a whitespace, skip it
            if (tokens[i] == ' ')
                continue;
 
            // Current token is a number, push it to stack for numbers
            if (tokens[i] >= '0' && tokens[i] <= '9')
            {
                StringBuffer sbuf = new StringBuffer();
                // There may be more than one digits in number
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
                    sbuf.append(tokens[i++]);
                values.push(Integer.parseInt(sbuf.toString()));
            }
 
            // Current token is an opening brace, push it to 'ops'
            else if (tokens[i] == '(')
                ops.push(tokens[i]);
 
            // Closing brace encountered, solve entire brace
            else if (tokens[i] == ')')
            {
                while (ops.peek() != '(')
                  values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.pop();
            }
 
            // Current token is an operator.
            else if (tokens[i] == '+' || tokens[i] == '-' ||
                     tokens[i] == '*' || tokens[i] == '/')
            {
                // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                  values.push(applyOp(ops.pop(), values.pop(), values.pop()));
 
                // Push current token to 'ops'.
                ops.push(tokens[i]);
            }
        }
        
        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
 
        // Top of 'values' contains result, return it
        System.out.println("RESULTADO " + values.pop());   
    }
    
    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    public boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }
    
    // A utility method to apply an operator 'op' on operands 'a' 
    // and 'b'. Return the result.
    public int applyOp(char op, int b, int a) {
        int result = 0;
        ACLMessage msg;
        msg = new ACLMessage(ACLMessage.INFORM);
            
        switch (op) {
            case '+':
                msg.addReceiver(new AID("Addition", AID.ISLOCALNAME));
                msg.setContent(a + "," + b);
                agent.send(msg);
                ACLMessage additionReply;
                
                do {
                    additionReply = agent.receive();
                } while(additionReply == null);
                
                if(additionReply != null) {
                    //System.out.println("REPLY " + additionReply.getContent());
                    return Integer.parseInt(additionReply.getContent());
                }
                break;
            case '-':
                msg.addReceiver(new AID("Substraction", AID.ISLOCALNAME));
                msg.setContent(a + "," + b);
                agent.send(msg);
                
                ACLMessage substractionReply;
                
                do {
                    substractionReply = agent.receive();
                } while(substractionReply == null);
                
                if(substractionReply != null) {
                    //System.out.println("REPLY " + substractionReply.getContent());
                    return Integer.parseInt(substractionReply.getContent());
                }
                break;
            case '*':
                msg.addReceiver(new AID("Multiplication", AID.ISLOCALNAME));
                msg.setContent(a + "," + b);
                agent.send(msg);
                
                ACLMessage multiplicationReply;
                
                do {
                    multiplicationReply = agent.receive();
                } while(multiplicationReply == null);
                
                if(multiplicationReply != null) {
                    //System.out.println("REPLY " + multiplicationReply.getContent());
                    return Integer.parseInt(multiplicationReply.getContent());
                }
                break;
            case '/':
                if (b == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                
                msg.addReceiver(new AID("Division", AID.ISLOCALNAME));
                msg.setContent(a + "," + b);
                agent.send(msg);
                
                ACLMessage divisionReply;
                
                do {
                    divisionReply = agent.receive();
                }while(divisionReply == null);
                
                if(divisionReply != null) {
                    //System.out.println("REPLY " + divisionReply.getContent());
                    return Integer.parseInt(divisionReply.getContent());
                }
        }
        return result;
    }
}