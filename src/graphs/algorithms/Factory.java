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

    public static Graph buildEmptyGraph(int vertices) {
        Graph graph = new Graph("Empty (" + vertices + ")");
        try {
            for (int i = 1; i <= vertices; i++) {
                graph.addVertex();
            }
        } catch (Exception ex) {
            Logger.getLogger(Factory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return graph;
    }

    public static Graph buildCompleteGraph(int vertices) {
        Graph graph = buildEmptyGraph(vertices);
        graph.setName("K" + vertices);
        try {
            for (int i = 1; i <= vertices; i++) {
                for (int j = i + 1; j <= vertices; j++) {
                    graph.addEdge(graph.getVertex(i), graph.getVertex(j));
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return graph;
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
            return null;
        }
        return graph;
    }

    public static Graph buildCycleGraph(int vertices) {
        Graph graph = buildLineGraph(vertices);
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

    public static Graph buildLineGraph(int vertices) {
        Graph graph = buildEmptyGraph(vertices);
        graph.setName("L" + vertices);
        try {
            for (int i = 1; i < vertices; i++) {
                graph.addEdge(graph.getVertex(i), graph.getVertex(i+1));
            }
        } catch (Exception ex) {
            return null;
        }
        return graph;
    }

    public static Graph buildCompleteBiPartiteGraph(int leftVertices, int rightVertices) {
        Graph graph = buildEmptyGraph(leftVertices + rightVertices);
        graph.setName("K" + leftVertices + "," + rightVertices);
        try {
            for (int i = 1; i <= leftVertices; i++) {
                for (int j = 1; j <= rightVertices; j++) {
                    graph.addEdge(graph.getVertex(i), graph.getVertex(rightVertices + j));
                }

            }
        } catch (Exception ex) {
            return null;
        }
        return graph;
    }

    public static Graph buildRandomBiPartiteGraph(int leftVertices, int rightVertices, double density) {
        Graph graph = buildEmptyGraph(leftVertices + rightVertices);
        graph.setName("K" + leftVertices + "," + rightVertices);
        Random rand = new Random();    
        try {
            for (int i = 1; i <= leftVertices; i++) {
                for (int j = 1; j <= rightVertices; j++) {
                    if (rand.nextDouble() < density) {
                        graph.addEdge(graph.getVertex(i), graph.getVertex(rightVertices + j));
                    }
                }

            }
        } catch (Exception ex) {
            return null;
        }
        return graph;
    }
    
    public static Graph copyVerticesFrom(Graph other) {
        Graph result = new Graph("Copy of " + other.getName());
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
        try {
            for (Vertex v : result.getVertices()) {
                for (Vertex u : result.getVertices()) {
                    if (v.equals(u)) {
                        continue;
                    }
                    if ((!graph.hasEdge(v, u)) && (!result.hasEdge(u, v))) {
                        result.addEdge(v.getName(), u.getName());
                    }
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(Factory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }
}
