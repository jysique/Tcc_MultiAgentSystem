package agents;

import TF.Functions;
import TF.HostAgent;
import TF.Position;
import classes.Antibody;
import classes.Virus;
import jade.core.AID;
import static classes.Antibody.StatusAntibody.MOVING;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.util.Map;
import java.util.Random;

public class AntibodyAgent extends EntityAgent {

    @Override
    protected void setup() {
        super.setup();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("AnitBodyAgent");
        sd.setName("AntiBodyAgentDescription");
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
            }
        }
    }

    @Override
    protected void computeMessage(ACLMessage msg) { }
    
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
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("patrullando");
        //System.out.println("hola1");
        //System.out.println("Aqui"+ getLocalName() +" patrullando");
        for (Map.Entry<String, Virus> entry : Virus.getActiveVirus().entrySet()) {
                Virus virus = entry.getValue();
                //if (virus.status == Virus.StatusVirus.INFECTING) { 
                    //System.out.println("hola1");
                if (Functions.isClose(getLocal().position, virus.position) &&  virus.status!=Virus.StatusVirus.DEAD) {
                    String msg_temp = "Aqui "+ getLocalName() +" encontrado un virus en "+ getLocal().position.getX() + " ; " + getLocal().position.getY() ;
                    //System.out.println(msg_temp);
                    msg.setContent(msg_temp);
                    eliminar_virus(virus);
                    getLocal().status = MOVING;
                    break;
                }
                //}
        }
        msg.addReceiver(new AID("BCell",AID.ISLOCALNAME));
        msg.addReceiver(new AID("NCell",AID.ISLOCALNAME));
        msg.addReceiver(new AID("Cell",AID.ISLOCALNAME));
        send(msg);
    }
    public void eliminar_virus(Virus virus){
        //System.out.println("murio el we " + virus.getName());
        virus.status = Virus.StatusVirus.DEAD;
        //HostAgent.number_of_Virus -= 1;

        getLocal().status = Antibody.StatusAntibody.MOVING;
    }
    public void attack_to_position(Position pos){
        
    }
}