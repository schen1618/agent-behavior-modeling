import java.awt.*;

public class EnvironmentDensity extends Environment
{
    
    public EnvironmentDensity(int gridSize, int numAgents, String agentType, java.util.List<Point> dangerArea,
                              String boundType)
    {
        super(gridSize, numAgents, agentType, dangerArea, boundType);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        /*
        g.setColor(new Color(227, 227, 227));
        for(Point p : dangerArea)
        {
            g.fillRect((int) p.getX() - (gridUnitSize / 2), (int) p.getY() - (gridUnitSize / 2), gridUnitSize,
                    gridUnitSize);
        }
        
        
        g.setColor(Color.GRAY);
        for(int x = gridPixelStart; x <= gridSizePixel; x += gridUnitSize)
        {
            for(int y = gridPixelStart; y <= gridSizePixel; y += gridUnitSize)
            {
                g.drawRect(x, y, gridUnitSize, gridUnitSize);
            }
        }*/
        
        for(Location l : locationList.values())
        {
            //double num = (l.getAgentsInLocationList().size() / (double) Main.numAgents) * 255.0;
            
            double num = l.getAgentsInLocationList().size() * 2;
            
            if(num > 255)
            {
                num = 255;
            }
            g.setColor(new Color(255 - (int) num, 255 - (int) num, 255 - (int) num));
            
            //g.setColor(new Color((int) num, (int) num, (int) num));
            
            g.fillRect((int) l.getX() - (gridUnitSize / 2), (int) l.getY() - (gridUnitSize / 2), (int) gridUnitSize,
                    (int) gridUnitSize);
        }
    }
}
