import java.awt.*;
import java.util.List;
import java.util.*;

public class Location extends Point
{
    private double locationELevel;
    private List<Agent> agentsInLocationList = new ArrayList<>();
    
    public Location(int x, int y)
    {
        setLocation(x, y);
        locationELevel = 0;
    }
    
    public double getLocationELevel()
    {
        return locationELevel;
    }
    
    public void setLocationELevel(double unitELevel)
    {
        this.locationELevel = unitELevel;
    }
    
    /**
     * Averages elevel of agents in location
     */
    public double calcLocationELevel()
    {
        return agentsInLocationList.stream().mapToDouble(Agent::getCurrentELevel).average().orElse(0);
    }
    
    public List<Agent> getAgentsInLocationList()
    {
        return agentsInLocationList;
    }
    
    public void addAgentsInLocationList(Agent a)
    {
        agentsInLocationList.add(a);
    }
    
    public void removeAgentsInLocationList(Agent a)
    {
        agentsInLocationList.remove(a);
    }
    
}
