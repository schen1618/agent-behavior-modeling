import java.awt.*;

public class EmotionDisplay extends Display
{
    public EmotionDisplay(Environment e)
    {
        super(e);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(new Color(227, 227, 227));
        
        for(Point p : Environment.dangerArea)
        {
            g.fillRect((int) p.getX() - (Environment.gridUnitSize / 2), (int) p.getY() - (
                            Environment.gridUnitSize / 2), Environment.gridUnitSize,
                    Environment.gridUnitSize);
        }
        
        for(Point p : Environment.dangerAreaLeft)
        {
            g.fillRect((int) p.getX() - (Environment.gridUnitSize / 2), (int) p.getY() - (
                            Environment.gridUnitSize / 2), Environment.gridUnitSize,
                    Environment.gridUnitSize);
        }
        
        for(Point p : Environment.dangerAreaRight)
        {
            g.fillRect((int) p.getX() - (Environment.gridUnitSize / 2), (int) p.getY() - (
                            Environment.gridUnitSize / 2), Environment.gridUnitSize,
                    Environment.gridUnitSize);
        }
        
        g.setColor(Color.WHITE);
        for(int x = Environment.gridPixelStart; x <= Environment.gridSizePixel + Environment.gridUnitSize; x += Environment.gridUnitSize)
        {
            for(int y = Environment.gridPixelStart; y <= Environment.gridSizePixel + Environment.gridUnitSize; y += Environment.gridUnitSize)
            
            {
                g.drawRect(x, y, Environment.gridUnitSize, Environment.gridUnitSize);
            }
        }
        
        for(Agent agent : e.getAgentList())
        {
            if(agent.getCurrentELevel() > 255)
            {
                g.setColor(new Color(255, 510 - (int) agent.getCurrentELevel(), 0));
            }
            else
            {
                g.setColor(new Color((int) agent.getCurrentELevel(), 255, 0));
            }
            
            g.fillOval((int) (agent.getX() - agent.radius), (int) (agent.getY() - agent.radius), (int) agent.diameter,
                    (int) agent.diameter);
        }
    }
}
