package agents;

import TF.Functions;
import TF.Position;
import classes.BCell;
import classes.NCell;
import classes.TCell;
import classes.Virus;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.Map;
import java.util.Random;

public class TCellAgent extends CellAgent {   
    @Override
    protected void setup() {
        super.setup();
    }
    
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
                       getLocal().status=TCell.StatusCell.MOVING;
                    }
                    break;
                case GOINGTO:
                    getLocal().goToGoal();
                    if(getLocal().hasArrived()){
                       getLocal().status=TCell.StatusCell.MOVING;
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
            //getLocal().status = TCell.StatusCell.ATTACKING;
            //getLocal().setGoal(new Position(posX,posY));
            //getLocal().setGoal(new Position(getLocal().position.getX(),posY));
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
    public void patrullando(){
        for (Map.Entry<String, NCell> entry : NCell.getNormalCells().entrySet()) {
            NCell c = entry.getValue();
            if(Functions.isClose(getLocal().position, c.position) &&  c.status==NCell.StatusCell.INFECTED){
                c.status = NCell.StatusCell.DEAD;
                ACLMessage m_to_tcells = new ACLMessage(ACLMessage.INFORM);
                m_to_tcells.setContent("ATTACK:" + c.position.getX() + ":" + c.position.getY());
                for(Map.Entry<String,TCell> entry_t : TCell.getTCells().entrySet()){
                    m_to_tcells.addReceiver(new AID(entry_t.getValue().getName(), AID.ISLOCALNAME));
                }
                send(m_to_tcells);
                break;
            }
        }
        
        for (Map.Entry<String, Virus> entry : Virus.getActiveVirus().entrySet()) {
            Virus v = entry.getValue();
            if(Functions.isClose(getLocal().position, v.position) &&  v.status==Virus.StatusVirus.INFECTING){
                //v.status = Virus.StatusVirus.DEAD;
                //System.out.println(HostAgent.number_of_I_cells);
                ACLMessage m_to_bcell = new ACLMessage(ACLMessage.INFORM);
                m_to_bcell.setContent("ATTACK:" + v.position.getX() + ":" + v.position.getY());
                for(Map.Entry<String,BCell> entry_b :BCell.getBCells().entrySet()){
                    m_to_bcell.addReceiver(new AID(entry_b.getValue().getName(), AID.ISLOCALNAME));
                }
                send(m_to_bcell);
                break;
            }
        }
    }
}