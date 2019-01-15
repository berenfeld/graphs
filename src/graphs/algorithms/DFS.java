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
public class DFS {

    public static final String DFS_DISCOVERY_TIME = "dfs-discovery-time";
    public static final String DFS_FINISH_TIME = "dfs-finish-time";
    public static final String DFS_VERTICES_VISIT_LIST = "dfs-vertices-visit-list";

    public static final int DFS_COLOR_NOT_VISITED = Utils.getColorNumber(Color.WHITE);
    public static final int DFS_COLOR_VISITING = Utils.getColorNumber(Color.GRAY);
    public static final int DFS_COLOR_VISITED = Utils.getColorNumber(Color.BLACK);
    
    public static final int DFS_COLOR_NOT_TRAVERESED = Utils.getColorNumber(Color.WHITE);
    public static final int DFS_COLOR_TRAVERESED = Utils.getColorNumber(Color.BLACK);
    
    private static class DFSContext {

        public int timer;
        public ArrayList<Vertex> visited = new ArrayList<>();
        public boolean copy;
        public Graph result;
    }

    private static void dfsVisit(Vertex v, DFSContext context) throws Exception {
        v.setColor(DFS_COLOR_VISITING);
        context.timer++;

        v.setAttribute(DFS_DISCOVERY_TIME, context.timer);

        Utils.debug("Visiting vertex " + v + " time " + context.timer);
        for (Edge adjacentEdge : v.getOutgoingEdges()) {

            Vertex adjacent = adjacentEdge.getOtherVertex(v);
            if (adjacent.getColor() == DFS_COLOR_NOT_VISITED) {
                adjacent.setColor(DFS_COLOR_VISITING);
                adjacentEdge.setColor(DFS_COLOR_TRAVERESED);
                dfsVisit(adjacent, context);
            }
        }
        v.setColor(DFS_COLOR_VISITED);
        context.timer++;
        v.setAttribute(DFS_FINISH_TIME, context.timer);
        context.visited.add(v);
        Utils.debug("Finished vertex " + v + " time " + context.timer);
    }

    public static Graph dfs(Graph graph, Vertex initial, boolean copy, boolean forest) {
        Graph resultGraph = graph;
        if (copy) {
            resultGraph = Factory.copyOf(graph);
            resultGraph.setName("DFS on " + graph.getName());
        } 
        
        try {
            for (Vertex vertex : resultGraph.getVertices()) {
                vertex.setColor(DFS_COLOR_NOT_VISITED);
            }
            for (Edge edge : resultGraph.getEdges()) {
                edge.setColor(DFS_COLOR_NOT_TRAVERESED);
            }

            DFSContext context = new DFSContext();
            context.timer = 0;
            context.copy = copy;
            context.result = resultGraph;

            Vertex start = resultGraph.getRandomVertex();
            if (initial != null) {
                start = resultGraph.getVertex(initial.getName());
            }
            dfsVisit(start, context);

            if (forest) {
                for (Vertex v : resultGraph.getVertices()) {
                    if (v.getColor() == DFS_COLOR_NOT_VISITED) {
                        dfsVisit(v, context);
                    }
                }
            }
            resultGraph.setAttribute(DFS_VERTICES_VISIT_LIST, context.visited);
        } catch (Exception ex) {
            Logger.getLogger(Factory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return resultGraph;
    }

    public static Graph dfs(Graph g) {
        return DFS.dfs(g, g.getRandomVertex(), true, true);
    }

    public static Graph dfs(Graph g, boolean copy) {
        return DFS.dfs(g, g.getRandomVertex(), copy, true);
    }

    public static Graph dfs(Graph g, Vertex v) {
        return DFS.dfs(g, v, true, true);
    }

    public static Graph dfs(Graph g, Vertex v, boolean copy) {
        return DFS.dfs(g, v, copy, true);
    }
}
