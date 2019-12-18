import java.awt.*;

public class DensityDisplay extends Display
{
    
    public DensityDisplay(Environment e)
    {
        super(e);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        for(Location l : e.getLocationList().values())
        {
            double num = l.getAgentsInLocationList().size() * Main.displayContrast;
            
            if(num > 255)
            {
                num = 255;
            }
            g.setColor(new Color(255 - (int) num, 255 - (int) num, 255 - (int) num));
            
            g.fillRect((int) l.getX() - (Environment.gridUnitSize / 2), (int) l.getY() - (Environment.gridUnitSize / 2),
                    Environment.gridUnitSize, Environment.gridUnitSize);
        }
    }
}
