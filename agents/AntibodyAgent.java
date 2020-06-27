package agents;

import TF.Functions;
import TF.HostAgent;
import TF.Position;
import classes.Antibody;
import classes.Virus;
import jade.core.AID;
import static classes.Antibody.StatusAntibody.MOVING;
import static classes.Antibody.StatusAntibody.GOINGTO;
import classes.BCell;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class AntibodyAgent extends EntityAgent {
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    
    @Override
    protected void setup() {
        super.setup();
    }
    
    public Antibody getLocal() { return Antibody.getLocal(getLocalName()); }
    
    @Override
    protected void computeStatus() { 
        if(getLocal()!=null){
            switch(getLocal().status) {
                case MOVING: 
                    movementAgent();
                    patrullando();
                    break;
                case ATTACKING:
                    getLocal().goToGoal();
                    if(getLocal().hasArrived()){
                       getLocal().status=Antibody.StatusAntibody.MOVING;
                    }
                    break;
                case GOINGTO:
                    getLocal().goToGoal();
                    if(getLocal().hasArrived()){
                       getLocal().status=Antibody.StatusAntibody.MOVING;
                    }
                    break;       
            }
        }
        
    }

    @Override
    protected void computeMessage(ACLMessage msg) {
        if(msg.getContent().contains("ATTACK")){
            //System.out.println("=>"+msg.getContent());
            String[] pos = msg.getContent().split(":",4);
            int posX = Integer.parseInt(pos[1]);
            int posY = Integer.parseInt(pos[2]);
            getLocal().status = Antibody.StatusAntibody.ATTACKING;
            getLocal().setGoal(new Position(posX,posY));
            //getLocal().setGoal(new Position(getLocal().position.getX(),posY));
        }
    }
    
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
    public void patrullando(){
        for (Map.Entry<String, Virus> entry : Virus.getActiveVirus().entrySet()) {
            Virus v = entry.getValue();
            if(Functions.isClose(getLocal().position, v.position) &&  v.status!=Virus.StatusVirus.DEAD){
                v.status = Virus.StatusVirus.DEAD;
                ACLMessage m_to_antibody = new ACLMessage(ACLMessage.INFORM);
                m_to_antibody.setContent("ATTACK:" + v.position.getX() + ":" + v.position.getY());
                for(Map.Entry<String,Antibody> entry_a : Antibody.getAntibodies().entrySet()){
                    m_to_antibody.addReceiver(new AID(entry_a.getValue().getName(), AID.ISLOCALNAME));
                }
                send(m_to_antibody);
                break;
            }
        }
    }
    
    public void eliminar_virus(Virus virus){
    }
}