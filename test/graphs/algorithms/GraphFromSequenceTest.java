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


}
