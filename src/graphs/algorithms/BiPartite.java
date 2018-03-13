/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.Graph;
import graphs.core.Vertex;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ranb
 */
public class BiPartite {
     public static final String BIPARTITE_SIDE_A = "_BIPARTITE_SIDE_A";
     public static final String BIPARTITE_SIDE_B = "_BIPARTITE_SIDE_B";
     
     public static Graph bipartite(Graph g) {
         Graph bfs = BFS.bfs(g, true);
         List<Vertex> sideA = new ArrayList();
         List<Vertex> sideB = new ArrayList();
         
         for (Vertex v : bfs.getVertices()) {
             int depth = (Integer)v.getAttribute(BFS.BFS_VERTEX_DEPTH);
             if ( (depth % 2) == 0 ) {
                 sideA.add(v);
             } else {
                 sideB.add(v);
             }
         }
         
         Graph biPartite = Factory.copyOf(g);
         biPartite.setAttribute(BIPARTITE_SIDE_A, sideA);
         biPartite.setAttribute(BIPARTITE_SIDE_B, sideB);
         return biPartite;
     }
}
