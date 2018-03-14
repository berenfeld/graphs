/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.Graph;
import graphs.core.Vertex;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ranb
 */
public class GraphFromDegreeSequence {
    
    public static Graph fromDegreeSequence(List<Integer> degreeSequence) throws Exception
    {
        int vertices = degreeSequence.size();
        Graph graph = Factory.buildEmptyGraph(vertices);
        graph.setName("From sequence : "+ degreeSequence);
        Collections.sort(degreeSequence);
        Collections.reverse(degreeSequence);
        
        int vertexIndex = 1;
        do {
            int currentVertexDegree = degreeSequence.remove(0);
            
            Vertex v = graph.getVertex(vertexIndex);
           
            for (int i=0;i<currentVertexDegree;i++) {
                graph.addEdge(v, graph.getVertex(vertexIndex + i + 1));
                int adjacentDegree = degreeSequence.get(i);
                adjacentDegree --;
                if ( adjacentDegree < 0 ) {
                    throw new Exception("Illegal degree sequence");
                }
                degreeSequence.set(i,adjacentDegree);
            }
             vertexIndex++;
        } while( ! degreeSequence.isEmpty());
        
        return graph;
    }
}
