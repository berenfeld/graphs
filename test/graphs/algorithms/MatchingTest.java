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
public class MatchingTest {

    public MatchingTest() {
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
    public void test_maximalMatching_completeGraph() throws Exception {

        Graph g = Factory.buildCompleteGraph(100);
        Graph matching = Matching.maximalMatching(g);
        assertEquals(100, matching.getNumberOfVertices());
        assertEquals(50, matching.getNumberOfEdges());
    }

    @Test
    public void test_maximalMatching_completeBipartiteGraph() throws Exception {

        Graph g = Factory.buildCompleteBiPartiteGraph(50, 50);
        Graph matching = Matching.maximalMatching(g);
        assertEquals(100, matching.getNumberOfVertices());
        assertEquals(50, matching.getNumberOfEdges());
    }
}
