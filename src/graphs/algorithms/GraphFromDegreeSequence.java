/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.Graph;
import graphs.core.Vertex;
import graphs.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ranb
 */
public class GraphFromDegreeSequence {

    public static class VertexInfo implements Comparable
    {
        public int degree;
        public Vertex vertex;

        @Override
        public int compareTo(Object o) {
            return degree - ((VertexInfo)o).degree;
        }

        @Override
        public String toString() {
            return String.valueOf(degree);
        }
                 
    }
    
    public static Graph fromDegreeSequence(List<Integer> degreeSequence) throws Exception {
        int vertices = degreeSequence.size();
        Graph graph = Factory.buildEmptyGraph(vertices);
        graph.setName("From Degree Sequence : " + degreeSequence);
        Utils.info("Create graph from sequence " + degreeSequence);
           
        List<VertexInfo> verticesInfo = new ArrayList<>();
        for (int i=0;i<vertices;i++) {
            VertexInfo vertexInfo = new VertexInfo();
            vertexInfo.vertex = graph.getVertex(i+1);
            vertexInfo.degree = degreeSequence.get(i);
            verticesInfo.add(0,vertexInfo);
        }
        
        while ( ! verticesInfo.isEmpty()) {
            Collections.sort(verticesInfo);
            Collections.reverse(verticesInfo);
            
            Utils.debug("degrees list " + verticesInfo);
            
            VertexInfo vertexInfo = verticesInfo.remove(0);
            
            int currentVertexDegree = vertexInfo.degree;
            if (currentVertexDegree < 0) {
                throw new Exception("Can't create graph from degree sequence " + degreeSequence);
            }
            
            if (currentVertexDegree > verticesInfo.size() ) {
                throw new Exception("Can't create graph from degree sequence " + degreeSequence);
            }
            for (int i = 0; i < currentVertexDegree; i++) {
                VertexInfo otherVertexInfo = verticesInfo.get(i);
                otherVertexInfo.degree --;
                graph.addEdge(vertexInfo.vertex, otherVertexInfo.vertex);
                Utils.debug("added edge " + vertexInfo.vertex + "-" + otherVertexInfo.vertex);
            }   
            vertexInfo.degree = 0;
        } 

        return graph;
    }
}
