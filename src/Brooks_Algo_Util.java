import org.w3c.dom.Node;

import java.lang.reflect.Array;
import java.util.*;

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


    protected void GreedyColoring(List<Integer> order){} // - nir; the only func coloring the vx’es

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

    /**
     * Checks whether a given graph is an odd cycle
     * @param g - A connected weighted graph
     * @return  - True if the graph is an odd cycle, false otherwise
     */
    protected boolean isOddCycle(weighted_graph g) { // - itai
        if (g.nodeSize() % 2 == 0 || g.edgeSize() != g.nodeSize()) {
            return false;
        }
        for (node_info node : g.getV()) {
            int degree = g.getV(node.getKey()).size();
            if (degree != 2) {
                return false;
            }
        }
        return true;
    }

    protected boolean isClique(weighted_graph g){ // - Evyater
        return false;
    }

    /**
     * Defines the arrangement of the vertices in a graph (even cycle or path)
     * and sends them to the GreedyColoring methods.
     * In a path graph and an even cycle graph the coloring is simple (2-coloring)
     * so all it has to do is to arrange the vertices and send them for coloring.
     * @param g - A graph that is a path or an even cycle
     */
    protected void handleDeltaTwo(weighted_graph g){ // - itai only order considered to Greedy
        List<Integer> nodesOrder;
        if(isEvenCycle(g)){
            nodesOrder = cycleOrder(g);
        }
        else{
            nodesOrder = pathOrder(g);
        }
        GreedyColoring(nodesOrder);
    }

    /**
     * Checks whether the graph is an even cycle
     * @param g - A weighted graph
     * @return  - True if the graph is an even cycle, false otherwise
     */
    private boolean isEvenCycle(weighted_graph g){
        if (g.nodeSize() % 2 != 0 || g.edgeSize() != g.nodeSize()) {
            return false;
        }
        for (node_info node : g.getV()) {
            int degree = g.getV(node.getKey()).size();
            if (degree != 2) {
                return false;
            }
        }
        return true;
    }

    /**
     * Defines the arrangement of the vertices in an even cycle graph
     * @param cycle - A graph that is an even cycle
     * @return  - List of nodes
     */
    private List<Integer> cycleOrder(weighted_graph cycle){
        List<Integer> nodesOrder = new ArrayList<>();
        node_info first = cycle.getV().iterator().next();
        nodesOrder.add(first.getKey());
        int index = 0;
        while(nodesOrder.size() < cycle.nodeSize()){
            Collection<node_info> neighbors = cycle.getV(nodesOrder.get(index));
            for(node_info current : neighbors){
                if(nodesOrder.size() == 1){
                    nodesOrder.add(current.getKey());
                    index++;
                    break;
                }
                else if(nodesOrder.size() > 1 && current.getKey() != nodesOrder.get(index-1)){
                    nodesOrder.add(current.getKey());
                    index++;
                    break;
                }
            }
        }
        return nodesOrder;
    }

    /**
     * Defines the arrangement of the vertices in a path graph
     * @param path - A path graph
     * @return  - List of nodes
     */
    private List<Integer> pathOrder(weighted_graph path){
        List<Integer> nodesOrder = new ArrayList<>();
        node_info first = null;
        for (node_info node : path.getV()) {
            if (path.getV(node.getKey()).size() == 1) {
                first = node;
            }
        }
        nodesOrder.add(first.getKey());
        int index = 0;
        while(nodesOrder.size() < path.nodeSize()){
            Collection<node_info> neighbors = path.getV(nodesOrder.get(index));
            for(node_info current : neighbors){
                if(nodesOrder.size() == 1){
                    nodesOrder.add(current.getKey());
                    index++;
                    break;
                }
                else if(nodesOrder.size() > 1 && current.getKey() != nodesOrder.get(index-1)){
                    nodesOrder.add(current.getKey());
                    index++;
                    break;
                }
            }
        }
        return nodesOrder;
    }

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

    protected int[] XYZ(weighted_graph g){ // - nir (first is x)
        return new int[3];
    }
    protected void handleXYZcase(weighted_graph g){} // - Zigler

}
