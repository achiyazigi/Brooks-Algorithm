
import java.util.*;

/**
 * this class holds the utilities to find and color
 * a legit node coloring with deltaG colors.
 * (assuming the graph isn't complete or odd cycle)
 */
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

    public void init(weighted_graph g){
        this.g = g;
    }

    public void updateColoring() throws Exception{
        List<weighted_graph> components = SCC();
        for(weighted_graph comp: components){
            int delta = deltaG(comp);
            if(delta < 3){ // the component is an even Cycle or a Path
                if(delta == 2){ // greedy it is!
                    handleDeltaTwo(comp);
                }
                else{
                    GreedyColoring(arbitrary_order(comp));
                }
            }
            else{ // deltaG >= 3
                int root = findRoot(comp);
                if(root >= 0){ // not d-regular
                    List<Integer> order = spanningTreeOrder(comp, root);
                    GreedyColoring(order);
                }
                else{ // d-regular
                    if(isClique(comp)){ // clique can be colored with at least deltaG + 1 colors
                        GreedyColoring(arbitrary_order(comp));
                    }
                    else{ // not cilque, lets examine local connectivuty
                        int sep = isOneConnected(comp);
                        if(sep < 0){ // not 1 connected, existence of xyz promised
                            handleXYZcase(comp);
                        }
                        else{ // 1 connected d-regular graph! 
                            handleOneConnected(comp, sep);
                        }
                    }
                }
            }
        }
    }

    protected List<Integer> arbitrary_order(weighted_graph local_g){
        List<Integer> order = new LinkedList<>();
        for(node_info node: local_g.getV()){
            order.add(node.getKey());
        }
        return order;
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

    /**
     * creates a graph from list of keys based on initialized graph: g
     * @param comp
     * @return the subgraph of g
     */
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

    /**
     * simple dfs round, saving the current tree keys in a List
     * @param n
     * @return the list of all keys in the tree.
     */
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
        reset();
        HashSet<Integer> setOfNeiColors = new HashSet<Integer>();
        for(int node_id : order){   // for each node in the list
            setOfNeiColors.clear();	// make a list of it's neighbors's colors
            for (node_info nei : g.getV(node_id)){
            	if(order.contains(nei.getKey()) && nei.getTag() == 1) {
            		setOfNeiColors.add(nei.getColor());
            	}
            }
            color = 1;
            while(setOfNeiColors.contains(color)){color++;}				//find the minimum color not used by a neighbor
            g.getNode(node_id).setColor(color);     // color the node
            g.getNode(node_id).setTag(1);
        }
    }

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

    /**
     * 
     * @param g - graph 
     * @return true - if the graph is clique, otherwise false
     */
    protected boolean isClique(weighted_graph comp){ // - Eviatar
        return  comp.edgeSize() == ( comp.nodeSize() * ( comp.nodeSize() - 1 )) / 2 ;
    }

    /**
     * Defines the arrangement of the vertices in a graph (even cycle or path)
     * and sends them to the GreedyColoring methods.
     * In a path graph and an even cycle graph the coloring is simple (2-coloring)
     * so all it has to do is to arrange the vertices and send them for coloring.
     * @param g - A graph that is a path or an even cycle
     */
    protected void handleDeltaTwo(weighted_graph g){
        List<Integer> nodesOrder;
        if(isEvenCycle(g)){
            nodesOrder = cycleOrder(g);
            GreedyColoring(nodesOrder);
        }
        else if(isPath(g)){
            nodesOrder = pathOrder(g);
            GreedyColoring(nodesOrder);
        }
        else{
            GreedyColoring(arbitrary_order(g));
        }
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
     * Checks whether the graph is a path
     * @param g - A weighted graph
     * @return  - True if the graph is a path, false otherwise
     */
    private boolean isPath(weighted_graph g){
        if (g.edgeSize() != g.nodeSize() - 1) {
            return false;
        }
        int degreeOne = 0, degreeTwo = 0;
        for (node_info node : g.getV()) {
            int degree = g.getV(node.getKey()).size();
            if(degree == 1){
                degreeOne++;
            }
            else if(degree == 2){
                degreeTwo++;
            }
            else {
                return false;
            }
        }
        return degreeOne == 2 && degreeTwo == g.nodeSize()-2;
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
        int index = 1;
        while(nodesOrder.size() < cycle.nodeSize()){
            Collection<node_info> neighbors = cycle.getV(nodesOrder.get(index-1));
            for(node_info current : neighbors){
                if(nodesOrder.size() == 1 || current.getKey() != nodesOrder.get(index-2)){
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
            if (path.getV(node.getKey()).size() == 1) { first = node; }
        }
        nodesOrder.add(first.getKey());
        int index = 1;
        while(nodesOrder.size() < path.nodeSize()){
            Collection<node_info> neighbors = path.getV(nodesOrder.get(index-1));
            for(node_info current : neighbors){
                if(nodesOrder.size() == 1 || current.getKey() != nodesOrder.get(index-2)){
                    nodesOrder.add(current.getKey());
                    index++;
                    break;
                }
            }
        }
        return nodesOrder;
    }

    /**
     * 
     * @param local_g
     * @return a node key with degree less than deltaG, -1 d-regular
     */
    protected int findRoot(weighted_graph local_g){
        int delta = this.deltaG(local_g);
        int node_key;
        for(node_info n : local_g.getV()){
            node_key = n.getKey();
            if(local_g.getV(node_key).size() < delta){
                return node_key;
            }
        }
        return -1;
    }
    
    /**
     * @param g- graph that need to do on him spaning tree
     * @param root - from where to start the spanning tree
     * @return list - an order of the nodes as the last node is the root
     **/
    protected List<Integer> spanningTreeOrder(weighted_graph g, int root){ // - Eviatar
    	weighted_graph ch = new WGraph_DS();
    	for(node_info node:g.getV()) {	 //create new graph with the same nodes 
    		ch.addNode(node.getKey());
    	}
    	
    	bfs(root,ch, g);	//take two random nodes
    	for(node_info node: ch.getV()) {
    		node.setTag(-1);  		// yet didn't visit him using in postOrderTraversal
    	}
    	ch.getNode(root).setTag(0);	
    	LinkedList<Integer> list = new LinkedList<Integer>();
    	postOrderTraveral(root,ch,list);  
        return list;
    }
    
	/**
	 * @param get src
	 * the run time of this function is o(v+e)
	 * sign in the tag of every node the distance from the src 
	 * and if havn't path to which node her tag will be with -1
	 * the run time of this function is o(v+e)
	 */
	private void bfs(int src,weighted_graph ch, weighted_graph original) {
		if(original.getV().isEmpty())								//check if it is empty graph
			return;
		
		for(node_info keys : original.getV()) {						//initial the tag's nodes- O(V)
			keys.setTag(-1);
		}
		Queue<Integer> queue=new LinkedList<Integer>();
		queue.add(original.getNode(src).getKey());				//keep the key in the queue
		original.getNode(src).setTag(0);				//the first node init with 0 										
		ch.getNode(src).setTag(0);
		
		while(!queue.isEmpty()) {
			Integer loc=queue.poll();
				if(original.getV(loc).isEmpty() == false) {			//check if have neighbors for the specific node
					for(node_info key : original.getV(loc)) {
						if(key.getTag() == -1) {
							queue.add(key.getKey());
							key.setTag(original.getNode(loc).getTag()+1);			//if have neighbor update the the tag according to his parent
							ch.connect(loc, key.getKey(), 0);
						}
				}
			}
		}
	}
	/**
	 * 
	 * @param root - from to start the postorder traversal
	 * @param ch - the new graph - spanning graph
	 * @param list - list that contain the postroder traversal on the nodes
	 */
	public void postOrderTraveral(int root, weighted_graph ch,LinkedList<Integer> list) {

		for(node_info node: ch.getV(root)){ //move on all his neighbors
			if(node.getTag() == -1) {
				node.setTag(0);
				postOrderTraveral(node.getKey(), ch, list);
			}
		}
		list.add(root);
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

    /**
     * performing gridyColoring on 2 parts of subgraph local_g.
     * if the seperator color is not the same in both parts,
     * switching colors on the 2nd part is being performed.
     * @param local_g
     * @param sep
     * @throws Exception when the subgraph isn't one connected.
     */
    protected void handleOneConnected(weighted_graph local_g, int sep) throws Exception{
        HashSet<Integer> sep_nei = new HashSet<>();
        for(node_info n: local_g.getV(sep)){
            sep_nei.add(n.getKey());
        }
        
        local_g.removeNode(sep);
        Brooks_Algo_Util ba = new Brooks_Algo_Util();
        ba.init(local_g);
        List<weighted_graph> seperated = ba.SCC();
        local_g.addNode(sep);
        if(seperated.size() > 2){
            throw new Exception("local_g isn't OneConnected");
        }
        for (weighted_graph part_of_g : seperated) {
            part_of_g.addNode(sep);
            for (int potential_nei : sep_nei) {
                part_of_g.connect(sep, potential_nei, 1);
                local_g.connect(sep, potential_nei, 1);
            }
        }
        List<Integer> part_one = this.spanningTreeOrder(seperated.get(0), sep);
        List<Integer> part_two = this.spanningTreeOrder(seperated.get(1), sep);
        this.GreedyColoring(part_one);
        node_info sep_in_g = g.getNode(sep);
        int root_color = sep_in_g.getColor(); // keep the root's color before coloring the 2nd part
        this.GreedyColoring(part_two);
        if(root_color != sep_in_g.getColor()){ // the color of sep came out differently
            SwitchColorsToFit(seperated.get(1), sep, root_color); // missmatch of colors in 2nd part of local g
        }

    }

    /**
     * swap color of sep with disired_root_color's color,
     * along all nodes in subgraph local_g.
     * @param local_g
     * @param sep
     * @param disired_root_color
     */
    private void SwitchColorsToFit(weighted_graph local_g, int sep, int disired_root_color) {
        int color_to_switch = g.getNode(sep).getColor();
        for (node_info v : local_g.getV()) {
            node_info original_v = g.getNode(v.getKey());
            if(original_v.getColor() == color_to_switch){
                original_v.setColor(disired_root_color);
            }
            else if(original_v.getColor() == disired_root_color){
                original_v.setColor(color_to_switch);
            }
        }
    }

    /**
     * for testing
     * @param local_g
     * @return highest color in a subgraph local_g
     */
    public int get_highest_color(weighted_graph local_g) {
        int max = 0;
        for (node_info v : local_g.getV()) {
            int cur_color = g.getNode(v.getKey()).getColor();
            if(cur_color > max){
                max = cur_color;
            }
        }
        return max;
    }

    
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


    /**
     * get xyz, change order of tree rooted with x like so:
     * res := (z,y,.....,x)
     * color y&z with the color 1,
     * perform gridyColoring on res.
     * @param local_g
     */
    protected void handleXYZcase(weighted_graph local_g){
        int[] XYZ = XYZ(local_g);
        int x = XYZ[0];
        int y = XYZ[1];
        int z = XYZ[2];
        List<Integer> res = spanningTreeOrder(local_g, x);
        int index_y = res.indexOf(y);
        int index_z = res.indexOf(z);
        res.remove(index_y);
        res.remove(index_z);
        g.getNode(y).setColor(1);
        g.getNode(z).setColor(1);
        res.add(0, y);
        res.add(0, z);
        GreedyColoring(res);
    }

}