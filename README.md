#On-Lattice Crowd Modeling

Sherry Chen

Dr. Alethea Barbaro, MATH301 Fall 2019 - Present

Case Western Reserve University

##About

A program that models agent movement and dynamics based on emotional level. Created using Java and Java Swing.

Agent movement is restricted to an $n \times n$ grid. No restriction on the number of agents at a grid point (2+ agents may be at the same grid point).

## How To Run

As of 12/18/2019:

Modify parameters in Main file to desired specifications

- numAgents: 0 - 50,000+, depending on machine capabilities
- gridSize: 1 - 50+ (50 fits perfectly on my screen)
- boundaryType: BOUND or TORUS
- eDiffThreshold: 0 - 20+
- movement: A or B, can create more movements
- numAdj: 4 or 8
- dangerAreaStart: 0 - gridSize * 10
- dangerAreaEnd:  0 - gridSize * 10 
- displayContrast: recommend 2 - 4
- decayRate:  0 - 1, recommend 1 or 0.99

Recommended values also commented in the file.

Run Main in IDE or type into command prompt:

```bash
$ javac Main.java
$ java Main
```



##Definitions

- Emotional level (eLevel): parameter that influences agent movement

- Location: Point on the grid that contains agents

- Location Emotional Level: Average eLevel of agents in that location, unless in danger area

- Boundary types: Bound (cannot go past edges), Torus (boundless, edges wrap around)

- Movement type: determines the probability of an agent moving to a location

- Danger area: agent emotional level is automatically set to max. Agents try to avoid these locations

- eDiffThreshold: threshold for deciding whether to move based on their eLevel or adopt another agent's direction.

- Grid: Evenly spaced Points (Java class) arranged in $n \times n$ grid



##Structure (Class Descriptions)

- Agent: point that moves on a grid

- Display (abstract)
  - EmotionDisplay: displays emotion color of agent on a spectrum (green = low eLevel, red = high eLevel)
  - DensityDisplay: displays density of agents (white = low number of agents, dark = high number of agents)

- Environment (abstract): environment where agents move (on a grid)
  - EnvFour: Each agent has 4 neighbors
  - EnvEight: Each agent has 8 neighbors

- Location: Point on the grid that contains agents

- Main: Driver of program. Contains thread that executes agent actions. Contains parameters to specify gridSize, number of agents, etc.

- Movement (interface): groups movement types together



##Program Process Description

1. Create danger area
2. Initialize Environment
   1. Initialize Agents and Locations
3. Initialize Display(s)
4. Process Agents
   1. Calculate each Location eLevel (average of agents in location)
   2. Calculate each Agent eLevel (average eLevel of adjacent locations)
   3. Move agents (described below in Agent Movement)

5. Update Display(s)
6. Repeat 4 and 5.



##Agent Movement

Two types of agent movement:

- Normal
  
  - Calculates probability of moving to an adjacent location based on the following equations:
    
    - Movement A: $prob = e^{-PQL_{elevel}(L_{size}+ 1)}$
    
    - Movement B: $prob = e^{-P^2QL_{elevel}(L_{size} + 1)A_{current}}$
    
      where $P$ and $Q$ are changeable parameters, $L_{elevel}$ is the location eLevel, $L_{size}$ is the number of agents in a location, $A_{current}$ is the current eLevel of the agent
  
  - Chooses from probabilities using a uniform distribution
  
- Adoption

  - If the change in eLevel is great enough (set by eDiffThreshold), then this agent will adopt the previous move of an agent (chosen using a uniform distribution) in an adjacent location with highest eLevel



## Examples of Behavior

![image-20191219060306690](C:\Users\schen\AppData\Roaming\Typora\typora-user-images\image-20191219060306690.png)

- Grey area is danger area 
- Only a few agents in the grid to show colors
- Movement is easier to see with fewer agents



![image-20191219060609599](C:\Users\schen\AppData\Roaming\Typora\typora-user-images\image-20191219060609599.png)

- Color and density display side by side

- Ring of agents moving away from danger area

  

  

  ####For eDiffThreshold = 5, numAgents = 50000, Torus boundary

![image-20191219060749053](C:\Users\schen\AppData\Roaming\Typora\typora-user-images\image-20191219060749053.png)

- Torus behavior shown



![image-20191219060951798](C:\Users\schen\AppData\Roaming\Typora\typora-user-images\image-20191219060951798.png)

- Agent crowding in one area (furthest distance from danger area)

- Unusual pattern emerging near danger area after a few seconds

  

![image-20191219061439536](C:\Users\schen\AppData\Roaming\Typora\typora-user-images\image-20191219061439536.png)

- All agents drawn to danger area (agent behavior needs to be improved)



![image-20191219061634428](C:\Users\schen\AppData\Roaming\Typora\typora-user-images\image-20191219061634428.png)

- Dissipation of crowd and emergence of a "line"

  

![image-20191219061725927](C:\Users\schen\AppData\Roaming\Typora\typora-user-images\image-20191219061725927.png)

- End behavior, line remains



In gif format:

![ediff](C:\Users\schen\Desktop\ediff.gif)



## Improvements To Be Made

12/18/19

Idea: If agent adopts move of another agent, instead of adopting their previous move, they try to predict the next move of the agent and adopt the prediction. 

We can see in the example above that all the agents flock to the danger area, which is a result of agents adopting the previous move (moving into the danger area). Although this makes interesting patterns and behaviors, this is contradictory to what a person would do in a dangerous area (from our current beliefs). This new idea may resolve the issue.