import java.awt.*;
import java.util.List;

public class AgentA extends Agent
{
    public AgentA(int gridSizePixel, int gridUnitSize, List<Point> dangerArea)
    {
        super(gridSizePixel, gridUnitSize, dangerArea);
    }
    
    @Override
    public double calcMoveProb(Location e)
    {
        double p = 1 / 512.0;
        double q = 1.0; // Main.numAgents;
        return Math.exp(-1 * p * e.getLocationELevel() * (q * (e.getAgentsInLocationList().size() + 1)));
        //return Math.exp(-1 * p * e.getLocationELevel()) + Math.exp(-1 * q * e.getAgentsInLocationList().size());
    }
}
