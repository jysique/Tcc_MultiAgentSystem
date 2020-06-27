package agents;

import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Random;

public class TCellAgent extends CellAgent {   
    @Override
    protected void setup() {
        super.setup();
    }
    
    @Override
    protected void computeStatus() {
        if(getLocal()!= null){
            switch(getLocal().status) {
                case MOVING:
                    movementAgent();
                    break;
                case ATTACKING:
                    break;
                case GOINGTO:
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