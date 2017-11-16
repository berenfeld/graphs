/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.Edge;
import graphs.core.Graph;
import graphs.core.Path;
import graphs.core.Vertex;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author me
 */
public class BFS {

    public static Graph buildForSequence(int[] gradesSequence) throws Exception {
        return null;
    }

    public static final String BFS_INITIAL_VERTEX = "BFS-Initial-Vertex";
    public static final String BFS_MAXIMUM_DEPTH = "BFS-Maximum-Depth";
    public static final String BFS_VERTEX_DEPTH = "BFS-Vertex-Depth";
    public static final String BFS_PATH_FROM_ROOT = "BFS-Path-From-Root";
    
    public static Graph bfs(Graph g, Vertex initial) {
        Graph bfsGraph = Factory.copyOf(g);
        try {
            bfsGraph.setName("BFS on " + g.getName());

            for (Vertex vertex : bfsGraph.getVertices() ) {
                vertex.setColor(Vertex.VERTEX_COLOR_BLACK);

            }
            for (Edge edge : bfsGraph.getEdges() ) {
                edge.setColor(Edge.EDGE_COLOR_BLACK);
            }

            Vertex start = bfsGraph.getVertex(initial.getName());
            start.setAttribute(BFS_VERTEX_DEPTH, 0);

            bfsGraph.setAttribute(BFS_INITIAL_VERTEX, start.getName());

            start.setColor(Vertex.VERTEX_COLOR_GRAY);
            Path path = new Path(bfsGraph);            
            start.setAttribute(BFS_PATH_FROM_ROOT, path);

            Queue<Vertex> queue = new LinkedList<>();
            queue.add(start);
            int maxDepth = 0;
            
            while (!queue.isEmpty()) {

                Vertex current = queue.poll();
                current.setColor(Vertex.VERTEX_COLOR_WHITE);

                int bfsDepth = (int) current.getAttribute(BFS_VERTEX_DEPTH);

                maxDepth = Math.max(maxDepth, bfsDepth);

                for (Edge adjacentEdge : current.getAdjacentEdges()) {
                    Vertex adjacent = adjacentEdge.getOtherVertex(current);
                                         
                    if (adjacent.getColor() == Vertex.VERTEX_COLOR_BLACK) {      
                    
                        path = new Path((Path)current.getAttribute(BFS_PATH_FROM_ROOT));                    
                        path.add(adjacentEdge);
                        
                        adjacent.setAttribute(BFS_PATH_FROM_ROOT, path);
                        adjacent.setColor(Vertex.VERTEX_COLOR_GRAY);
                        adjacent.setAttribute(BFS_VERTEX_DEPTH, bfsDepth + 1);
                        adjacentEdge.setColor(Edge.EDGE_COLOR_WHITE);
                        queue.add(adjacent);
                    } 
                }
            }

            for (Vertex vertex :bfsGraph.getVertices() ) {
                if (vertex.getColor() != Vertex.VERTEX_COLOR_WHITE) {
                    bfsGraph.removeVertex(vertex);
                }
            }

            for (Edge edge : bfsGraph.getEdges()) {
                if (edge.getColor() != Edge.EDGE_COLOR_WHITE) {
                    bfsGraph.removeEdge(edge.getFromVertex().getName(), edge.getToVertex().getName());
                }
            }

            start.setAttribute(BFS_MAXIMUM_DEPTH, maxDepth);
        } catch (Exception ex) {
            Logger.getLogger(Factory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return bfsGraph;
    }

    public static Graph bfs(Graph g) {
        return BFS.bfs(g, g.getRandomVertex());
    }
}
