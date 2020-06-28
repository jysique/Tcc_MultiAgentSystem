package agents;

import TF.HostAgent;
import TF.Position;
import classes.Antibody;
import classes.BCell;
import static classes.Cell.StatusCell.GOINGTO;
import static classes.Cell.StatusCell.MOVING;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Arrays;
import java.util.Random;

public class BCellAgent extends CellAgent {
    public int anticuerpos_attacking = 0;
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
                case MOVING: 
                    movementAgent();
                    break;
                case GOINGTO:
                    getLocal().goToGoal();
                    if(getLocal().hasArrived()){
                       getLocal().status=BCell.StatusCell.MOVING;
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
        
        if(msg.getContent().contains("ATTACK")){
            //System.out.println("=>"+msg.getContent());
            String[] pos = msg.getContent().split(":",4);
            int posX = Integer.parseInt(pos[1]);
            int posY = Integer.parseInt(pos[2]);
            getLocal().status = BCell.StatusCell.GOINGTO;
            //getLocal().setGoal(new Position(posX,posY));
            //getLocal().setGoal(new Position(getLocal().position.getX(),posY));
            getLocal().setGoal(new Position(posX,getLocal().position.getY()));
            
            reply.setContent("GO");
            send(reply);
            anticuerpos_attacking++;
        }
        else if(msg.getContent().contains("DEAL")){
            //System.out.println("=>" + msg.getSender()+ " acepto");
            if(anticuerpos_attacking <= 5){
                
                reply.setContent("ACCEPT");
            }else{
                anticuerpos_attacking = 0; //para que no se quede todo el tiempo en 5;
                System.out.println("limite");
                //SI LLEGA A 5 DENIED
                reply.setContent("DENIED");
            }
            send(reply);
            
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
}