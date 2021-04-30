import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Brooks_Algo_Util {
    
    private weighted_graph g;

    /**
     * resets the graph tags and info
     * NOTE! default tag = -1 ; default info = ""
     */
    private void reset() {
        for (node_info n : this.g.getV()) {
            n.setTag(-1);
            n.setInfo(null);
        }
    }

    public Brooks_Algo_Util(weighted_graph g){
        this.g = g;
    }

    //  ===start of SCC'S functions===

    /**
     * simple dfs forset
     * each tree converted to subgraph
     * NOTE! for diGraph in the future change to:
     * 1. dfs
     * 2. graph transpose
     * 3. dfs by finish order
     * @return List<weighted_graph>
     */
    protected List<weighted_graph> SCC(){ //- Zigler
        List<weighted_graph> res = new LinkedList<>();
        List<Integer> comp;
        this.reset();

        //dfs
        for (node_info n : this.g.getV()) {
            
            if(n.getTag() != 1){ // new tree in dfs forest!
                comp = this.dfs_visit(n);
                res.add(createSubGraph(comp)); // convertion keys -> graph
            }
        }
        this.reset();
        return res;
    }

    private weighted_graph createSubGraph(List<Integer> comp) {
        weighted_graph graph_comp = new WGraph_DS();

        for (int key : comp) {
            graph_comp.addNode(key);
        }

        for (int s : comp) {
            for (node_info v : this.g.getV(s)) {
                int d = v.getKey();
                graph_comp.connect(s,d,this.g.getEdge(s, d).getValue());
            }
        }
        return graph_comp;
    }

    private List<Integer> dfs_visit(node_info n) {
            Stack<node_info> nodes = new Stack<>();
            LinkedList<Integer> comp = new LinkedList<>();
            nodes.push(n);
            while(!nodes.isEmpty()){
                node_info cur = nodes.pop();
                cur.setTag(1);
                comp.add(cur.getKey());
                for (node_info ni: g.getV(cur.getKey())) {
                    if(ni.getTag() != 1){
                        nodes.push(ni);
                    }
                }
            }
            return comp;
    }

    //  ===end of SCC'S functions===

    /**
     * color the nodes of the class's graph with a greedy algorithm
     * @param order
     */
    protected void GreedyColoring(List<Integer> order){
        int color;
        HashSet<Integer> setOfNeiColors = new HashSet<Integer>();
        for(int node_id : order){   // for each node in the list
           setOfNeiColors.clear();	// make a list of it's neighbors's colors
           for (node_info nei : g.getV(node_id)){						
               setOfNeiColors.add(nei.getColor());
           }
           color = 1;
           while(setOfNeiColors.contains(color)){color++;}				//find the minimum color not used by a neighbor
            g.getNode(node_id).setColor(color);     // color the node
        }
    } // - nir; the only func coloring the vx’es

    /***
     * A method that check deltaG of a given graph- which is the max degree from all the node in the graph.
     * In case we found that exist node that has a degree(node)=|V|-1 so just return that.
     * @param g-the graph we work on.
     * @return deltaG
     */
    protected int deltaG(weighted_graph g){ // - Kafka
        int maxDegree=0;
        for (node_info node: g.getV()) {
            int degree=g.getV(node.getKey()).size();
            if(maxDegree<degree){
                maxDegree=degree;
                if(maxDegree==g.getV().size()-1){
                    return maxDegree;
                }
            }
        }
        return maxDegree;
    } 
    protected boolean isOddCycle(weighted_graph g){ // - itai
        return false;
    }
    protected boolean isClique(weighted_graph g){ // - Evyater
        return false;
    } 
    protected  void handleDeltaTwo(weighted_graph g){} // - itai only order considered to Greedy

    protected int findRoot(weighted_graph g){ // - Zigler
        return 0;
    }
    protected List<Integer> spanningTreeOrder(weighted_graph g, int root){ // - Evyatar
        return new LinkedList<Integer>();
    }

    /***
     * A method that check if kappaG of a given graph is one.
     * By removing each node and checking if the graph still connected.
     * To avoid mistakes before we remove node we hold a collection of all his neighbor to enable reconnecting the graph.
     * @param g-the graph we work on.
     * @return - key node or -1 if there is no such a node.
     */
    protected int isOneConnected(weighted_graph g){// - Kafka returns -1 if false
        weighted_graph_algorithms ga=new WGraph_Algo();
        ga.init(g);
        Collection<node_info> prevConnections;
        weighted_graph gCopy=ga.copy();
        ga.init(gCopy);
        for (node_info node:g.getV()) {
            prevConnections=g.getV(node.getKey());
            gCopy.removeNode(node.getKey());
            if(!ga.isConnected()){
                return node.getKey();
            }
            reConnect(gCopy, node.getKey(), prevConnections);

        }

        return -1;
    }

    /***
     * Re connect the graph when we want to re add a node to the graph with his previous connections.
     * @param g -the graph we work on.
     * @param k -node to re add.
     * @param prevConnections -node's previous connections.
     */
    private void reConnect(weighted_graph g,int k,Collection<node_info> prevConnections){
        g.addNode(k);
        for (node_info node:prevConnections) {
            g.connect(k, node.getKey(), 1);
        }
    }
    protected void handleOneConnected(weighted_graph g, int sep){} // - Zigler 

    /**
     * ===from this point:===
     *      kappaG >=2
     *    G is d-regular
     *      d >= 3
     *    G not clicke
     */

    /**
     * find 3 node [x,y,z] where (x,y),(x,z) in E(G), (z,y) not in E(G), and g/{z,y} is connected
     * @param g - connected graph
     * @return [x_id, y_id, z_id]
     */
    protected int[] XYZ(weighted_graph g){ // - nir (first is x)

    	weighted_graph_algorithms ga = new WGraph_Algo();
    	
    	for(node_info x : g.getV()) {				// check every triple
    		for(node_info y : g.getV()) {
    			for(node_info z : g.getV()) {
    				// if is cherry
    				if(x!=y && x!=z && z!=y &&
    					g.hasEdge(x.getKey(), y.getKey()) && 
    					g.hasEdge(x.getKey(), z.getKey()) && 
    					!g.hasEdge(y.getKey(), z.getKey())) {
    					
    					ga.init(g);							
    					weighted_graph copy = ga.copy();			// 'remove' y and z
    					copy.removeNode(y.getKey());
    					copy.removeNode(z.getKey());
    					ga.init(copy);
    					if(ga.isConnected()) {						// if connected, return the current triple
    						int[] result = {x.getKey(), y.getKey(), z.getKey()};
    						return result;
    					}
    				}
    			
    			}
    		}
    	}
    	
    	return null;
    }
    protected void handleXYZcase(weighted_graph g){} // - Zigler

}
