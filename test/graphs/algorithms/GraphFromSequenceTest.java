/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.*;
import graphs.utils.Utils;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 *
 * @author me
 */
public class GraphFromSequenceTest {

    public GraphFromSequenceTest() {
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
    public void test_GraphFromSequence1() throws Exception {
        Graph g = GraphFromDegreeSequence.fromDegreeSequence(Utils.parseList("2,2,2,2"));
        assertEquals(g.getNumberOfVertices(), 4);
        assertEquals(g.getNumberOfEdges(), 4);
    }

    @Test
    public void test_GraphFromSequence2() throws Exception {
        Graph g = GraphFromDegreeSequence.fromDegreeSequence(Utils.parseList("3,3,3,3,3,3"));
        assertEquals(g.getNumberOfVertices(), 6);
        assertEquals(g.getNumberOfEdges(), 9);
    }

    @Test
    public void test_GraphFromSequenceRandom() throws Exception {
                
        for (int i=0;i<10;i++)
        {
            int vertices = 5 * i;
            Graph g1 = Factory.buildRandomGraph(vertices, 0.1 * i);
            List<Integer> degreesList = g1.getDegrees();
            Graph g2 = GraphFromDegreeSequence.fromDegreeSequence(degreesList);

            assertEquals(g1.getNumberOfVertices(), g2.getNumberOfVertices());
            assertEquals(g1.getNumberOfEdges(), g2.getNumberOfEdges());
            List<Integer> d1 = g1.getDegrees();
            List<Integer> d2 = g2.getDegrees();
            Collections.sort(d1);
            Collections.sort(d2);

            assertEquals(d1,d2);
        }
    }
}
