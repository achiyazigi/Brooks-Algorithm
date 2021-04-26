# Brooks-Algorithm
Brooks-Algorithm


Brook-Coloring - no one

Bool Connected : split to CC’s.  

#protected List<weighted_graph> SCC();- Zigler

Greedy algorithm - oddCycle clicke

#protected void GreedyColoring(List<int>); - nir; the only func coloring the vx’es
  
deltaG

#protected int deltaG(weighted_graph g); - Kafka

Bool oddCycle

#protected boolean isOddCycle(weighted_graph g); - itai

Bool clicke

#protected boolean isClique(weighted_graph g); - Evyater

Color with 2 colors the even Cycle and path

#protected  void handleDeltaTwo(weighted_graph g) - itai; only order considered to Greedy

Find v < deltaG return v or null

#protected int findRoot(weighted_graph g); Zigler;

Spanning tree : Graph: return list of vx

#protected List<int> spanningTreeOrder(weighted_graph g, int root); - Evyatar;
  
kappaG

#protected int isOneConnected(weighted_graph g); Kafka returns -1 if false

Handle kappaG=1

#protected void handleOneConnected(weighted_graph g, int sep) - Zigler 

—————————

>> kappaG >=2

>> G is d-regular

>> d >= 3

>> G not clicke

Find XYZ - for now keep it simple

#protected int[] XYZ(weighted_graph g); - nir (first is x)

Generate spannigTree for g-{y,z} rooted with x returns {y,z…x} 

#protected void handleXYZcase(weighted_graph g); - Zigler
