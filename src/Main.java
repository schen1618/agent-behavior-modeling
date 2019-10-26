import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Main
{
    static int numAgents = 50000;
    static int gridSize = 50;
    static String agentType = "AgentA";
    
    public static void main(String[] args)
    {
        Environment e1 = new Environment(gridSize + 1, numAgents, agentType, generateDangerArea());
        Environment e2 = new EnvironmentDensity(gridSize + 1, numAgents, agentType, generateDangerArea());
        display(e1, new Color(69, 69, 69));
        display(e2, new Color(255, 255, 255));
    }
    
    public static java.util.List<Point> generateDangerArea()
    {
        int gridUnitSize = 10;
        //Point[] p = {new Point(40, 40), new Point(40, 50), new Point(50, 40), new Point(50, 50)};
        List<Point> dangerArea = new ArrayList<>();
        for(int i = 150; i < 200; i += gridUnitSize)
        {
            for(int j = 150; j < 200; j += gridUnitSize)
            {
                dangerArea.add(new Point(i, j));
            }
        }
    
        return dangerArea;
    }
    
    public static void display(Environment e, Color c)
    {
        JFrame frame = new JFrame();
        frame.setContentPane(e);
        frame.getContentPane().setBackground(c);
        frame.setSize((gridSize + 1) * 10 + 50, (gridSize + 1) * 10 + 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
