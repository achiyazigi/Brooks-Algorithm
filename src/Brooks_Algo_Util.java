import java.util.LinkedList;
import java.util.List;

public class Brooks_Algo_Util {
    
    private weighted_graph g;

    public Brooks_Algo_Util(weighted_graph g){
        this.g = g;
    }

    protected List<weighted_graph> SCC(){//- Zigler
        return new LinkedList<weighted_graph>();
    } 

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
