import java.awt.*;
import java.util.List;
import java.util.*;

public class EnvEight extends Environment
{
    public EnvEight(List<Point> dangerArea, List<Point> dangerAreaLeft, List<Point> dangerAreaRight)
    {
        super(dangerArea, dangerAreaLeft, dangerAreaRight);
    }
    
    public List<Point> findAdjLocation(int x, int y)
    {
        List<Point> a = new ArrayList<>();
        
        switch(Main.boundaryType)
        {
            case REFLECT:
                a.add(locationList.get(new Point(x, y)));
                
                if(locationList.get(new Point(x - gridUnitSize, y - gridUnitSize)) != null)
                {
                    a.add(locationList.get(new Point(x - gridUnitSize, y - gridUnitSize)));
                }
                if(locationList.get(new Point(x - gridUnitSize, y)) != null)
                {
                    a.add(locationList.get(new Point(x - gridUnitSize, y)));
                }
                if(locationList.get(new Point(x - gridUnitSize, y + gridUnitSize)) != null)
                {
                    a.add(locationList.get(new Point(x - gridUnitSize, y + gridUnitSize)));
                }
                if(locationList.get(new Point(x, y - gridUnitSize)) != null)
                {
                    a.add(locationList.get(new Point(x, y - gridUnitSize)));
                }
                if(locationList.get(new Point(x, y + gridUnitSize)) != null)
                {
                    a.add(locationList.get(new Point(x, y + gridUnitSize)));
                }
                if(locationList.get(new Point(x + gridUnitSize, y - gridUnitSize)) != null)
                {
                    a.add(locationList.get(new Point(x + gridUnitSize, y - gridUnitSize)));
                }
                if(locationList.get(new Point(x + gridUnitSize, y)) != null)
                {
                    a.add(locationList.get(new Point(x + gridUnitSize, y)));
                }
                if(locationList.get(new Point(x + gridUnitSize, y + gridUnitSize)) != null)
                {
                    a.add(locationList.get(new Point(x + gridUnitSize, y + gridUnitSize)));
                }
                return a;
            
            case TORUS:
                int newxUp = x - gridUnitSize;
                int newxDown = x + gridUnitSize;
                int newyLeft = y - gridUnitSize;
                int newyRight = y + gridUnitSize;
                if(newxUp < 20)
                {
                    newxUp = gridSizePixel + gridUnitSize;
                }
                if(newxDown > gridSizePixel + gridUnitSize)
                {
                    newxDown = 20;
                }
                if(newyLeft < 20)
                {
                    newyLeft = gridSizePixel + gridUnitSize;
                }
                if(newyRight > gridSizePixel + gridUnitSize)
                {
                    newyRight = 20;
                }
                
                a.add(locationList.get(new Point(x, y)));
                a.add(locationList.get(new Point(newxUp, newyLeft)));
                a.add(locationList.get(new Point(newxUp, y)));
                a.add(locationList.get(new Point(newxUp, newyRight)));
                a.add(locationList.get(new Point(x, newyLeft)));
                a.add(locationList.get(new Point(x, newyRight)));
                a.add(locationList.get(new Point(newxDown, newyLeft)));
                a.add(locationList.get(new Point(newxDown, y)));
                a.add(locationList.get(new Point(newxDown, newyRight)));
                return a;
            default:
                throw new IllegalArgumentException("Boundary type is not handled");
        }
    }
}
