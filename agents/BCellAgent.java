package agents;

import TF.HostAgent;
import TF.Position;
import classes.Antibody;
import static classes.Cell.StatusCell.GOINGTO;
import static classes.Cell.StatusCell.MOVING;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.util.Random;

public class BCellAgent extends CellAgent {
    
    public int numero_anticuerpos = 5;
    @Override
    protected void setup() {
        super.setup();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("BCellAgent");
            sd.setName("BCellAgentDescription");
        parallel.addSubBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() { 
                if (HostAgent.INFECTION) {
                    for(int i=0; i < numero_anticuerpos;i++){
                        Antibody antib = new Antibody();
                        antib.position = new Position(getLocal().position.getX(),getLocal().position.getY());
                        antib.start();
                        HostAgent.number_of_Antibodies+=1;
                    }   
                }
            }
        });
        
        parallel.addSubBehaviour(new WakerBehaviour(this, 8000) {
            @Override
            protected void onWake() {
                getLocal().status = GOINGTO;
                getLocal().setGoal(new Position(20,50));
            }
           
        });
    }
    
    @Override
    protected void computeStatus() {
        if(getLocal()!= null){
            switch(getLocal().status) {
                case MOVING: movementAgent();
                    break;
                case GOINGTO:
                    getLocal().goToGoal();
                    if(getLocal().hasArrived()){
                        getLocal().status = MOVING;
                    }
                    break;
                default:
                    break;
        }
        }
    }

    @Override
    protected void computeMessage(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        switch(msg.getContent()){
            case "patrullando":
                System.out.println(getLocalName() + ": mensaje de " + msg.getSender().getLocalName());
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent("recibido");
                break;
            case "encontrado":
                System.out.println(msg.getContent());
                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                reply.setContent("ataquen a la posicion: ");
            default:
                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                reply.setContent("desconocido");
                break;
        }
        send(reply);
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
}