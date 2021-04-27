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
}
