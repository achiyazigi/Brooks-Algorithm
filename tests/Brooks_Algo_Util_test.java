import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
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
        Brooks_Algo_Util ba = new Brooks_Algo_Util();
        ba.init(g);

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
        Brooks_Algo_Util ba = new Brooks_Algo_Util();
        ba.init(g);

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
        Brooks_Algo_Util ba = new Brooks_Algo_Util();
        ba.init(g);

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
    
    @Test
    void Greedy_Algo() {
    	
    	weighted_graph g = new WGraph_DS();
    	for(int i = 0; i < 5; i++) {g.addNode(i);}
    	for(int i = 0; i < 5; i++) {g.connect(i, (i+1)%5, 0);}
    	List<Integer> order = new LinkedList<Integer>();
    	for(int i = 0; i < 5; i++) {order.add(i);}
    	
    	Brooks_Algo_Util ba = new Brooks_Algo_Util();
        ba.init(g);

    	ba.GreedyColoring(order);
    	
    	
    	assertEquals(1, g.getNode(0).getColor());
    	assertEquals(2, g.getNode(1).getColor());
    	assertEquals(1, g.getNode(2).getColor());
    	assertEquals(2, g.getNode(3).getColor());
    	assertEquals(3, g.getNode(4).getColor());
    	
    	weighted_graph g1 = graph_creator(4);
    	g1.connect(0, 1, 0);
    	g1.connect(0, 2, 0);
    	g1.connect(1, 2, 0);
    	g1.connect(2, 3, 0);
    	
    	List<Integer> order1 = new LinkedList<Integer>();
    	order1.add(2); order1.add(1); order1.add(0); order1.add(3);
    	Brooks_Algo_Util ba1 = new Brooks_Algo_Util();
        ba1.init(g1);

    	ba1.GreedyColoring(order1);
    	
    	assertEquals(3, g1.getNode(0).getColor());
    	assertEquals(2, g1.getNode(1).getColor());
    	assertEquals(1, g1.getNode(2).getColor());
    	assertEquals(2, g1.getNode(3).getColor());
    }
    
    @Test
    void XYZ() {
    	
    	weighted_graph g = make_k_n_n(3);
    	Brooks_Algo_Util ba = new Brooks_Algo_Util();
        ba.init(g);

    	int[] xyz = ba.XYZ(g);
    	int x = xyz[0], y = xyz[1], z = xyz[2];
    	
    	assertTrue(g.hasEdge(x, y));
    	assertTrue(g.hasEdge(x, z));
    	assertFalse(g.hasEdge(y, z));
    	
    	g.removeNode(z);
    	g.removeNode(y);
    	weighted_graph_algorithms ga = new WGraph_Algo();
    	ga.init(g);
    	
    	assertTrue(ga.isConnected());
    }
    
    @Test
    public void findRoot(){
        weighted_graph g = graph_creator(100);
        for (int i = 0; i < g.nodeSize(); i++) {
            
            connectToAll(g, i);
        }
        Brooks_Algo_Util ba = new Brooks_Algo_Util();
        ba.init(g);
        assertEquals(-1, ba.findRoot(g));
        g.removeEdge(50, 51);
        int root_key = ba.findRoot(g);
        assertTrue(root_key ==51 || root_key == 50);
        g.removeEdge(50, 52);
        root_key = ba.findRoot(g);
        assertTrue(root_key ==50);
        for (int i = 1; i < g.nodeSize(); i++) {
            g.removeEdge(0, i);
        }
        root_key = ba.findRoot(g);
        assertTrue(root_key == 0 || root_key == 50 || root_key == 51);
    }

    @Test
    public void handleOneConnected(){
        weighted_graph g = graph_creator(9);
        for (int i = 0; i < g.nodeSize(); i++) {
            g.connect(i, 4, 1); // seperator is 4
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.connect(i, j, 1);
            }
        }
        for (int i = 5; i < g.nodeSize(); i++) {
            for (int j = 5; j < g.nodeSize(); j++) {
                g.connect(i, j, 1);
            }
        }

        Brooks_Algo_Util ba = new Brooks_Algo_Util();
        ba.init(g);

        try{ // case1: seperator has the same color in both parts (easy)

            ba.handleOneConnected(g, 4);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        assertTrue(legalColoring(g));
        assertTrue(ba.get_highest_color(g) <= ba.deltaG(g));


        g.addNode((int)g.getHighest_key()+1); // adding 1 node
        for (int i = 0; i <= 4; i++) {
            g.connect(i, (int)g.getHighest_key(), 1); // connecting it with all nodes in the 1st part
        }
        
        try{ // case2: seperator has different color in each part (hard)
            
            ba.handleOneConnected(g, 4);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        assertTrue(legalColoring(g));
        assertTrue(ba.get_highest_color(g) <= ba.deltaG(g));
    }

    /**
     * generate full bipartite graph with n vx on each side
     * @param n
     * @return
     */
    private static weighted_graph make_k_n_n(int n) {
    	weighted_graph g = graph_creator(2*n);
    	for(int i = 0; i < n; i++) {
    		for(int j = n; j < 2*n; j++) {
    			g.connect(i, j, 0);
    		}
    	}
    	return g;
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

    private static void printColoring(weighted_graph g){
        for (node_info  n : g.getV()) {
            System.out.print("(key: " + n.getKey() + ", color: " + n.getColor() + ") ");
        }
        System.out.println();
    }

    private static boolean legalColoring(weighted_graph g){
        for (node_info u : g.getV()) {
            int u_color = u.getColor();
            for (node_info v : g.getV(u.getKey())){
                if(u_color == v.getColor()){
                    System.out.println(v + " " + v.getColor() + " , " + u + " " + u_color);
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    public void spanning_tree(){
    	weighted_graph ch =new WGraph_DS();
    	ch.addNode(0);
    	ch.addNode(1);
    	ch.addNode(2);
    	ch.addNode(3);
    	ch.addNode(4);
    	ch.addNode(5);
    	ch.connect(0, 1, 0);
    	ch.connect(0, 2, 0);
    	ch.connect(1, 3, 0);
    	ch.connect(1, 4, 0);
    	ch.connect(1, 5, 0);
    	ch.connect(1, 2, 0);
    	ch.connect(2, 4, 0);
    	ch.connect(2, 3, 0);
    	ch.connect(3, 5, 0);
    	ch.connect(3, 4, 0);
    	ch.connect(4, 5, 0);
    	ch.connect(0, 5, 0);
    	Brooks_Algo_Util alg = new Brooks_Algo_Util();
        alg.init(ch);

    	LinkedList<Integer> list = new LinkedList<Integer>();
    	//check if the root return at the last of the linked list
    	list = (LinkedList<Integer>) alg.spanningTreeOrder(ch, 0);
    	assertTrue(list.getLast() == 0);
     	list = (LinkedList<Integer>) alg.spanningTreeOrder(ch, 1);
    	assertTrue(list.getLast() == 1);
     	list = (LinkedList<Integer>) alg.spanningTreeOrder(ch, 5);
    	assertTrue(list.getLast() == 5);
     	list = (LinkedList<Integer>) alg.spanningTreeOrder(ch, 3);
    	assertTrue(list.getLast() == 3);
    	
    }
    @Test
    public void is_clique(){
    	weighted_graph ch =new WGraph_DS();
    	ch.addNode(0);
    	ch.addNode(1);
    	ch.addNode(2);
    	ch.addNode(3);
    	ch.connect(0, 1, 0);
    	ch.connect(0, 2, 0);
    	ch.connect(0, 3, 0);
    	ch.connect(2, 3, 0);
    	ch.connect(2, 1, 0);
    	ch.connect(1, 3, 0);

    	Brooks_Algo_Util alg = new Brooks_Algo_Util();
        alg.init(ch);

    	assertTrue(alg.isClique(ch));
    	
    	ch.removeEdge(0, 1);
    	assertFalse(alg.isClique(ch));
    }

    @Test
    public void handleXYZcase(){
        weighted_graph g = graph_creator(1000);
        for (int i = 0; i < g.nodeSize(); i++) {
            connectToAll(g, i);
        }
        for (int i = 0; i < g.nodeSize(); i++) {
            g.removeEdge(i, (i+1)%g.nodeSize());
        }
        Brooks_Algo_Util ba = new Brooks_Algo_Util();
        ba.init(g);
        ba.handleXYZcase(g);
        assertEquals(ba.deltaG(g), g.nodeSize()-3);
        assertTrue(legalColoring(g));
        assertTrue(ba.get_highest_color(g) <= ba.deltaG(g));

        g = new WGraph_DS();
        g = graph_creator(100);
        for (int i = 0; i < g.nodeSize(); i++) {
            connectToAll(g, i);
        }
        
        for (int i = 0; i < g.nodeSize(); i++) {
            g.removeEdge(i, (i+2)%g.nodeSize());
            g.removeEdge(i, (i+1)%g.nodeSize());
        }
        ba = new Brooks_Algo_Util();
        ba.init(g);
        ba.handleXYZcase(g);
        assertEquals(ba.deltaG(g), g.nodeSize()-5);
        assertTrue(legalColoring(g));
        assertTrue(ba.get_highest_color(g) <= ba.deltaG(g));
    }
}
