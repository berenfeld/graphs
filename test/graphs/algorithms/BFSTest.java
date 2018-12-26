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
public class BFSTest {

    public BFSTest() {
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
     * Test of BFS method, of class Algorithms.
     */
    @Test
    public void test_BFS_randomGraph() {
        Graph g = Factory.buildRandomGraph(100, 0.8);
        Graph b1 = BFS.bfs(g, g.getVertex(1));
        Graph b2 = BFS.bfs(g, g.getVertex(1));
        assertEquals(b1, b2);
    }

    @Test
    public void test_BFS_completeGraph() throws Exception {
        Graph g = Factory.buildCompleteGraph(100);
        Vertex start = g.getRandomVertex();
        Graph bfs = BFS.bfs(g, start);
        assertEquals(bfs.getVertices(), g.getVertices());

        for (Vertex v : bfs.getVertices()) {
            if (v.equals(start)) {
                assertEquals(0, v.getAttribute(BFS.BFS_VERTEX_DEPTH));
                assertEquals(new Path(bfs), v.getAttribute(BFS.BFS_PATH_FROM_ROOT));
            } else {

                assertEquals(1, v.getAttribute(BFS.BFS_VERTEX_DEPTH));
                Path path = new Path(bfs);
                path.add(bfs.getEdge(start, v));

                assertEquals(path, v.getAttribute(BFS.BFS_PATH_FROM_ROOT));
            }
        }

    }

    @Test
    public void test_BFS_completeBiPartiteGraph() throws Exception {
        Graph g = Factory.buildCompleteBiPartiteGraph(50, 50);

        assertTrue(g.isConnected());
        Vertex start = g.getRandomVertex();
        LinkedList<Vertex> pathToRoot = new LinkedList<>();
        pathToRoot.add(start);
        Graph bfs = BFS.bfs(g, start);
        assertEquals(bfs.getVertices(), g.getVertices());
        for (Vertex v : bfs.getVertices() ) {
            if (v.equals(start)) {
                assertEquals(0, v.getAttribute(BFS.BFS_VERTEX_DEPTH));
                Path pathToVertex = new Path(bfs);
                assertEquals(pathToVertex, v.getAttribute(BFS.BFS_PATH_FROM_ROOT));
            } else if (v.connectedTo(start)) {
                assertEquals(1, v.getAttribute(BFS.BFS_VERTEX_DEPTH));
                Path pathToVertex = new Path(bfs);
                pathToVertex.add(bfs.getEdge(start, v));
                assertEquals(pathToVertex, v.getAttribute(BFS.BFS_PATH_FROM_ROOT));
            } else {

                Path pathToVertex = (Path) v.getAttribute(BFS.BFS_PATH_FROM_ROOT);
                assertEquals(2, v.getAttribute(BFS.BFS_VERTEX_DEPTH));
                assertEquals(start, pathToVertex.getFirstVertex());
                assertEquals(v, pathToVertex.getLastVertex());
                assertEquals(2, pathToVertex.edges().size());
            }

        }
    }

    @Test
    public void test_BFS_cyclicGraph() {
        Graph g = Factory.buildCycleGraph(100);

        for (Vertex v : g.getVertices() ) {
            Graph bfs = BFS.bfs(g, v);
            assertEquals(bfs.getNumberOfVertices(), 100);
            assertEquals(bfs.getNumberOfEdges(), 99);
            assertEquals(true, bfs.isConnected());
            assertEquals(v.getName(), (String) bfs.getAttribute(BFS.BFS_INITIAL_VERTEX));
        }

        Vertex start = g.getFirstVertex();
        Graph bfs = BFS.bfs(g, start);
        assertEquals(50, (int) bfs.getAttribute(BFS.BFS_MAXIMUM_DEPTH));
    }

    @Test
    public void test_BFS_lineGraph() {

        Graph g = Factory.buildLineGraph(100);
        Vertex start = g.getFirstVertex();
        Graph bfs = BFS.bfs(g, start);
        assertEquals(bfs.getNumberOfVertices(), 100);
        assertEquals(bfs.getNumberOfEdges(), 99);
        assertEquals(true, bfs.isConnected());
        assertEquals(start.getName(), (String) bfs.getAttribute(BFS.BFS_INITIAL_VERTEX));

    }

}
