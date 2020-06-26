package agents;

import TF.Functions;
import static classes.Antibody.StatusAntibody.MOVING;
import classes.Virus;
import classes.NCell;
import classes.Cell;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.util.Map;
import java.util.Random;

public class TCellAgent extends CellAgent {   
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    
    @Override
    protected void setup() {
        super.setup();
        parallel.addSubBehaviour(new TickerBehaviour(this,100){
            @Override
            protected void onTick() {
                msg.setContent("patrullando");
                msg.addReceiver(getAID());
                send(msg);
                }
            });
    }
    
    @Override
    protected void computeStatus() {
        if(getLocal()!= null){
            switch(getLocal().status) {
                case MOVING:
                    movementAgent();
                    eliminar_cel_infected();
                    break;
            /*case INFECTED: 
                break;*/
                case ATTACKING: 
                    break;
                case GOINGTO:
                    
                    break;
            /*case DEAD:
                break;*/
        }
        }
    }

    @Override
    protected void computeMessage(ACLMessage msg) {
        if(getLocal()!=null){
            ACLMessage reply = msg.createReply();
            switch(msg.getContent()){
                case "patrullando":
                    System.out.println(getLocalName() + ": mensaje de " + msg.getSender().getLocalName());
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("recibido");
                    send(reply);
                    break;
                case "recibido":
                    System.out.println( getLocalName() + "recibio mensaje de " + msg.getSender().getLocalName());
                    break;
                case "infectado":
                    System.out.println(msg);
                    break;
            }
            //System.out.println("<= " + reply);
            //send(reply);
        }
        
    }
    
    
        //COMBINACION DE MOV
    public void movementAgent(){
        Random aleatorio = new Random(System.currentTimeMillis());
        if (aleatorio.nextInt(100) <= 30) {
            getLocal().movementRandom();
        }else if(aleatorio.nextInt(100) > 30 && aleatorio.nextInt(100) <= 60){
            getLocal().movementLeftRight();
        }else{
            getLocal().movementUpDown();
        }
    }

    public void eliminar_cel_infected(){
        for (Map.Entry<String, NCell> entry : NCell.getNormalCells().entrySet()) {
                NCell ncell = entry.getValue();
                if (Functions.isClose(getLocal().position, ncell.position)) {
                    String msg_temp = "=> Aqui "+ getLocalName() +" encontrado un infectado en "+ getLocal().position.getX() + " ; " + getLocal().position.getY() ;
                    msg.setContent(msg_temp);
                    msg.addReceiver(getAID());
                    send(msg);
                    getLocal().status = Cell.StatusCell.MOVING;
                    ncell.status = Cell.StatusCell.DEAD;
                    break;
                }
        }
    }
    
    
}