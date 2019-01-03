/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.*;
import java.util.*;

/**
 *
 * @author me
 */
public class Prim {
    
    private static class EdgeWeightComparator implements Comparator<Edge> 
    {
        public int compare(Edge e, Edge f)
        {
            if ( f.getWeight() != e.getWeight()) {
                return e.getWeight() - f.getWeight();
            } else {
                return e.getName().compareTo(f.getName());
            }
        }
    }
    
    public static Graph minimumWeightSpanningTree(Graph g, Vertex start) throws Exception
    {
        Graph result = Factory.copyVerticesFrom(g);                      
        result.setName("MST(Prim) of " + g.getName());
        Set<Vertex> matched = new TreeSet<>();        
        matched.add(start);
                
        Set<Edge> edgesToConsider = new TreeSet<Edge>(new EdgeWeightComparator() );
        edgesToConsider.addAll(start.getOutgoingEdges());
               
        while (! edgesToConsider.isEmpty()) {
            Edge edgeToAdd = edgesToConsider.iterator().next();
            
            Edge addedEdge = result.addEdge(edgeToAdd);
            addedEdge.setWeight(edgeToAdd.getWeight());            
            
            Vertex added = null;
            for ( Vertex v : matched ) {
                if ( edgeToAdd.incidentIn(v)) {
                    added = edgeToAdd.getOtherVertex(v);
                    break;
                }
            }
            
            matched.add(added);            
            
            edgesToConsider.addAll(added.getOutgoingEdges());
            
            Set<Edge> edgesToRemove = new TreeSet<>();
            for (Edge edge : edgesToConsider) {
                if (    (matched.contains(edge.getFromVertex())) &&
                        (matched.contains(edge.getToVertex())) ) {
                    edgesToRemove.add(edge);                    
                }
            }
            edgesToConsider.removeAll(edgesToRemove);
            
        }
        
        
        return result;
        
    }
    
    public static Graph minimumWeightSpanningTree(Graph g)      throws Exception       
    {
        return minimumWeightSpanningTree(g, g.getRandomVertex());
    }
}
