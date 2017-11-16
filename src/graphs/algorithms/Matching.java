/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.Edge;
import graphs.core.Graph;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ranb
 */
public class Matching {
    
    public static Graph maximalMatching(Graph g) throws Exception {
        Graph result = Factory.copyOf(g);
        
        List<Edge> matchedEdges = new LinkedList<>();
        while (result.getNumberOfEdges() > 0 ) {
            Edge matched = result.getRandomEdge();            
            result.removeVertex(matched.getFromVertex());
            result.removeVertex(matched.getToVertex());
            matchedEdges.add(matched);
        }
        
        result = Factory.copyVerticesFrom(g);
        for (Edge matched : matchedEdges) {
            result.addEdge(matched);
        }
        return result;
    }

}
