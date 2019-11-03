import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Main
{
    static int numAgents = 50000;   // number of agents
    static int gridSize = 50;   // length of grid (points)
    static String agentType = "AgentA"; // AgentA, AgentB, ...
    static String boundType = "torus"; // bound types: "bound", "torus"
    private static int numWin = 0;  // number of windows (for nice display)
    
    public static void main(String[] args)
    {
        java.util.List<Point> dangerArea = generateDangerArea(150, 200);
        Environment e = new Environment(gridSize, numAgents, agentType, dangerArea, boundType);
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
                    e.calcAgentELevel(boundType);  //avg elevel from adj locations and find adj locations to move
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
