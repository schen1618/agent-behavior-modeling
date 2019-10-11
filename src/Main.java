import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        generate(50, 5000);
    }
    
   public static void generate(int gridSize, int numAgents)
   {
       int gridUnitSize = 10;
       //Point[] p = {new Point(40, 40), new Point(40, 50), new Point(50, 40), new Point(50, 50)};
       List<Point> dangerArea = new ArrayList<>();
       for(int i = 50; i < 150; i += gridUnitSize)
       {
           for(int j = 50; j < 150; j += gridUnitSize)
           {
               dangerArea.add(new Point(i, j));
           }
       }
    
       JFrame frame = new JFrame();
       frame.setContentPane(new Environment(gridSize + 1, numAgents, dangerArea));
       frame.getContentPane().setBackground(new Color(69, 69, 69));
       frame.setSize((gridSize + 1) * 10 + 50, (gridSize + 1) * 10 + 50);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setVisible(true);
   }
}
