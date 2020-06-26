package classes;

import static classes.Cell.Cells;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.QuadCurve2D;
import java.util.Map;
import java.util.TreeMap;

public class Antibody extends Entity {
    private static TreeMap<String,Antibody> Antibodies = new TreeMap<String,Antibody>();
    public static void addAntibody(Antibody a) { Antibodies.put(a.getName(), a); }
    public static Antibody getLocal(String name) { return Antibodies.get(name); }
    public static TreeMap<String, Antibody> getAntibodies() { return Antibodies;  }
    
    public static void paintAllAntibodies(Graphics g) {        
        try {
            for (Map.Entry<String, Antibody> entry : Antibodies.entrySet())
            entry.getValue().paint(g);
        } catch(Exception e) { }
    }
    
    public enum StatusAntibody { MOVING, GOINGTO, ATTACKING; }
    
    public StatusAntibody status;
    
    public Antibody() {
        super();
        color = Color.CYAN;
        radius = radius / 2;
        speed = 10;
        //entity_type = "Antibody";
        status = StatusAntibody.MOVING;
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        Graphics2D G2D = (Graphics2D)g;
        G2D.setStroke(new BasicStroke(3.0f));
        QuadCurve2D QC2D = new QuadCurve2D.Float(position.getX(), position.getY(), position.getX(), position.getY() + 10, position.getX() + 10, position.getY() + 10);
        G2D.draw(QC2D);
        QuadCurve2D QC2D2 = new QuadCurve2D.Float(position.getX(), position.getY(), position.getX(), position.getY() + 10, position.getX() - 10, position.getY() + 10);
        G2D.draw(QC2D2);
        G2D.setColor(Color.red);
    }
}