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
public class PrimTest {

    public PrimTest() {
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
    public void test_Prim_completeGraph() throws Exception {
        Graph g = Factory.buildCompleteGraph(100);
        for (int i = 1; i <= 100; i++) {
            for (int j = i + 1; j <= 100; j++) {
                g.getEdge(g.getVertex(i), g.getVertex(j)).setWeight(i + j - 1);
            }
        }
        Graph prim = Prim.minimumWeightSpanningTree(g, g.getFirstVertex());
        assertEquals(prim.getNumberOfVertices(), 100);
        assertEquals(prim.getNumberOfEdges(), 99);
        assertEquals((100 * 101 / 2) - 1, prim.getSumOfWeightsOfEdges() );
    }

    @Test
    public void test_Prim_randomWeights() throws Exception {

        Random rand = new Random();
        Graph g = Factory.buildCompleteGraph(100);

        for (Edge e : g.getEdges() ) {
            e.setWeight(rand.nextInt(100));
        }
        Graph prim = Prim.minimumWeightSpanningTree(g);
        assertEquals(prim.getNumberOfVertices(), 100);
        assertEquals(prim.getNumberOfEdges(), 99);
        

    }

}
