import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class Brooks_Algo_Util_test {
    @Test
    public void SCC(){
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 0);
        Brooks_Algo_Util ba = new Brooks_Algo_Util(g);
        List<weighted_graph> scc = ba.SCC();
        assertEquals(scc.size(), 3);
        g.connect(3, 2, 0);
        scc = ba.SCC();
        assertEquals(scc.size(), 2);
        g.connect(1, 2, 0);
        scc = ba.SCC();
        assertEquals(scc.size(), 1);
    }
    @Test
    void deltaG(){
        weighted_graph g=graph_creator(100);
        Brooks_Algo_Util ba = new Brooks_Algo_Util(g);
        assertEquals(0, ba.deltaG(g));
        g.connect(0,1,1);
        g.connect(0,2,1);
        g.connect(1,2,1);
        g.connect(3,1,1);
        assertEquals(3, ba.deltaG(g));
        g=small_graph();
        assertEquals(4, ba.deltaG(g));
        connectToAll(g,0);
        assertEquals(g.getV().size()-1, ba.deltaG(g));


    }
    @Test
    void isOneConnected(){
        weighted_graph g=graph_creator(3);
        Brooks_Algo_Util ba = new Brooks_Algo_Util(g);
        g.connect(0,1,1);
        g.connect(1,2,1);
        assertEquals(1, ba.isOneConnected(g));
        g=small_graph();
        assertEquals(-1,ba.isOneConnected(g));
        g.removeEdge(3,6);
        g.removeEdge(3,9);
        g.removeEdge(4,10);
        assertEquals(5,ba.isOneConnected(g));
        g.connect(5,6,1);
        assertEquals(5,ba.isOneConnected(g));
        connectToAll(g,5);
        assertEquals(5,ba.isOneConnected(g));
        connectToAll(g,8);
        assertEquals(-1,ba.isOneConnected(g));

    }

    /***
     * Connect a given node to all the other node in the graph.
     * @param g - graph.
     * @param key - node.
     */
    public static void connectToAll(weighted_graph g,int key){
        for (node_info node:g.getV()) {
            g.connect(key,node.getKey(),1);
        }
    }
    /***
     * creat graph without edges
     * @param v_size-|v|
     * @return graph
     */
    public static weighted_graph graph_creator(int v_size) {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < v_size; i++) {

            g.addNode(i);
        }
        return g;
    }

    /***
     * Creat small graph
     * @return weighted_graph.
     */
    public static weighted_graph small_graph() {
        weighted_graph g0 = graph_creator(11);
        g0.connect(0, 1, 1);
        g0.connect(0, 2, 1);
        g0.connect(0, 3, 1);

        g0.connect(1, 4, 1);
        g0.connect(1, 5, 1);
        g0.connect(2, 4, 1);
        g0.connect(3, 5, 1);
        g0.connect(3, 6, 1);
        g0.connect(5, 7, 1);
        g0.connect(6, 7, 1);
        g0.connect(7, 10, 1);
        g0.connect(6, 8, 1);
        g0.connect(8, 10, 1);
        g0.connect(4, 10, 1);
        g0.connect(3, 9, 1);
        g0.connect(10, 9, 1);


        return g0;
    }
}
