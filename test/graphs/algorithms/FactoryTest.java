/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.Graph;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ranb
 */
public class FactoryTest {
    
    public FactoryTest() {
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
    public void test_BuildEmptyGraph() {        
        Graph g = Factory.buildEmptyGraph(100);
        assertEquals(100, g.getNumberOfVertices() );
        assertEquals(false, g.isConnected());
        assertTrue(g.getEdges().isEmpty());
    }

    /**
     * Test of buildCompleteGraph method, of class Factory.
     */
    @Test
    public void test_BuildCompleteGraph() {
        Graph g = Factory.buildCompleteGraph(100);
        assertEquals(100, g.getNumberOfVertices());
        assertEquals(100 * 99 / 2, g.getNumberOfEdges());
        
    }

    @Test
    public void test_BuildRandomGraph() {
        
    }

    @Test
    public void test_BuildCycleGraph() {
        Graph g = Factory.buildCycleGraph(100);
        assertEquals(100, g.getNumberOfVertices());
        assertEquals(100, g.getNumberOfEdges());
    }

        @Test
    public void test_BuildLineGraph() {
        Graph g = Factory.buildLineGraph(100);
        assertEquals(100, g.getNumberOfVertices());
        assertEquals(99, g.getNumberOfEdges());
    }
    
    @Test
    public void test_BuildCompleteBiPartiteGraph() {
        Graph g = Factory.buildCompleteBiPartiteGraph(50, 50 );
        assertEquals(100, g.getNumberOfVertices());
        assertEquals(50 * 50, g.getNumberOfEdges());
        assertTrue(g.isConnected());
        assertEquals(1, g.getNumberOfConnectedComponents());
    }

    @Test
    public void test_CopyVerticesFrom() {
    }

    @Test
    public void test_CopyOf() {
        Graph g = Factory.buildRandomGraph(100, 0.5);
        Graph h = Factory.copyOf(g);
        assertEquals(g,h);
        
    }

    /**
     * Test of complementOf method, of class Factory.
     */
    @Test
    public void test_ComplementOf() {
        
    }
    
}
