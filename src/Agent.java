import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Agent extends Point
{
    //agent size
    float radius = 5;
    float diameter = radius * 2;
    
    //step size for agent (pixel)
    private int step = 10;
    
    //possible moves
    private int[] moves = {0, step, -1 * step};
    
    //possible corner moves for left + top
    private int[] leftTopBorderMoves = {0, step};
    
    //possible corner moves for right + bottom
    private int[] rightBottomBorderMoves = {0, -1 * step};
    
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
        maxMoveGrid = gridSizePixel;
        setLocation(randCoord(), randCoord());
        eLevel = 0;
        this.dangerArea = dangerArea;
    }
    
    public double geteLevel()
    {
        return eLevel;
    }
    
    public void seteLevel(double eLevel)
    {
        this.eLevel = eLevel;
    }
    
    public void calcELevel(double avg)
    {
        if(dangerArea.contains(this))
        {
            seteLevel(maxELevel);
        }
        else
        {
            seteLevel(avg);
        }
    }
    
    public List<Location> getAdjList()
    {
        return adjList;
    }
    
    public void setAdjList(List<Location> adjList)
    {
        this.adjList = adjList;
    }
    
    //generates random even int
    public static int randEvenInt(int min, int max)
    {
        if(min % 2 != 0)
        {
            ++min;
        }
        return min + 2 * ThreadLocalRandom.current().nextInt((max - min) / 2 + 1);
    }
    
    /**
     * Generates random coord for agent
     *
     * @return random coord within bounds
     */
    public int randCoord()
    {
        Random random = new Random();
        return (random.nextInt((gridSizePixel / 10) - 2) + 2) * gridUnitSize;
    }
    
    /**
     * Moves agent
     */
    public void move()
    {
        setLocation(getNextMove());
    }
    
    public Point getNextMove()
    {
        PriorityQueue<Location> pq = new PriorityQueue<>(new Comparator<Location>()
        {
            @Override
            public int compare(Location o1, Location o2)
            {
                return java.lang.Double.compare(h2(o1), h2(o2));
            }
        });
        pq.addAll(adjList);
        return adjList.get(adjList.indexOf(pq.poll())).getLocation();
    }
    
    public double h1(Location e)
    {
        return 0.125 * Math.exp(-1 * e.getUnitELevel() * e.getAgentsInUnitList().size());
    }
    
    public double h2(Location e)
    {
        return 0.125 * Math.exp(-1 * geteLevel() * e.getAgentsInUnitList().size());
    }
    
    public void moveRandom()
    {
        int dx;
        int dy;
        Random rand = new Random();
        
        if(getX() == 20) //left edge
        {
            int r = rand.nextInt(leftTopBorderMoves.length);
            dx = leftTopBorderMoves[r];
        }
        else if(getX() == gridSizePixel) //right edge
        {
            int r = rand.nextInt(rightBottomBorderMoves.length);
            dx = rightBottomBorderMoves[r];
        }
        else
        {
            int r = rand.nextInt(moves.length);
            dx = moves[r];
        }
        
        if(getY() == 20) //top edge
        {
            int r = rand.nextInt(leftTopBorderMoves.length);
            dy = leftTopBorderMoves[r];
        }
        else if(getY() == gridSizePixel) //bottom edge
        {
            int r = rand.nextInt(rightBottomBorderMoves.length);
            dy = rightBottomBorderMoves[r];
        }
        else
        {
            int r = rand.nextInt(moves.length);
            dy = moves[r];
        }
        
        setLocation(getX() + dx, getY() + dy);
    }
    
    /*public List<Point> findAdjLocationELevel(int x, int y)
    {
        List<Point> adjList = null;
        //center, left edge, right edge, corners
        if(x == 20)
        {
            adjList = new ArrayList<>();
            
            adjList.add(new Point(x, y + gridUnitSize)); //left top corner
            adjList.add(new Point(x + gridUnitSize, y));
            adjList.add(new Point(x + gridUnitSize, y + gridUnitSize));
            
            if(!(y == 20 || y == maxMoveGrid)) //top edge
            {
                adjList.add(new Point(x, y - gridUnitSize));
                adjList.add(new Point(x + gridUnitSize, y - gridUnitSize));
            }
            
            return adjList;
        }
        
        if(x == maxMoveGrid)
        {
            adjList = new ArrayList<>();
            
            adjList.add(new Point(x - gridUnitSize, y)); //left bottom corner
            adjList.add(new Point(x, y + gridUnitSize));
            adjList.add(new Point(x - gridUnitSize, y + gridUnitSize));
            
            if(!(y == 20 || y == maxMoveGrid)) //bottom edge
            {
                adjList.add(new Point(x, y - gridUnitSize));
                adjList.add(new Point(x - gridUnitSize, y - gridUnitSize));
            }
            
            return adjList;
        }
    
        if(y == 20)
        {
            adjList = new ArrayList<>();
        
            adjList.add(new Point(x - gridUnitSize, y)); //left bottom corner
            adjList.add(new Point(x, y + gridUnitSize));
            adjList.add(new Point(x - gridUnitSize, y + gridUnitSize));
        
            if(!(x == maxMoveGrid)) //left edge
            {
                adjList.add(new Point(x, y - gridUnitSize));
                adjList.add(new Point(x - gridUnitSize, y - gridUnitSize));
            }
        
            return adjList;
        }
        
        return adjList;
    }
    */
}

