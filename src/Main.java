import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Main
{
    static final int numAgents = 50000;   // number of agents
    static final int gridSize = 50;   // length of grid (points)
    static Movement A = ((location, agent) -> {
        double p = 1 / 510.0;
        double q = 1.0; // Main.numAgents;
        return Math.exp(-1 * p * location.getLocationELevel() * (q * (location.getAgentsInLocationList().size() + 1)));
        //return Math.exp(-1 * p * e.getLocationELevel()) + Math.exp(-1 * q * e.getAgentsInLocationList().size());
    });
    static Movement B = ((location, agent) -> {
        double p = 1 / 510.0;
        double q = 1.0; // Main.numAgents;
        return Math.exp(-1 * p * location.getLocationELevel() * (q * (location.getAgentsInLocationList().size() + 1)) * p * agent.geteLevel());
    });
    static Environment.BoundaryType boundaryType = Environment.BoundaryType.BOUND; // bound types: BOUND, TORUS
    private static int numWin = 0;  // number of windows (for nice display)
    
    public static void main(String[] args)
    {
        java.util.List<Point> dangerArea = generateDangerArea(150, 250);
        Environment e = new EnvEight(gridSize, numAgents, B, dangerArea, boundaryType); // EnvFour or EnvEight (neighbors)
        Display emotion = new EmotionDisplay(e);
        Display density = new DensityDisplay(e);
        display(emotion, new Color(69, 69, 69));
        display(density, new Color(255, 255, 255));
        
        Thread thread = new Thread(() -> {
            while(true)
            {
                try
                {
                    e.calcLocationELevel();   //avg elevel from agents in location
                    e.calcAgentELevel();  //avg elevel from adj locations and find adj locations to move
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
