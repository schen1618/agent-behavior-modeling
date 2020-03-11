import java.awt.*;
import java.util.List;
import java.util.*;

public abstract class Environment
{
    public enum BoundaryType
    {
        REFLECT, TORUS
    }
    
    //total grid size in pixels
    static int gridSizePixel;
    
    //grid unit size in pixels
    static int gridUnitSize = 10;
    
    //grid left edge pixel location
    static int gridPixelStart = 15;
    
    //greatest coord (pixel) an agent can move to
    static int maxMoveGrid;
    
    //smallest coord (pixel) an agent can move to
    static int minMoveGrid = 20;
    
    //list that contains agents
    private List<Agent> agentList = new ArrayList<>();
    
    //list that contains locations
    HashMap<Point, Location> locationList = new HashMap<>();
    
    //list that contains danger locations
    static List<Point> dangerArea;
    
    static List<Point> dangerAreaLeft;
    
    static List<Point> dangerAreaRight;
    
    public Environment(List<Point> dangerArea, List<Point> dangerAreaLeft, List<Point> dangerAreaRight)
    {
        Environment.gridSizePixel = Main.gridSize * gridUnitSize;
        Environment.dangerArea = dangerArea;
        Environment.dangerAreaLeft = dangerAreaLeft;
        Environment.dangerAreaRight = dangerAreaRight;
        maxMoveGrid = gridSizePixel + 10;
        initLocations();
        initAgentArray(Main.numAgents);
    }
    
    public HashMap<Point, Location> getLocationList()
    {
        return locationList;
    }
    
    public List<Agent> getAgentList()
    {
        return agentList;
    }
    
    /**
     * Initializes agent array, creates agents, sets random agent location, and adds agent location to locationList
     *
     * @param n number of agents
     */
    public void initAgentArray(int n)
    {
        int len = locationList.keySet().toArray().length;
        Object[] locationArray = locationList.keySet().toArray();
        
        for(int i = 0; i < n; i++)
        {
            Agent agent = new Agent();
            agentList.add(agent);
            Point p = (Point) locationArray[new Random().nextInt(len)];
            agent.setLocation(p);
            agent.prevLocation = p;
            locationList.get(p).addAgentsInLocationList(agent);
        }
    }
    
    /**
     * Initializes location array
     */
    public void initLocations()
    {
        for(int x = gridPixelStart + 5; x <= gridSizePixel + gridUnitSize; x += gridUnitSize)
        {
            for(int y = gridPixelStart + 5; y <= gridSizePixel + gridUnitSize; y += gridUnitSize)
            {
                Point p = new Point(x, y);
                Location l = new Location(x, y);
                if(dangerArea.contains(p) || dangerAreaLeft.contains(p) || dangerAreaRight.contains(p))
                {
                    l.setLocationELevel(510); //max eLevel
                }
                locationList.put(p, l); //add location to list
            }
        }
    }
    
    /**
     * Moves agents and removes/adds to new location
     */
    public void moveAgents() throws Exception
    {
        for(Agent a : agentList)
        {
            Location prev = locationList.get(a.getLocation());
            a.move();
            a.prevLocation = prev;
            prev.removeAgentsInLocationList(a); //remove agent from prev location
            locationList.get(a.getLocation()).addAgentsInLocationList(a);   //add agent to new location
        }
    }
    
    /**
     * Calculates elevel of location from agents in location
     */
    public void calcLocationELevel()
    {
        for(Location l : locationList.values())
        {
            if(dangerArea.contains(l) || dangerAreaLeft.contains(l) || dangerAreaRight.contains(l)) //if agent is in dangerous area
            {
                l.setLocationELevel(510); //max eLevel
            }
            else
            {
                l.setLocationELevel(l.calcLocationELevel());
            }
        }
    }
    
    /**
     * Calculates agent elevel from surrounding location elevels
     */
    public void calcAgentELevel()
    {
        for(Agent a : agentList)
        {
            List<Point> adjList = findAdjLocation(a.x, a.y);
            List<Location> h = new ArrayList<>();
            for(Point p : adjList)  //find adj points, set agent adj list
            {
                Location l = locationList.get(p);
                h.add(l);
            }
            a.setAdjList(h);
            
            //find avg location elevel of adj locations and set to agent elevel (only surrounding, not including
            // current location)
            double d = adjList.stream().mapToDouble(e -> locationList.get(e).getLocationELevel()).average().orElse(0);
            a.setELevel(d);
        }
    }
    
    /**
     * Finds adjacent locations
     *
     * @param x x coord
     * @param y y coord
     * @return list of adjacent locations
     */
    public abstract List<Point> findAdjLocation(int x, int y);
}