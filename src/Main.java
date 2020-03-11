import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Main
{
    private static Movement A = ((location, agent) -> {
        //b = 10000;  // 10^2 --> 10^-5, 10^6 --> 10^0
        double p = 1 / 510.0;
        double E = (location.getLocationELevel() + 1) * p;
        double N = (location.getAgentsInLocationList().size() + 1) * (1.0 / Main.numAgents);
        return Math.exp(-1 * Main.b * E * N);
    });
    
    // Change these parameters only
    static double b = 10000;
    static int numAgents = 50000;   // number of agents
    static int gridSize = 50;   // length of grid (points)
    static Environment.BoundaryType boundaryType = Environment.BoundaryType.TORUS; // bound types: BOUND, TORUS
    static final double eDiffThreshold = 10; //10, 15
    static final Movement movement = Main.A; // A, B
    static int numAdj = 4; // 4 or 8
    private static final int dangerAreaStart = 150; // between 0 and gridSize * 10
    private static final int dangerAreaEnd = 180; // between 0 and gridSize * 10
    static int displayContrast = 4; // recommend between 2-4, depends on number of agents
    static final double decayRate = 1;
    static final java.util.List<Point> dangerArea = new ArrayList<>();//generateDangerArea(dangerAreaStart,
    // dangerAreaEnd);
    static final java.util.List<Point> dangerAreaLeft = generateDangerArea_vertical(20);
    static final java.util.List<Point> dangerAreaRight = generateDangerArea_vertical(400);  //510
    //
    
    static boolean hasEmotionDisplay = true;
    static boolean hasDensityDisplay = true;
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Crowd Modeling");
        frame.setContentPane(new GUI().$$$getRootComponent$$$());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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
        
        Display emotion = null;
        Display density = null;
        int densityIndex = 2;
        
        if(!hasEmotionDisplay)
        {
            densityIndex = 1;
        }
        
        if(hasEmotionDisplay)
        {
            emotion = new EmotionDisplay(e);
            display(emotion, new Color(69, 69, 69), 1);
        }
        if(hasDensityDisplay)
        {
            density = new DensityDisplay(e);
            display(density, new Color(255, 255, 255), densityIndex);
        }
        
        Display finalEmotion = emotion;
        Display finalDensity = density;
        
        Thread thread = new Thread(() -> {
            while(true)
            {
                try
                {
                    e.calcLocationELevel();   //avg e level from agents in location
                    e.calcAgentELevel();  //avg e level from adj locations and find adj locations to move
                    e.moveAgents();   //move agents and add/remove from location in list
                    repaintDisplays(finalEmotion, finalDensity);
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
    
    public static void repaintDisplays(Display emotion, Display density)
    {
        if(emotion != null)
        {
            emotion.repaint();
        }
        if(density != null)
        {
            density.repaint();
        }
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
            dangerArea.add(new Point(x + gridUnitSize, i));
            dangerArea.add(new Point(x + (gridUnitSize * 2), i));
            dangerArea.add(new Point(x + (gridUnitSize * 3), i));
            dangerArea.add(new Point(x + (gridUnitSize * 4), i));
            dangerArea.add(new Point(x + (gridUnitSize * 5), i));
        }
        
        return dangerArea;
    }
    
    public static int numWin = 1;
    
    /**
     * Creates JFrame and formats
     *
     * @param d display
     * @param c color of display background
     */
    public static void display(Display d, Color c, int windowIndex)
    {
        JFrame frame = new JFrame();
        frame.setTitle(d.getClass().getSimpleName());
        frame.setContentPane(d);
        frame.getContentPane().setBackground(c);
        frame.setSize((gridSize + 1) * 10 + 50, (gridSize + 1) * 10 + 50);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation((windowIndex * (gridSize * 11)) - 200, 0);
        frame.setVisible(true);
    }
}