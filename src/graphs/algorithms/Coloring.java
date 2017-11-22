/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.Graph;
import graphs.core.Vertex;

/**
 *
 * @author me
 */
public class Coloring {
    
    public static void colorGraph_Greedy(Graph g) {
        
        
        for(Vertex v : g.getVertices()) {
            v.setColor(0);
        }
        
        for(Vertex v : g.getVertices()) {
            int color = 1;
            for (Vertex neighbor : v.getNeighbors()) {
                if (neighbor.getColor() == color) {
                    color ++;
                }
            }
            v.setColor(color);
        }
    }
}
