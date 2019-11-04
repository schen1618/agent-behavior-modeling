import java.awt.*;
import java.util.List;
import java.util.*;

public abstract class Agent extends Point
{
    //agent size
    float radius = 5;
    float diameter = radius * 2;
    
    //step size for agent (pixel)
    private int step = 10;
    
    //total grid size (pixel)
    private int gridSizePixel;
    
    //unit size (pixel)
    private int gridUnitSize;
    
    //greatest coord (pixel) an agent can move to
    private int maxMoveGrid;
    
    //emotion level (0-510)
    private double eLevel;
    
    private int maxELevel = 510;
    
    private List<Point> dangerArea;
    
    List<Location> adjList = new ArrayList<>();
    
    public Agent(int gridSizePixel, int gridUnitSize, List<Point> dangerArea)
    {
        this.gridSizePixel = gridSizePixel;
        this.gridUnitSize = gridUnitSize;
        maxMoveGrid = gridSizePixel + 10;
        //setLocation(randCoord(), randCoord());
        eLevel = 0;
        this.dangerArea = dangerArea;
    }
    
    /**
     * get elevel
     *
     * @return elevel
     */
    public double geteLevel()
    {
        return eLevel;
    }
    
    /**
     * set elevel
     *
     * @param avg average elevel of adjacent locations
     */
    public void setELevel(double avg)
    {
        if(dangerArea.contains(this))
        {
            eLevel = maxELevel;
        }
        else
        {
            eLevel = avg;
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
     * Generates random coord for agent
     *
     * @return random coord within bounds
     */
    public int randCoord()
    {
        Random random = new Random();
        return (random.nextInt((maxMoveGrid / 10) - 2) + 2) * gridUnitSize;
    }
    
    /**
     * Moves agent, sets location
     */
    public void move() throws Exception
    {
        setLocation(findNextMove());
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
        
        double sum = getAdjList().stream().mapToDouble(this::calcMoveProb).sum(); //sum of h1 of adjList
        for(Location a : getAdjList())
        {
            double x = calcMoveProb(a) / sum;
            min = min + x;
            //min = min + (calcMoveProb(a) / sum);  //add to previous probability (0 <= min <= 1)
            
            if(d < min)
            {
                return a.getLocation();
            }
        }
        
        throw new Exception("findNextMove method error, probably divide by 0");
    }
    
    public abstract double calcMoveProb(Location e);
    
}

