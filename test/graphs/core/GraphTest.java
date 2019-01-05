/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.core;

import graphs.algorithms.Factory;
import graphs.utils.Utils;
import java.util.Map;
import java.util.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author me
 */
public class GraphTest {

    public GraphTest() {
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
     * Test of toString method, of class Graph.
     */
    @Test
    public void test_Diameter_completeGraph() {
        Graph g = Factory.buildCompleteGraph(100);
        assertEquals(1, g.diameter());
    }

    @Test
    public void test_Diameter_cyclicGraph() {
        Graph g = Factory.buildCycleGraph(100);
        assertEquals(50, g.diameter());
    }

    @Test
    public void test_Diameter_completeBipartiteGraph() {
        Graph g = Factory.buildCompleteBiPartiteGraph(50, 50);
        assertEquals(2, g.diameter());
    }

    @Test
    public void test_connectivity() throws Exception {
        Utils.debug("test started");
        Graph g = Factory.buildEmptyGraph(10);
        
        Graph complete = Factory.complementOf(g);
        int numberOfConnectedComponentes = g.getNumberOfConnectedComponents();
        assertEquals(1, complete.getNumberOfConnectedComponents());
        assertEquals(1, complete.diameter());
        assertEquals(10, numberOfConnectedComponentes);

        Utils.debug("complete graph ready");
        while (!g.isComplete()) {
            Edge randomEdge = complete.getRandomEdge();
            Utils.debug("adding edge " + randomEdge);
            g.addEdge(randomEdge);
            complete.removeEdge(randomEdge);
            assertTrue(g.getNumberOfConnectedComponents() <= numberOfConnectedComponentes);
            numberOfConnectedComponentes = g.getNumberOfConnectedComponents();
        }

        while (g.getNumberOfEdges() > 0) {
            g.removeEdge(g.getRandomEdge());
            assertTrue(g.getNumberOfConnectedComponents() >= numberOfConnectedComponentes);
            numberOfConnectedComponentes = g.getNumberOfConnectedComponents();
        }

        assertEquals(10, numberOfConnectedComponentes);
    }

    @Test
    public void test_Diameter_growingAndShrinkingGraph() throws Exception {
        Graph g = Factory.buildEmptyGraph(10);
        Graph complete = Factory.complementOf(g);
        assertNotNull(complete);

        int diameter = g.diameter();
        assertEquals(Integer.MAX_VALUE, diameter);

        while (!g.isComplete()) {
            Edge randomEdge = complete.getRandomEdge();
            g.addEdge(randomEdge);
            complete.removeEdge(randomEdge);

            if (!g.isConnected()) {
                assertEquals(Integer.MAX_VALUE, diameter);
            } else {
                assertTrue(g.diameter() + " should be <= " + diameter, g.diameter() <= diameter);
                diameter = g.diameter();
            }
        }

        while (g.getNumberOfEdges() > 0) {
            g.removeEdge(g.getRandomEdge());

        }
    }
}
