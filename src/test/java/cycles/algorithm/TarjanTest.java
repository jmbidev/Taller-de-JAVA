package cycles.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import java.util.List;


public class TarjanTest {

    Graph graph;
    Tarjan tarjan;

    @Before
    public void before() {
        graph = new Graph<String>(true);

        Vertex v0,v1,v2,v3,v4,v5,v6,v7;

        v0 = new Vertex<String>(0);
        v4 = new Vertex<String>(4);
        v3 = new Vertex<String>(3);
        v1 = new Vertex<String>(1);
        v2 = new Vertex<String>(2);
        v5 = new Vertex<String>(5);
        v6 = new Vertex<String>(6);
        v7 = new Vertex<String>(7);

        v0.setData("0");
        v1.setData("1");
        v2.setData("2");
        v3.setData("3");
        v4.setData("4");
        v5.setData("5");
        v6.setData("6");
        v7.setData("7");

        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        graph.addVertex(v7);

        graph.addEdge(v4.getId(), v5.getId(), v4.getData(), v5.getData());
        graph.addEdge(v5.getId(), v6.getId(), v5.getData(), v6.getData());
        graph.addEdge(v6.getId(), v4.getId(), v6.getData(), v4.getData());
        graph.addEdge(v1.getId(), v4.getId(), v1.getData(), v4.getData());
        graph.addEdge(v0.getId(), v1.getId(), v0.getData(), v1.getData());
        graph.addEdge(v1.getId(), v7.getId(), v1.getData(), v7.getData());
        graph.addEdge(v3.getId(), v0.getId(), v3.getData(), v0.getData());
        graph.addEdge(v2.getId(), v0.getId(), v2.getData(), v0.getData());
        graph.addEdge(v1.getId(), v2.getId(), v1.getData(), v2.getData());
        graph.addEdge(v7.getId(), v2.getId(), v7.getData(), v2.getData());
        graph.addEdge(v2.getId(), v3.getId(), v2.getData(), v3.getData());

        tarjan = new Tarjan();
    }

    @Test
    public void cyclesCount() {
        assertEquals(5, tarjan.findCycles(graph, graph.getVertexes().size(), false, "", "").size());
    }

    @Test
    public void cyclesExactCount() {
        assertEquals(0, tarjan.findCycles(graph, 1, true, "", "").size());
        assertEquals(0, tarjan.findCycles(graph, 2, true, "", "").size());
        assertEquals(2, tarjan.findCycles(graph, 3, true, "", "").size());
        assertEquals(2, tarjan.findCycles(graph, 4, true, "", "").size());
        assertEquals(1, tarjan.findCycles(graph, 5, true, "", "").size());
        assertEquals(0, tarjan.findCycles(graph, 6, true, "", "").size());
    }

    @Test
    public void cyclesBetween0_Count(){
        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "1").size());
        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "2").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "3").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "4").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "5").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "6").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "7").size());

        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "0").size());
        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "0").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "0").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "0").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "0").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "0").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "0").size());
    }

    @Test
    public void cyclesBetween1_Count(){
        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "0").size());
        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "2").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "3").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "4").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "5").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "6").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "7").size());

        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "1").size());
        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "1").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "1").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "1").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "1").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "1").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "1").size());
    }

    @Test
    public void cyclesBetween2_Count(){
        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "0").size());
        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "1").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "3").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "4").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "5").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "6").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "7").size());

        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "2").size());
        assertEquals(4, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "2").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "2").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "2").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "2").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "2").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "2").size());
    }

    @Test
    public void cyclesBetween3_Count(){
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "0").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "1").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "2").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "4").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "5").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "6").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "7").size());

        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "3").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "3").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "3").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "3").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "3").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "3").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "3").size());
    }

    @Test
    public void cyclesBetween4_Count(){
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "0").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "1").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "2").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "3").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "5").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "6").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "7").size());

        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "4").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "4").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "4").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "4").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "4").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "4").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "4").size());
    }

    @Test
    public void cyclesBetween5_Count(){
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "0").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "1").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "2").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "3").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "4").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "6").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "7").size());

        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "5").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "5").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "5").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "5").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "5").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "5").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "5").size());
    }

    @Test
    public void cyclesBetween6_Count(){
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "0").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "1").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "2").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "3").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "4").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "5").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "7").size());

        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "6").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "6").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "6").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "6").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "6").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "6").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "6").size());
    }

    @Test
    public void cyclesBetween7_Count(){
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "0").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "1").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "2").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "3").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "4").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "5").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "7", "6").size());

        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "0", "7").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "1", "7").size());
        assertEquals(2, tarjan.findCycles(graph, graph.getVertexes().size(), false, "2", "7").size());
        assertEquals(1, tarjan.findCycles(graph, graph.getVertexes().size(), false, "3", "7").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "4", "7").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "5", "7").size());
        assertEquals(0, tarjan.findCycles(graph, graph.getVertexes().size(), false, "6", "7").size());
    }


    @After
    public void after() {
        graph = null;
        tarjan = null;
    }
}
