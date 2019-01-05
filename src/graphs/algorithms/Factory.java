/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.algorithms;

import graphs.core.Edge;
import graphs.core.Graph;
import graphs.core.Vertex;
import graphs.utils.Utils;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author me
 */
public class Factory {

    public static Graph buildEmptyGraph(int vertices, boolean directed) {
        Graph graph = new Graph("Empty (" + vertices + ")", directed);
        try {
            for (int i = 1; i <= vertices; i++) {
                graph.addVertex();
            }
        } catch (Exception ex) {
            Logger.getLogger(Factory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return graph;
    }
    
    public static Graph buildEmptyGraph(int vertices ) {
        return buildEmptyGraph(vertices, false);
    }

    public static Graph buildCompleteGraph(int vertices, boolean directed) {
        Graph graph = buildEmptyGraph(vertices, directed);
        graph.setName("K" + vertices);
        try {
            for (int i = 1; i <= vertices; i++) {
                for (int j = i + 1; j <= vertices; j++) {
                    graph.addEdge(graph.getVertex(i), graph.getVertex(j));
                    if (directed) {
                        graph.addEdge(graph.getVertex(j), graph.getVertex(i));
                    }
                }
            }
        } catch (Exception ex) {
            Utils.exception(ex);
            return null;
        }
        return graph;
    }

    public static Graph buildCompleteGraph(int vertices) {
        return buildCompleteGraph(vertices,false);
    }
    
    public static Graph buildRandomGraph(int vertices, double density) {
        Graph graph = buildEmptyGraph(vertices);
        graph.setName("Random (" + vertices + ")");
        Random rand = new Random();
        try {
            for (int i = 1; i <= vertices; i++) {
                for (int j = i + 1; j <= vertices; j++) {
                    if (rand.nextDouble() < density) {
                        graph.addEdge(graph.getVertex(i), graph.getVertex(j));
                    }
                }
            }
        } catch (Exception ex) {
            Utils.exception(ex);
            return null;
        }
        return graph;
    }
    
    public static Graph buildCycleGraph(int vertices, boolean directed) {
        Graph graph = buildLineGraph(vertices, directed);
        graph.setName("C" + vertices);
        try {
            if (vertices > 2) {
                graph.addEdge(graph.getVertex(vertices), graph.getVertex(1));
            }
        } catch (Exception ex) {
            return null;
        }
        return graph;
    }

    public static Graph buildCycleGraph(int vertices) {
        return buildCycleGraph(vertices, false);
    }
    
    public static Graph buildLineGraph(int vertices, boolean directed) {
        Graph graph = buildEmptyGraph(vertices, directed);
        graph.setName("L" + vertices);
        try {
            for (int i = 1; i < vertices; i++) {
                graph.addEdge(graph.getVertex(i), graph.getVertex(i + 1));
            }
        } catch (Exception ex) {
            return null;
        }
        return graph;
    }

    public static Graph buildLineGraph(int vertices) {
        return buildLineGraph(vertices, false);
    }
    
    public static Graph buildCompleteBiPartiteGraph(int leftVertices, int rightVertices) {
        return buildRandomBiPartiteGraph(leftVertices, rightVertices, 1.0);
    }

    public static Graph buildRandomBiPartiteGraph(int leftVertices, int rightVertices, double density) {
        int vertices = leftVertices + rightVertices;
        Graph graph = buildEmptyGraph(leftVertices + rightVertices);
        graph.setName("K" + leftVertices + "," + rightVertices);
        Random rand = new Random();
        try {
            for (int i = 1; i <= leftVertices; i++) {
                graph.getVertex(i).setAttribute(Vertex.VERTEX_ATTRIBUTE_SIDE, 1);
            }
            for (int i = leftVertices + 1; i <= vertices; i++) {
                graph.getVertex(i).setAttribute(Vertex.VERTEX_ATTRIBUTE_SIDE, 2);
            }
            for (int i = 1; i <= leftVertices; i++) {
                for (int j = 1; j <= rightVertices; j++) {
                    if (rand.nextDouble() < density) {
                        graph.addEdge(graph.getVertex(i), graph.getVertex(leftVertices + j));
                    }
                }

            }
        } catch (Exception ex) {
            Utils.exception(ex);
            return null;
        }
        return graph;
    }

    public static Graph copyVerticesFrom(Graph other) {
        Graph result = new Graph("Copy of " + other.getName(), other.isDirected());
        try {
            for (Vertex v : other.getVertices()) {
                Vertex added = result.addVertex(v.getName());
                added.copyAttributesTo(v);
            }
        } catch (Exception ex) {
            return null;
        }
        return result;
    }

    public static Graph copyOf(Graph other) {
        Graph result = copyVerticesFrom(other);
        try {

            for (Edge edge : other.getEdges()) {
                Edge newEdge = result.addEdge(edge.getFromVertex().getName(), edge.getToVertex().getName());
                newEdge.copyAttributesTo(edge);
            }
        } catch (Exception ex) {
            return null;
        }
        return result;
    }

    public static Graph complementOf(Graph graph) {
        Graph result = copyVerticesFrom(graph);
        result.setName("Complement of " + graph.getName());
        try {
            for (Vertex v : result.getVertices()) {
                for (Vertex u : result.getVertices()) {
                    if (v.equals(u)) {
                        continue;
                    }
                    if (( ! graph.hasEdge(v, u)) && (! result.hasEdge(v,u))) {
                        result.addEdge(v.getName(), u.getName());
                    }
                }

            }
        } catch (Exception ex) {
            Utils.exception(ex);
            return null;
        }
        return result;
    }

    public static Graph transposeOf(Graph graph) {
        Graph result = copyVerticesFrom(graph);
        result.setName("Transpose of " + graph.getName());
        try {
            for (Vertex v : result.getVertices()) {
                for (Vertex u : result.getVertices()) {
                    if (v.equals(u)) {
                        continue;
                    }
                    if (graph.hasEdge(v, u)) {
                        result.addEdge(u.getName(), v.getName());
                    }
                }

            }
        } catch (Exception ex) {
            Utils.exception(ex);
            return null;
        }
        return result;
    }

}
