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
    
    public void calcLocationELevel()
    {
        locationELevel = agentsInLocationList.stream().mapToDouble(Agent::geteLevel).average().orElse(0);
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
    
    /*
    public static void main(String[] args)
    {
        Agent p = new Agent(50, 50, new ArrayList<>());
        Agent q = new Agent(50, 50, new ArrayList<>());
        p.seteLevel(5);
        q.seteLevel(10);
        
        Location l = new Location(1,1);
        l.getAgentsInUnitList().put(p, p.geteLevel());
        l.getAgentsInUnitList().put(q, q.geteLevel());
    
        //System.out.println(l.calcUnitELevel());
    }
    */
}
