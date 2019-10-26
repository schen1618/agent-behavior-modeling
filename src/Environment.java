import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class Environment extends JPanel
{
    //total grid size in pixels
    int gridSizePixel;
    
    //grid unit size in pixels
    int gridUnitSize = 10;
    
    //grid left edge pixel location
    int gridPixelStart = 15;
    
    //number of agents
    int numAgents;
    
    //list that contains agents
    java.util.List<Agent> agentList = new ArrayList<>();
    
    //list that contains locations
    HashMap<Point, Location> locationList = new HashMap<>();
    
    //list that contains danger locations
    List<Point> dangerArea;
    
    String heuristic;
    
    public Environment(int gridSize, int numAgents, String agentType, List<Point> dangerArea, String boundType)
    {
        this.gridSizePixel = gridSize * gridUnitSize;
        this.numAgents = numAgents;
        this.dangerArea = dangerArea;
        initLocations();
        initAgentArray(numAgents, agentType);
        
        Thread thread = new Thread(() -> {
            while(true)
            {
                try
                {
                    calcLocationELevel();   //avg elevel from agents in location
                    calcAgentELevel(boundType);  //avg elevel from adj locations and find adj locations to move
                    moveAgents();   //move agents and add/remove from location in list
                    repaint();
                    Thread.sleep(50);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }
    
    /**
     * Initializes agent array, creates agents, sets random agent location, and adds agent location to locationList
     *
     * @param n number of agents
     */
    public void initAgentArray(int n, String agentType)
    {
        int len = locationList.keySet().toArray().length;
        Object[] locationArray = locationList.keySet().toArray();
        
        if(agentType.equals("AgentA"))
        {
            for(int i = 0; i < n; i++)
            {
                AgentA agent = new AgentA(gridSizePixel, gridUnitSize, dangerArea, heuristic);
                agentList.add(agent);
                Point p = (Point) locationArray[new Random().nextInt(len)];
                agent.setLocation(p);
                locationList.get(p).addAgentsInLocationList(agent);
            }
        }
        
        else if(agentType.equals("AgentB"))
        {
            for(int i = 0; i < n; i++)
            {
                AgentB agent = new AgentB(gridSizePixel, gridUnitSize, dangerArea, heuristic);
                agentList.add(agent);
                Point p = (Point) locationArray[new Random().nextInt(len)];
                agent.setLocation(p);
                locationList.get(p).addAgentsInLocationList(agent);
            }
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
    public void calcAgentELevel(String boundType)
    {
        for(Agent a : agentList)
        {
            List<Point> adjList = findAdjLocation((int) a.getX(), (int) a.getY(), boundType);
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
    public List<Point> findAdjLocation(int x, int y, String boundType)
    {
        List<Point> a = new ArrayList<>();
        
        if(boundType.equals("bound"))
        {
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
        }
        
        if(boundType.equals("torus"))
        {
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
            
            a.add(locationList.get(new Point(newxUp, newyLeft)));
            a.add(locationList.get(new Point(newxUp, y)));
            a.add(locationList.get(new Point(newxUp, newyRight)));
            a.add(locationList.get(new Point(x, newyLeft)));
            a.add(locationList.get(new Point(x, newyRight)));
            a.add(locationList.get(new Point(newxDown, newyLeft)));
            a.add(locationList.get(new Point(newxDown, y)));
            a.add(locationList.get(new Point(newxDown, newyRight)));
        }
        
        return a;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(new Color(227, 227, 227));
        
        for(Point p : dangerArea)
        {
            g.fillRect((int) p.getX() - (gridUnitSize / 2), (int) p.getY() - (gridUnitSize / 2), gridUnitSize,
                    gridUnitSize);
        }
        
        g.setColor(Color.WHITE);
        for(int x = gridPixelStart; x <= gridSizePixel + gridUnitSize; x += gridUnitSize)
        {
            for(int y = gridPixelStart; y <= gridSizePixel + gridUnitSize; y += gridUnitSize)
            {
                g.drawRect(x, y, gridUnitSize, gridUnitSize);
            }
        }
        
        for(Agent agent : agentList)
        {
            if(agent.geteLevel() > 255)
            {
                g.setColor(new Color(255, 510 - (int) agent.geteLevel(), 0));
            }
            else
            {
                g.setColor(new Color((int) agent.geteLevel(), 255, 0));
            }
            
            g.fillOval((int) (agent.getX() - agent.radius), (int) (agent.getY() - agent.radius), (int) agent.diameter,
                    (int) agent.diameter);
        }
    }
}