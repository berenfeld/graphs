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
public class GraphTestDirected {

    public GraphTestDirected() {
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


    @Test
    public void test_completeGraph() {
        Graph g = Factory.buildCompleteGraph(100, true);
        assertEquals(g.getVertices().size(), 100);
        assertEquals(g.getEdges().size(), 100 * 99);
        
        //assertEquals(1, g.diameter());
    }

    @Test
    public void test_addAndRemoveEdges() {
        try {
            Graph g = Factory.buildEmptyGraph(100, true);
            for (int i=1;i<=101;i++) {
                for (int j=i+1;j<101;j++) {
                    g.addEdge(g.getVertex(i), g.getVertex(j));
                    g.addEdge(g.getVertex(j), g.getVertex(i));
                }            
            }
            assertEquals(g.getNumberOfEdges(), 100 * 99);
            for (int i=1;i<=101;i++) {
                for (int j=i+1;j<101;j++) {
                    g.removeEdge(g.getVertex(i), g.getVertex(j));
                    g.removeEdge(g.getVertex(j), g.getVertex(i));
                }            
            }
            assertEquals(g.getNumberOfEdges(), 0);
        } catch (Exception ex) {
            Utils.exception(ex);
            assertTrue(false);
        }
    }
    
    @Test
    public void test_addAndRemoveEdges_random() {
        try {
            Graph g = Factory.buildEmptyGraph(10, true);
            int totalEdges = 10 * 9;
            
            while (! g.isComplete()) {
                Graph gc = Factory.complementOf(g);
                assertNotNull(gc);
                assertEquals(gc.getNumberOfEdges(), totalEdges - g.getNumberOfEdges());
                Edge e = gc.getRandomEdge();
                g.addEdge(e.getFromVertex(), e.getToVertex());
            }
            assertEquals(g.getNumberOfEdges(), totalEdges);
            while (! g.isEmpty()) {
                g.removeEdge(g.getRandomEdge());
            }
            for (Vertex v : g.getVertices()) {
                assertEquals(v.getOutgoingEdges().size(), 0);
            }
            assertEquals(g.getNumberOfEdges(), 0);
        } catch (Exception ex) {
            Utils.exception(ex);
            assertTrue(false);
        }
    }
    
    @Test
    public void notest_Diameter_cyclicGraph() {
        Graph g = Factory.buildCycleGraph(100);
        assertEquals(50, g.diameter());
    }

    @Test
    public void notest_Diameter_completeBipartiteGraph() {
        Graph g = Factory.buildCompleteBiPartiteGraph(50, 50);
        assertEquals(2, g.diameter());
    }

    @Test
    public void notest_connectivity() throws Exception {
        Graph g = Factory.buildEmptyGraph(100);
        Graph complete = Factory.complementOf(g);

        int numberOfConnectedComponentes = g.getNumberOfConnectedComponents();
        assertEquals(1, complete.getNumberOfConnectedComponents());
        assertEquals(1, complete.diameter());
        assertEquals(100, numberOfConnectedComponentes);

        while (!g.isComplete()) {
            Edge randomEdge = complete.getRandomEdge();
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

        assertEquals(100, numberOfConnectedComponentes);
    }

    @Test
    public void notest_Diameter_growingAndShrinkingGraph() throws Exception {
        Graph g = Factory.buildEmptyGraph(10);
        Graph complete = Factory.complementOf(g);

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
