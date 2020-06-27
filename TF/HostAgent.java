package TF;

import classes.BCell;
import classes.NCell;
import classes.TCell;
import classes.Virus;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.wrapper.PlatformController;
import frame.*;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import java.util.Map;

public class HostAgent extends Agent {
    public static MainFrame frame = null;
    //public static int number_of_N_cells = 100;
    public static int number_of_N_cells = 10;
    public static int number_of_I_cells = 0;
    public static int number_of_B_cells = 10;
    public static int number_of_T_cells = 10;
    //public static int number_of_Virus = 150;
    public static int number_of_Virus = 150;
    public static int number_of_Antibodies = 0;
    public static boolean ENABLED = false;
    public static boolean INFECTION = false;
    public static PlatformController container;
    
    @Override
    public void setup() {
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            DFService.register(this, dfd);
            //
            frame = new MainFrame(this);
            frame.setVisible(true);
            //
            container = getContainerController();
            for (int i = 0; i < number_of_N_cells; i++) {
                NCell ncell = new NCell();
                ncell.start();
            }
            for (int i = 0; i < number_of_B_cells; i++) {
                BCell bcell = new BCell();
                bcell.start();
            }
            for (int i = 0; i < number_of_T_cells; i++) {
                TCell tcell = new TCell();
                tcell.start();
            }
            for (int i = 0; i < number_of_Virus; i++) {
                Virus v = new Virus();
                v.start();
            }  
            //
            ParallelBehaviour parallel = new ParallelBehaviour();
            parallel.addSubBehaviour(new CyclicBehaviour(this) {
                @Override
                public void action() { 
                    MainFrame.panel_principal.repaint();
                    MainFrame.panel_stats.repaint();
                }
            });
            parallel.addSubBehaviour(new TickerBehaviour(this, 10000) {
                @Override
                public void onTick() { 
                    for (Map.Entry<String, NCell> entry : NCell.getNormalCells().entrySet()) {
                        NCell cell = entry.getValue();
                        NCell c = cell.reproduce();
                        if (c != null) {
                            c.start();
                            number_of_N_cells += 1;
                        }
                    }
                }
            });
            addBehaviour(parallel);
            ENABLED = true;
        }
        catch(Exception e) {
            System.err.println("exception " + e);
            e.printStackTrace();
        }
    }
}