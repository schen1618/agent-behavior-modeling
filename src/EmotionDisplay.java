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
        
        for(Point p : e.dangerArea)
        {
            g.fillRect((int) p.getX() - (e.gridUnitSize / 2), (int) p.getY() - (e.gridUnitSize / 2), e.gridUnitSize,
                    e.gridUnitSize);
        }
        
        g.setColor(Color.WHITE);
        for(int x = e.gridPixelStart; x <= e.gridSizePixel + e.gridUnitSize; x += e.gridUnitSize)
        {
            for(int y = e.gridPixelStart; y <= e.gridSizePixel + e.gridUnitSize; y += e.gridUnitSize)
            {
                g.drawRect(x, y, e.gridUnitSize, e.gridUnitSize);
            }
        }
        
        for(Agent agent : e.getAgentList())
        {
            if(agent.geteLevel() > 255)
            {
                g.setColor(new Color(255, 510 - (int) agent.geteLevel(), 0));
            }
            else
            {
                g.setColor(new Color((int) agent.geteLevel(), 255, 0));
            }
            
            g.fillOval((int) (agent.getX() - agent.radius), (int) (agent.getY() - agent.radius), (int) agent.diameter,
                    (int) agent.diameter);
        }
    }
}
