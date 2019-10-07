import java.awt.*;
import java.util.HashMap;

public class Location extends Point
{
    private double unitELevel;
    private HashMap<Agent, java.lang.Double> agentsInUnitList = new HashMap<>();
    
    public Location(int x, int y)
    {
        setLocation(x, y);
        unitELevel = 0;
    }
    
    public double getUnitELevel()
    {
        return unitELevel;
    }
    
    public void setUnitELevel(double unitELevel)
    {
        this.unitELevel = unitELevel;
    }
    
    public double calcAndGetUnitELevel()
    {
        unitELevel = agentsInUnitList.values().stream().mapToDouble(java.lang.Double::valueOf).average().orElse(0);
        return unitELevel;
    }
    
    public HashMap<Agent, java.lang.Double> getAgentsInUnitList()
    {
        return agentsInUnitList;
    }
    
    public void addToAgentsInUnitList(Agent a)
    {
        agentsInUnitList.put(a, a.geteLevel());
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
