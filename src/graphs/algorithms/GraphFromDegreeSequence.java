/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.Graph;
import graphs.core.Vertex;
import graphs.utils.Utils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ranb
 */
public class GraphFromDegreeSequence {

    public static Graph fromDegreeSequence(List<Integer> degreeSequence) throws Exception {
        int vertices = degreeSequence.size();
        Graph graph = Factory.buildEmptyGraph(vertices);
        graph.setName("From Degree Sequence : " + degreeSequence);
        Collections.sort(degreeSequence);
        Collections.reverse(degreeSequence);

        
        for (int vertexIndex = 1; vertexIndex <= vertices; vertexIndex ++) {
            int currentVertexDegree = degreeSequence.get(vertexIndex-1);
            if (currentVertexDegree < 0) {
                throw new Exception("Can't create graph from degree sequence " + degreeSequence);
            }

            if (currentVertexDegree == 0) {
                continue;
            }
            Vertex v = graph.getVertex(vertexIndex);

            while(currentVertexDegree > 0) {
                int otherVertexIndex = vertices;
                while (otherVertexIndex > 0) {
                    int adjacentDegree = degreeSequence.get(otherVertexIndex - 1);
                    if ( adjacentDegree > 0 ) {
                        adjacentDegree--;                        
                        degreeSequence.set(otherVertexIndex - 1, adjacentDegree);
                        currentVertexDegree --;
                        Vertex otherVertex = graph.getVertex(otherVertexIndex);
                        graph.addEdge(v, otherVertex );
                        Utils.info("added edge " + v + "-" + otherVertex);
                    }
                    otherVertexIndex--;
                    if ( currentVertexDegree == 0 ) {
                        break;
                    }
                }
                if ( currentVertexDegree > 0 ) {
                    throw new Exception("Can't create graph from degree sequence " + degreeSequence);                    
                } 
            }
            degreeSequence.set(vertexIndex-1, 0);
            Utils.info("degrees list " + degreeSequence);
        } 

        return graph;
    }
}
