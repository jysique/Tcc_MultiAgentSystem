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
import java.util.Arrays;
import java.util.Random;

public class BCellAgent extends CellAgent {
    
    public int numero_anticuerpos = 5;
    @Override
    protected void setup() {
        super.setup();
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
    }
    
    @Override
    protected void computeStatus() {
        if(getLocal()!= null){
            switch(getLocal().status) {
                case MOVING: movementAgent();
                    break;
                case GOINGTO:
                    break;
                default:
                    break;
        }
        }
    }

    @Override
    protected void computeMessage(ACLMessage msg) {
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