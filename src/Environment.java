import java.awt.*;
import java.util.List;
import java.util.*;

public abstract class Environment
{
    public enum BoundaryType
    {
        BOUND, TORUS
    }
    
    //total grid size in pixels
    int gridSizePixel;
    
    //grid unit size in pixels
    int gridUnitSize = 10;
    
    //grid left edge pixel location
    int gridPixelStart = 15;
    
    //number of agents
    int numAgents;
    
    //list that contains agents
    List<Agent> agentList = new ArrayList<>();
    
    //list that contains locations
    HashMap<Point, Location> locationList = new HashMap<>();
    
    //list that contains danger locations
    List<Point> dangerArea;
    
    BoundaryType boundaryType;
    
    public Environment(int gridSize, int numAgents, Movement movement, List<Point> dangerArea,
                       BoundaryType boundaryType)
    {
        this.gridSizePixel = gridSize * gridUnitSize;
        this.numAgents = numAgents;
        this.dangerArea = dangerArea;
        this.boundaryType = boundaryType;
        initLocations();
        initAgentArray(numAgents, movement);
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
    public void initAgentArray(int n, Movement movement)
    {
        int len = locationList.keySet().toArray().length;
        Object[] locationArray = locationList.keySet().toArray();
        
        for(int i = 0; i < n; i++)
        {
            Agent agent = new Agent(gridSizePixel, gridUnitSize, dangerArea, movement);
            agentList.add(agent);
            Point p = (Point) locationArray[new Random().nextInt(len)];
            agent.setLocation(p);
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
                if(dangerArea.contains(p))
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
            l.calcLocationELevel();
            
            if(dangerArea.contains(l)) //if agent is in dangerous area
            {
                l.setLocationELevel(510); //max eLevel
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