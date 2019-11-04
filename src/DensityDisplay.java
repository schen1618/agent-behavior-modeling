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
        
        for(Location l : e.getLocationList().values())
        {
            //double num = (l.getAgentsInLocationList().size() / (double) Main.numAgents) * 255.0;
            
            double num = l.getAgentsInLocationList().size() * 5;
            
            if(num > 255)
            {
                num = 255;
            }
            g.setColor(new Color(255 - (int) num, 255 - (int) num, 255 - (int) num));
            
            g.fillRect((int) l.getX() - (e.gridUnitSize / 2), (int) l.getY() - (e.gridUnitSize / 2),
                    (int) e.gridUnitSize,
                    (int) e.gridUnitSize);
        }
    }
}
