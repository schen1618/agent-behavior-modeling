import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Main
{
    private static Movement A = ((location, agent) -> {
        double b = 10000;  // 10^2 --> 10^-5, 10^6 --> 10^0
        double p = 1 / 510.0;
        //double q = 1.0 / ((double)Main.numAgents/(Main.gridSize*Main.gridSize));
        
        double E = (location.getLocationELevel() + 1) * p;
        double N = (location.getAgentsInLocationList().size() + 1) * (1.0 / Main.numAgents);
        return Math.exp(-1 * b * E * N);
    
        //BigDecimal a = new BigDecimal(-1 * p * location.getLocationELevel() * q * (location.getAgentsInLocationList().size() + 1));
        //System.out.println(a.doubleValue());
        //return Math.exp(b.multiply(a).doubleValue());
        
        //return Math.exp(-1 * p * e.getLocationELevel()) + Math.exp(-1 * q * e.getAgentsInLocationList().size());
    });
    
    // Change these parameters only
    static int numAgents = 50000;   // number of agents
    static final int gridSize = 50;   // length of grid (points)
    static final Environment.BoundaryType boundaryType = Environment.BoundaryType.TORUS; // bound types: BOUND, TORUS
    static final double eDiffThreshold = 10; //10, 15
    static final Movement movement = Main.A; // A, B
    private static final int numAdj = 4; // 4 or 8
    private static final int dangerAreaStart = 150; // between 0 and gridSize * 10
    private static final int dangerAreaEnd = 180; // between 0 and gridSize * 10
    static final int displayContrast = 4; // recommend between 2-4, depends on number of agents
    static final double decayRate = 1;
    static final java.util.List<Point> dangerArea = new ArrayList<>();//generateDangerArea(dangerAreaStart, dangerAreaEnd);
    static final java.util.List<Point> dangerAreaLeft = generateDangerArea_vertical(20);
    static final java.util.List<Point> dangerAreaRight = generateDangerArea_vertical(400);  //510
    //
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Crowd Modeling");
        frame.setContentPane(new GUI().$$$getRootComponent$$$());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //run();
    }
    
    public static void run()
    {
        Environment e;
        
        if(numAdj == 4)
        {
            e = new EnvFour(Main.dangerArea, Main.dangerAreaLeft, Main.dangerAreaRight); // 4 neighbors
        }
        else
        {
            e = new EnvEight(Main.dangerArea, Main.dangerAreaLeft, Main.dangerAreaRight); // 8 neighbors
        }
        
        Display emotion = new EmotionDisplay(e);
        Display density = new DensityDisplay(e);
        display(emotion, new Color(69, 69, 69));
        display(density, new Color(255, 255, 255));
        
        Thread thread = new Thread(() -> {
            while(true)
            {
                try
                {
                    e.calcLocationELevel();   //avg e level from agents in location
                    e.calcAgentELevel();  //avg e level from adj locations and find adj locations to move
                    e.moveAgents();   //move agents and add/remove from location in list
                    emotion.repaint();
                    density.repaint();
                    Thread.sleep(50);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }
    
    /**
     * Generates "danger" area, square
     *
     * @return list of danger area points
     */
    public static java.util.List<Point> generateDangerArea(int start, int end)
    {
        int gridUnitSize = 10;
        List<Point> dangerArea = new ArrayList<>();
        for(int i = start; i < end; i += gridUnitSize)
        {
            for(int j = start; j < end; j += gridUnitSize)
            {
                dangerArea.add(new Point(i, j));
            }
        }
        
        return dangerArea;
    }
    
    public static java.util.List<Point> generateDangerArea_vertical(int x)
    {
        int gridUnitSize = 10;
        List<Point> dangerArea = new ArrayList<>();
        
        
        for(int i = 20; i < (gridSize * gridUnitSize) + 20; i += gridUnitSize)
        {
            dangerArea.add(new Point(x, i));
            dangerArea.add(new Point(x+gridUnitSize, i));
            dangerArea.add(new Point(x + (gridUnitSize * 2), i));
            dangerArea.add(new Point(x + (gridUnitSize * 3), i));
            dangerArea.add(new Point(x + (gridUnitSize * 4), i));
            dangerArea.add(new Point(x + (gridUnitSize * 5), i));
        }
        
        return dangerArea;
    }
    
    private static int numWin = 0;  // number of windows (for nice display)
    
    /**
     * Creates JFrame and formats
     *
     * @param d display
     * @param c color of display background
     */
    public static void display(Display d, Color c)
    {
        JFrame frame = new JFrame();
        frame.setTitle(d.getClass().getSimpleName());
        frame.setContentPane(d);
        frame.getContentPane().setBackground(c);
        frame.setSize((gridSize + 1) * 10 + 50, (gridSize + 1) * 10 + 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(numWin * gridSize * 11, 0);
        numWin++;
        frame.setVisible(true);
    }
}