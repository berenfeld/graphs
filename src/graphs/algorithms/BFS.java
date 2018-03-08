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
import graphs.utils.Utils;
import java.awt.Color;
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

    public static final String BFS_INITIAL_VERTEX = "_BFS-Initial-Vertex";
    public static final String BFS_MAXIMUM_DEPTH = "_BFS-Maximum-Depth";
    public static final String BFS_VERTEX_DEPTH = "d";
    public static final String BFS_PATH_FROM_ROOT = "_BFS-Path-From-Root";
    public static final String BFS_PREDECESSOR = "p";

    public static final int BFS_COLOR_NOT_VISITED = Utils.getColorNumber(Color.WHITE);
    public static final int BFS_COLOR_VISITING = Utils.getColorNumber(Color.GRAY);
    public static final int BFS_COLOR_VISITED = Utils.getColorNumber(Color.BLACK);

    public static final int EDGE_COLOR_NOT_TRAVERESED = Utils.getColorNumber(Color.WHITE);
    public static final int EDGE_COLOR_TRAVERESED = Utils.getColorNumber(Color.BLACK);

    public static Graph bfs(Graph g, Vertex initial) {
        Graph bfsGraph = Factory.copyOf(g);
        try {
            for (Vertex vertex : g.getVertices()) {
                vertex.setColor(BFS_COLOR_NOT_VISITED);

            }
            for (Edge edge : g.getEdges()) {
                edge.setColor(EDGE_COLOR_NOT_TRAVERESED);
            }

            Vertex start = g.getVertex(initial.getName());
            start.setAttribute(BFS_VERTEX_DEPTH, 0);

            g.setAttribute(BFS_INITIAL_VERTEX, start.getName());

            start.setColor(BFS_COLOR_VISITING);
            Path path = new Path(g);
            start.setAttribute(BFS_PATH_FROM_ROOT, path);
            start.setAttribute(BFS_PREDECESSOR, null);
            Queue<Vertex> queue = new LinkedList<>();
            queue.add(start);
            int maxDepth = 0;

            while (!queue.isEmpty()) {

                Vertex current = queue.poll();

                int bfsDepth = (int) current.getAttribute(BFS_VERTEX_DEPTH);

                maxDepth = Math.max(maxDepth, bfsDepth);

                for (Edge adjacentEdge : current.getAdjacentEdges()) {
                    Vertex adjacent = adjacentEdge.getOtherVertex(current);

                    if (adjacent.getColor() == BFS_COLOR_NOT_VISITED) {

                        path = new Path((Path) current.getAttribute(BFS_PATH_FROM_ROOT));
                        path.add(adjacentEdge);

                        adjacent.setAttribute(BFS_PATH_FROM_ROOT, path);
                        adjacent.setAttribute(BFS_PREDECESSOR, current.getName());
                        adjacent.setColor(BFS_COLOR_VISITING);
                        adjacent.setAttribute(BFS_VERTEX_DEPTH, bfsDepth + 1);
                        adjacentEdge.setColor(EDGE_COLOR_TRAVERESED);
                        queue.add(adjacent);
                    }
                }

                current.setColor(BFS_COLOR_VISITED);
            }

            bfsGraph.setName("BFS on " + g.getName());

            for (Vertex vertex : g.getVertices()) {
                if (vertex.getColor() != BFS_COLOR_VISITED) {
                    bfsGraph.removeVertex(vertex);
                }
            }

            for (Edge edge : g.getEdges()) {
                if ((bfsGraph.hasEdge(edge.getName())) && (edge.getColor() != EDGE_COLOR_TRAVERESED)) {
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
