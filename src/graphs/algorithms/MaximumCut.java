/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.Edge;
import graphs.core.Graph;
import graphs.core.Vertex;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ranb
 */
public class MaximumCut {

    public static final String MAXIMUM_CUT_SIDE_A = "_MAXIMUM_CUT_SIDE_A";
    public static final String MAXIMUM_CUT_SIDE_B = "_MAXIMUM_CUT_SIDE_B";

    public static Graph maximumCut(Graph g) {

        List<Vertex> sideA = new ArrayList();
        List<Vertex> sideB = new ArrayList();

        int i = 0;
        for (Vertex v : g.getVertices()) {
            if ((i % 2) == 0) {
                sideA.add(v);
            } else {
                sideB.add(v);
            }
            i++;
        }

        while (true) {
            boolean foundGreaterCut = false;

            for (Vertex v : sideA) {
                int adjacentsInSameSize = 0;
                int adjacentsInOtherSide = 0;
                for (Vertex adjacent : v.getNeighbors()) {
                    if (sideA.contains(adjacent)) {
                        adjacentsInSameSize++;
                    } else {
                        adjacentsInOtherSide++;
                    }
                }
                if ( adjacentsInOtherSide < adjacentsInSameSize) {
                    sideA.remove(v);
                    sideB.add(v);
                    foundGreaterCut = true; 
                    break;
                }
            }
            if ( foundGreaterCut ) {
                continue;
            }
            
            for (Vertex v : sideB) {
                int adjacentsInSameSize = 0;
                int adjacentsInOtherSide = 0;
                for (Vertex adjacent : v.getNeighbors()) {
                    if (sideB.contains(adjacent)) {
                        adjacentsInSameSize++;
                    } else {
                        adjacentsInOtherSide++;
                    }
                }
                if ( adjacentsInOtherSide < adjacentsInSameSize) {
                    sideB.remove(v);
                    sideA.add(v);
                    foundGreaterCut = true; 
                    break;
                }
            }
            if ( ! foundGreaterCut ) {
                break;
            }
        }
       
        g.setAttribute(MAXIMUM_CUT_SIDE_A, sideA);
        g.setAttribute(MAXIMUM_CUT_SIDE_B, sideB);
        return g;        
    }
}
