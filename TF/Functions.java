package TF;

import static TF.HostAgent.container;
import jade.wrapper.AgentController;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.LookupOp;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class Functions {
    
    /*private static ArrayList<Integer> randomIDs = new ArrayList<Integer>();
    public static int randomID() {
        int id = (int) (10000 * Math.random());
        while (randomIDs.contains(id))
            id = (int) (10000 * Math.random());
        randomIDs.add(id);
        return id;
    }*/
    
    private static ArrayList<String> randomIDs = new ArrayList<String>();
    
    private static String randomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        //int targetStringLength = 5;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        //System.out.println(generatedString);
        return generatedString;
    }
    
    public static String randomString() {
        String s = randomString(10);
        /*while (randomIDs.contains(s))
            s = randomString(10);
        randomIDs.add(s);*/
        return s;
    }
    
    public static boolean isClose(Position p1, Position p2) { return p1.isClose(p2, 80); }
    
    public static void createAgent(String localname, String type) {
        try {
            AgentController ac = container.createNewAgent(localname, type, null);
            ac.start();
        } catch(Exception e) {
            System.err.println("exception " + e);
            e.printStackTrace();
        }
    }
    
    public static Shape shapeStar(double centerX, double centerY,
        double innerRadius, double outerRadius, int numRays,
        double startAngleRad)
    {
        Path2D path = new Path2D.Double();
        double deltaAngleRad = Math.PI / numRays;
        for (int i = 0; i < numRays * 2; i++)
        {
            double angleRad = startAngleRad + i * deltaAngleRad;
            double ca = Math.cos(angleRad);
            double sa = Math.sin(angleRad);
            double relX = ca;
            double relY = sa;
            if ((i & 1) == 0) {
                relX *= outerRadius;
                relY *= outerRadius;
            } else {
                relX *= innerRadius;
                relY *= innerRadius;
            }
            if (i == 0)
                path.moveTo(centerX + relX, centerY + relY);
            else
                path.lineTo(centerX + relX, centerY + relY);
        }
        path.closePath();
        return path;
    }
    
    public static Image cargarImagen(String path) {
        try {
            Image imagen = ImageIO.read(new File(path));
            imagen = makeColorTransparent(imagen, Color.WHITE);
            return imagen;
        } catch (IOException e) { e.printStackTrace(); return null; }
    }
    
    private static Image makeColorTransparent(Image im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;            
            @Override
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB)
                    return 0x00FFFFFF & rgb;
                else 
                    return rgb;
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
    
    public static Image replaceColor(Image img, Color color_inicial, Color color_final) {
        BufferedImageOp lookup = new LookupOp(new ColorMapper(color_inicial, color_final), null);
        return lookup.filter(toBufferedImage(img), null);
    }
    
    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage)
            return (BufferedImage) img;
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        // Return the buffered image
        return bimage;
    }
    
    private Polygon makeShape(int px, int py, int numberOfNodes, double minRadius, double maxRadius) {
        // Split a full circle into numberOfNodes step, this is how much to advance each part
        double angleStep = Math.PI * 2 / numberOfNodes;
        Polygon localshape = new Polygon();
        Random rnd = new Random(System.currentTimeMillis());
        for(int i = 0; i < numberOfNodes; ++i) {
            double targetAngle = angleStep * i; // This is the angle we want if all parts are equally spaced
            double angle = targetAngle + (rnd.nextDouble() - 0.5) * angleStep * 0.25; // add a random factor to the angle, which is +- 25% of the angle step
            double r = minRadius + rnd.nextDouble() * (maxRadius - minRadius); // make the radius random but within minRadius to maxRadius
            // calculate x and y positions of the part point
            double x = Math.cos(angle) * r; 
            double y = Math.sin(angle) * r;
            localshape.addPoint((int)x, (int)y);
        }
        localshape.translate(px, py);
        return localshape;
    }
    
    /*public static void agent_exit_actionPerformed(ActionEvent e, AgenteAnfitrion a) {
        a.addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() { ((AgenteAnfitrion) myAgent).terminarBatalla(); }
        });
    }*/  
}