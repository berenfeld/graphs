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

    public static final class Configuration {

        public boolean putResultsInNewGraph;
        public Vertex initialVertex;
        public boolean returnBFSTree;
        public boolean createBFSForest;
    }

    public static final String BFS_INITIAL_VERTEX = "bfs-initial-vertex";
    public static final String BFS_MAXIMUM_DEPTH = "bfs-maximum-depth";
    public static final String BFS_VERTEX_DEPTH = "bfs-depth";
    public static final String BFS_VERTEX_PATH_FROM_ROOT = "bfs-path";
    public static final String BFS_PREDECESSOR = "bfs-predecesor";
    public static final String BFS_MAXIMUM_DEPTH_VERTEX = "bfs-maximum-depth-vertex";
    public static final String BFS_VERTEX_NUMBER_IN_LEVEL = "bfs-vertex-number-in-level";
    public static final String BFS_NUMBER_OF_VERTICES_IN_EACH_LEVEL = "bfs-number-of-vertices-in-each-level";

    public static final int BFS_COLOR_NOT_VISITED = Utils.getColorNumber(Color.WHITE);
    public static final int BFS_COLOR_VISITING = Utils.getColorNumber(Color.GRAY);
    public static final int BFS_COLOR_VISITED = Utils.getColorNumber(Color.BLACK);

    public static final int BFS_EDGE_COLOR_NOT_TRAVERESED = Utils.getColorNumber(Color.WHITE);
    public static final int BFS_EDGE_COLOR_TRAVERESED = Utils.getColorNumber(Color.BLACK);

    public static Graph bfs(Graph g, Configuration configuration) {
        Vertex initial = null;
        if (configuration.initialVertex == null) {
            initial = g.getRandomVertex();
        } else {
            initial = g.getVertex(configuration.initialVertex.getName());
        }

        Graph resultGraph = g;
        if (configuration.putResultsInNewGraph) {
            Graph bfsGraph = Factory.copyOf(g, "BFS on " + g.getName());
            resultGraph = bfsGraph;
        }

        try {
            for (Vertex vertex : resultGraph.getVertices()) {
                vertex.setColor(BFS_COLOR_NOT_VISITED);

            }
            for (Edge edge : resultGraph.getEdges()) {
                edge.setColor(BFS_EDGE_COLOR_NOT_TRAVERESED);
            }

            Vertex start = resultGraph.getVertex(initial.getName());
            start.setAttribute(BFS_VERTEX_DEPTH, 0);

            Set<String> unvisitedVertices = new HashSet(g.getVerticesNames());
            resultGraph.setAttribute(BFS_INITIAL_VERTEX, start.getName());
            Map<Integer, Integer> verticesPerLevel = new HashMap<>();
            verticesPerLevel.put(0, 1);

            start.setColor(BFS_COLOR_VISITING);
            Path path = new Path(resultGraph);
            start.setAttribute(BFS_VERTEX_PATH_FROM_ROOT, path);
            start.setAttribute(BFS_PREDECESSOR, null);
            start.setAttribute(BFS_VERTEX_NUMBER_IN_LEVEL, 1);
            Queue<Vertex> queue = new LinkedList<>();
            queue.add(start);
            int maxDepth = 0;

            while (!queue.isEmpty()) {

                Vertex current = queue.poll();
                unvisitedVertices.remove(current.getName());
                int bfsDepth = (int) current.getAttribute(BFS_VERTEX_DEPTH);

                if (bfsDepth > maxDepth) {
                    maxDepth = bfsDepth;
                    resultGraph.setAttribute(BFS_MAXIMUM_DEPTH_VERTEX, current);
                }

                for (Edge adjacentEdge : current.getOutgoingEdges()) {
                    Vertex adjacent = adjacentEdge.getOtherVertex(current);

                    if (adjacent.getColor() == BFS_COLOR_NOT_VISITED) {

                        path = new Path((Path) current.getAttribute(BFS_VERTEX_PATH_FROM_ROOT));
                        path.add(adjacentEdge);

                        adjacent.setAttribute(BFS_VERTEX_PATH_FROM_ROOT, path);
                        adjacent.setAttribute(BFS_PREDECESSOR, current.getName());
                        int adjacentDepth = bfsDepth + 1;
                        Integer verticesInLevel = verticesPerLevel.get(bfsDepth + 1);
                        int vertexIndexInLevel = 1;
                        if (verticesInLevel == null) {
                            verticesPerLevel.put(adjacentDepth, vertexIndexInLevel);
                        } else {
                            verticesInLevel++;
                            vertexIndexInLevel = verticesInLevel;
                            verticesPerLevel.put(adjacentDepth, verticesInLevel);
                        }
                        adjacent.setAttribute(BFS_VERTEX_NUMBER_IN_LEVEL, vertexIndexInLevel);
                        adjacent.setColor(BFS_COLOR_VISITING);

                        adjacent.setAttribute(BFS_VERTEX_DEPTH, adjacentDepth);
                        adjacentEdge.setColor(BFS_EDGE_COLOR_TRAVERESED);
                        queue.add(adjacent);

                    }
                }

                current.setColor(BFS_COLOR_VISITED);
                if (queue.isEmpty()) {
                    if (!unvisitedVertices.isEmpty()) {
                        if (configuration.createBFSForest) {
                            Vertex v = resultGraph.getVertex(unvisitedVertices.iterator().next());
                            path = new Path(resultGraph);
                            v.setAttribute(BFS_VERTEX_PATH_FROM_ROOT, path);
                            v.setAttribute(BFS_PREDECESSOR, null);
                            Integer verticesInLevel0 = verticesPerLevel.get(0);                            
                            v.setAttribute(BFS_VERTEX_NUMBER_IN_LEVEL, verticesInLevel0 + 1);
                            verticesPerLevel.put(0, verticesInLevel0 + 1);
                            v.setAttribute(BFS_VERTEX_DEPTH, 0);
                            v.setColor(BFS_COLOR_VISITING);
                            queue.add(v);
                        }
                    }
                }
            }

            if (configuration.returnBFSTree) {

                for (Vertex vertex : resultGraph.getVertices()) {
                    if (vertex.getColor() != BFS_COLOR_VISITED) {
                        resultGraph.removeVertex(vertex);
                    }
                }

                for (Edge edge : resultGraph.getEdges()) {
                    if ((resultGraph.hasEdge(edge.getName())) && (edge.getColor() != BFS_EDGE_COLOR_TRAVERESED)) {
                        resultGraph.removeEdge(edge.getFromVertex().getName(), edge.getToVertex().getName());
                    }
                }
            }

            resultGraph.setAttribute(BFS_MAXIMUM_DEPTH, maxDepth);
            resultGraph.setAttribute(BFS_NUMBER_OF_VERTICES_IN_EACH_LEVEL, verticesPerLevel);

        } catch (Exception ex) {
            Logger.getLogger(Factory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return resultGraph;
    }

    public static Graph bfs(Graph g) {
        return BFS.bfs(g, new Configuration());
    }

    public static Graph bfs(Graph g, Vertex initialVertex) {
        BFS.Configuration configuration = new BFS.Configuration();
        configuration.initialVertex = initialVertex;
        return BFS.bfs(g, new Configuration());
    }
}
