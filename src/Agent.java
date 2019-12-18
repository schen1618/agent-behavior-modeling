import java.awt.*;
import java.util.List;
import java.util.*;

public class Agent extends Point
{
    //agent size
    float radius = 5;
    float diameter = radius * 2;
    
    //emotion level (0-510)
    private double currentELevel;
    private double prevELevel;
    
    private List<Location> adjList = new ArrayList<>();
    
    Point prevLocation;
    
    public Agent()
    {
        currentELevel = 0;
        prevELevel = 0;
    }
    
    /**
     * get elevel
     *
     * @return elevel
     */
    public double getCurrentELevel()
    {
        return currentELevel;
    }
    
    /**
     * set elevel
     *
     * @param avg average elevel of adjacent locations
     */
    public void setELevel(double avg)
    {
        prevELevel = getCurrentELevel();
        
        if(Environment.dangerArea.contains(this))
        {
            currentELevel = 510; //max e level (for color purposes)
        }
        else
        {
            currentELevel = Main.decayRate * avg;
        }
    }
    
    /**
     * get adjacent location list
     *
     * @return adj location list
     */
    public List<Location> getAdjList()
    {
        return adjList;
    }
    
    /**
     * set adj list
     *
     * @param adjList adjlist
     */
    public void setAdjList(List<Location> adjList)
    {
        this.adjList = adjList;
    }
    
    /**
     * Moves agent, sets location
     */
    public void move() throws Exception
    {
        if(currentELevel - prevELevel > Main.eDiffThreshold)
        {
            setLocation(adoptAdjMove());
        }
        else
        {
            setLocation(findNextMove());
        }
    }
    
    /**
     * Finds next location
     *
     * @return next location
     * @throws Exception Divide by 0, most times
     */
    public Point findNextMove() throws Exception
    {
        double d = new Random().nextDouble();   //unif dist random
        double min = 0;
        
        double sum = getAdjList().stream().mapToDouble((loc) -> Main.movement.probability(loc, this)).sum(); //sum of h1
        // of adjList
        
        if(sum == 0)
        {
            Collections.shuffle(getAdjList());
            return getAdjList().get(new Random().nextInt(getAdjList().size()));
        }
        
        for(Location a : getAdjList())
        {
            double x = Main.movement.probability(a, this) / sum;
            min = min + x;
            //min = min + (calcMoveProb(a) / sum);  //add to previous probability (0 <= min <= 1)
            
            if(d < min)
            {
                return a.getLocation();
            }
        }
        
        throw new Exception("findNextMove method error, probably divide by 0");
    }
    
    public Point adoptAdjMove() throws Exception
    {
        List<Agent> agentList =
                getAdjList().stream().max(Comparator.comparing(Location::getLocationELevel)).get().getAgentsInLocationList(); //max elevel of adjList
        List<Point> nextLocFromAdjAgents = new ArrayList<>();
        
        //get prev direction of adj agent and apply to current agent
        for(Agent a : agentList)
        {
            Point nextLocation = findLocationFromTransform(this.getLocation(), findMoveTransform(a.prevLocation,
                    a.getLocation()));
            nextLocFromAdjAgents.add(nextLocation);
        }
    
        if(nextLocFromAdjAgents.size() == 0)
        {
            return findNextMove(); //if adj locations contain no agents
        }
        
        Collections.shuffle(nextLocFromAdjAgents);
        int d = new Random().nextInt(nextLocFromAdjAgents.size()); //unif dist random
        return agentList.get(d);
    }
    
    public Point findMoveTransform(Point before, Point after)
    {
        int x;
        int y;
        
        if(after.x - before.x == Environment.maxMoveGrid - Environment.minMoveGrid)
        {
            x = -1;
        }
        else if(after.x - before.x == Environment.minMoveGrid - Environment.maxMoveGrid)
        {
            x = 1;
        }
        else
        {
            x = after.x - before.x;
        }
        
        if(after.y - before.y == Environment.maxMoveGrid - Environment.minMoveGrid)
        {
            y = -1;
        }
        else if(after.y - before.y == Environment.minMoveGrid - Environment.maxMoveGrid)
        {
            y = 1;
        }
        else
        {
            y = after.y - before.y;
        }
        
        return new Point(x, y);
    }
    
    public Point findLocationFromTransform(Point p, Point transform) throws Exception
    {
        int x;
        int y;
        
        x = p.x + transform.x;
        y = p.y + transform.y;
        
        switch(Main.boundaryType)
        {
            case TORUS:
                if(x > Environment.maxMoveGrid)
                {
                    x = Environment.minMoveGrid;
                }
                else if(x < Environment.minMoveGrid)
                {
                    x = Environment.maxMoveGrid;
                }
                
                if(y > Environment.maxMoveGrid)
                {
                    y = Environment.minMoveGrid;
                }
                else if(y < Environment.minMoveGrid)
                {
                    y = Environment.maxMoveGrid;
                }
                
                return new Point(x, y);
            
            case BOUND:
                if(x > Environment.maxMoveGrid)
                {
                    x = Environment.maxMoveGrid;
                }
                else if(x < Environment.minMoveGrid)
                {
                    x = Environment.minMoveGrid;
                }
                
                if(y > Environment.maxMoveGrid)
                {
                    y = Environment.maxMoveGrid;
                }
                else if(y < Environment.minMoveGrid)
                {
                    y = Environment.minMoveGrid;
                }
                
                return new Point(x, y);
        }
        
        throw new Exception("Error in findLocationFromTransform");
    }
}

