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
     * NOTE! for diGraph is the future change to:
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

    protected int deltaG(weighted_graph g){ // - Kafka
        return 0;
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
    protected int isOneConnected(weighted_graph g){// - Kafka returns -1 if false
        return -1;
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
