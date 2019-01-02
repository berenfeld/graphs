/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 *
 * @author me
 */
public class DFSTest {

    public DFSTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of DFS method, of class Algorithms.
     */
    @Test
    public void test_DFS_randomGraph() {
        Graph g = Factory.buildRandomGraph(100, 0.8);
        Graph b1 = DFS.dfs(g, g.getVertex(1));
        Graph b2 = DFS.dfs(g, g.getVertex(1));
        assertEquals(b1, b2);
        
        for (Vertex v : b1.getVertices()) {
            assertEquals(v.getColor(), DFS.DFS_COLOR_VISITED);
        }
    }


    @Test
    public void test_DFS_completeGraph() throws Exception {
        Graph g = Factory.buildCompleteGraph(100);
        Vertex start = g.getRandomVertex();
        Graph dfs = DFS.dfs(g, start);
        assertEquals(dfs.getVertices(), g.getVertices());

        assertEquals(dfs.getVertex(start.getName()).getAttribute(DFS.DFS_DISCOVERY_TIME), 1);
        assertEquals(dfs.getVertex(start.getName()).getAttribute(DFS.DFS_FINISH_TIME), dfs.getNumberOfVertices() * 2);
        for (Vertex v : dfs.getVertices()) {
            assertEquals(v.getColor(), DFS.DFS_COLOR_VISITED);
        }

    }
/*
    @Test
    public void test_DFS_completeBiPartiteGraph() throws Exception {
        Graph g = Factory.buildCompleteBiPartiteGraph(50, 50);

        assertTrue(g.isConnected());
        Vertex start = g.getRandomVertex();
        LinkedList<Vertex> pathToRoot = new LinkedList<>();
        pathToRoot.add(start);
        Graph dfs = DFS.dfs(g, start);
        assertEquals(dfs.getVertices(), g.getVertices());
        for (Vertex v : dfs.getVertices() ) {
            if (v.equals(start)) {
                assertEquals(0, v.getAttribute(DFS.DFS_VERTEX_DEPTH));
                Path pathToVertex = new Path(dfs);
                assertEquals(pathToVertex, v.getAttribute(DFS.DFS_VERTEX_PATH_FROM_ROOT));
            } else if (v.connectedTo(start)) {
                assertEquals(1, v.getAttribute(DFS.DFS_VERTEX_DEPTH));
                Path pathToVertex = new Path(dfs);
                pathToVertex.add(dfs.getEdge(start, v));
                assertEquals(pathToVertex, v.getAttribute(DFS.DFS_VERTEX_PATH_FROM_ROOT));
            } else {

                Path pathToVertex = (Path) v.getAttribute(DFS.DFS_VERTEX_PATH_FROM_ROOT);
                assertEquals(2, v.getAttribute(DFS.DFS_VERTEX_DEPTH));
                assertEquals(start, pathToVertex.getFirstVertex());
                assertEquals(v, pathToVertex.getLastVertex());
                assertEquals(2, pathToVertex.edges().size());
            }

        }
    }

    @Test
    public void test_DFS_cyclicGraph() {
        Graph g = Factory.buildCycleGraph(100);

        for (Vertex v : g.getVertices() ) {
            Graph dfs = DFS.dfs(g, v);
            assertEquals(dfs.getNumberOfVertices(), 100);
            assertEquals(dfs.getNumberOfEdges(), 99);
            assertEquals(true, dfs.isConnected());
            assertEquals(v.getName(), (String) dfs.getAttribute(DFS.DFS_INITIAL_VERTEX));
        }

        Vertex start = g.getFirstVertex();
        Graph dfs = DFS.dfs(g, start);
        assertEquals(50, (int) dfs.getAttribute(DFS.DFS_MAXIMUM_DEPTH));
    }

    @Test
    public void test_DFS_lineGraph() {

        Graph g = Factory.buildLineGraph(100);
        Vertex start = g.getFirstVertex();
        Graph dfs = DFS.dfs(g, start);
        assertEquals(dfs.getNumberOfVertices(), 100);
        assertEquals(dfs.getNumberOfEdges(), 99);
        assertEquals(true, dfs.isConnected());
        assertEquals(start.getName(), (String) dfs.getAttribute(DFS.DFS_INITIAL_VERTEX));

    }
*/
}
