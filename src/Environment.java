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
    List<Agent> agentList = new ArrayList<>();
    
    //list that contains locations
    HashMap<Point, Location> locationList = new HashMap<>();
    
    //list that contains danger locations
    List<Point> dangerArea;
    
    public Environment(int gridSize, int numAgents)
    {
        this(gridSize, numAgents, new ArrayList<>());
    }
    
    public Environment(int gridSize, int numAgents, List<Point> dangerArea)
    {
        this.gridSizePixel = gridSize * gridUnitSize;
        this.numAgents = numAgents;
        this.dangerArea = dangerArea;
        initLocations();
        initAgentArray(numAgents);
        
        
        Thread thread = new Thread(() -> {
            while(true)
            {
                calcLocationELevel();
                calcAgentELevel();
                moveAgents();
                repaint();
                try
                {
                    Thread.sleep(50);
                }
                catch(InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }
    
    /**
     * Initializes agent array, creates agents and adds agent location to locationList
     * @param n number of agents
     */
    public void initAgentArray(int n)
    {
        for(int i = 0; i < n; i++)
        {
            Agent agent = new Agent(gridSizePixel, gridUnitSize, dangerArea);
            agentList.add(agent);
            int x = (int) agent.getX();
            int y = (int) agent.getY();
            locationList.get(new Point(x, y)).addAgentsInLocationList(agent);
        }
    }
    
    /**
     * Initializes location array
     */
    public void initLocations()
    {
        for(int x = gridPixelStart + 5; x <= gridSizePixel; x += gridUnitSize)
        {
            for(int y = gridPixelStart + 5; y <= gridSizePixel; y += gridUnitSize)
            {
                Point p = new Point(x, y);
                Location l = new Location(x, y);
                if(dangerArea.contains(p))
                {
                    l.setLocationELevel(510); //max eLevel
                }
                locationList.put(p, l);
            }
        }
    }
    
    public void moveAgents()
    {
        for(Agent a : agentList)
        {
            Location prev = locationList.get(a.getLocation());
            a.move();
            prev.removeAgentsInLocationList(a);
            locationList.get(a.getLocation()).addAgentsInLocationList(a);
        }
    }
    
    public void calcLocationELevel()
    {
        for(Location l : locationList.values())
        {
            l.calcLocationELevel();
            
            if(dangerArea.contains(l))
            {
                l.setLocationELevel(510); //max eLevel
            }
        }
    }
    
    public void calcAgentELevel()
    {
        for(Agent a : agentList)
        {
            List<Point> adjList = findAdjLocation((int) a.getX(), (int) a.getY());
            List<Location> h = new ArrayList<>();
            for(Point p : adjList)
            {
                Location l = locationList.get(p);
                h.add(l);
            }
            a.setAdjList(h);
            double d = adjList.stream().mapToDouble(e -> locationList.get(e).getLocationELevel()).average().orElse(0);
            a.setELevel(d);
        }
    }
    
    /**
     * Finds adjacent locations
     * @param x x coord
     * @param y y coord
     * @return list of adjacent locations
     */
    public List<Point> findAdjLocation(int x, int y)
    {
        List<Point> a = new ArrayList<>();
        
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
    }
    
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
        for(int x = gridPixelStart; x <= gridSizePixel; x += gridUnitSize)
        {
            for(int y = gridPixelStart; y <= gridSizePixel; y += gridUnitSize)
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