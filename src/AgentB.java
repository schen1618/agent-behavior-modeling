import java.awt.*;
import java.util.List;

public class AgentB extends Agent
{
    
    public AgentB(int gridSizePixel, int gridUnitSize, List<Point> dangerArea, String heuristic)
    {
        super(gridSizePixel, gridUnitSize, dangerArea, heuristic);
    }
    
    @Override
    public double calcMoveProb(Location e)
    {
        double p = 1 / 510.0;
        double q = 1.0 / Main.numAgents;
        return Math.
                exp(-1 * p * e.getLocationELevel() * (q * (e.getAgentsInLocationList().size() + 1)) * p * geteLevel());
    }
}
