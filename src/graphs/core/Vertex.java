/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.core;

import graphs.utils.Utils;
import java.util.*;

/**
 *
 * @author me
 */
public class Vertex extends BaseElement implements Comparable {

    public Vertex(Graph graph, String name) {
        _graph = graph;
        _name = name;
    }

    public static String VERTEX_ATTRIBUTE_COLOR = "Color";
    public static int VERTEX_COLOR_WHITE = 0;
    public static int VERTEX_COLOR_GRAY = 1;
    public static int VERTEX_COLOR_BLACK = 2;
    public static String VERTEX_ATTRIBUTE_WEIGHT = "Weight";
    
    private Graph _graph;
    private String _name;
    private Map<String, Vertex> _neighbors = new HashMap<String, Vertex>();
    private Map<String, Edge> _adjacentEdges = new HashMap<String, Edge>();

    
    public Graph getGraph() {
        return _graph;
    }

    public String getName() {
        return _name;
    }

    void connectTo(Vertex other, Edge byEdge) throws Exception {

        if (!byEdge.incidentIn(this)) {
            throw new Exception("Can't connect vertex '" + this + "' by edge '" + byEdge + "'");
        }
        if (!byEdge.incidentIn(other)) {
            throw new Exception("Can't connect vertex '" + this + "' by edge '" + byEdge + "'");
        }
        if (other.equals(this)) {
            throw new Exception("Can't connect vertex '" + this + "' to itself");
        }
        if (!_graph.equals(other._graph)) {
            throw new Exception("Can't connect vertex '" + this + "' vertex from another graph");
        }
        _neighbors.put(other.getName(), other);
        _adjacentEdges.put(other.getName(), byEdge);
    }

    void disconnectFrom(Vertex other) throws Exception {
        if (other.equals(this)) {
            throw new Exception("Can't disconnect vertex '" + this + "' to itself");
        }
        if (!_graph.equals(other._graph)) {
            throw new Exception("Can't disconnect vertex '" + this + "' vertex from another graph");
        }
        if (!_neighbors.containsKey(other.getName())) {
            throw new Exception("Can't disconnect vertex '" + this + "' rom non adjacent vertex '" + other + "'");
        }
        if (!_adjacentEdges.containsKey(other.getName())) {
            throw new Exception("Can't disconnect vertex '" + this + "' rom non adjacent vertex '" + other + "'");
        }
        _neighbors.remove(other.getName());
        _adjacentEdges.remove(other.getName());
    }

    public Set< Vertex> getNeighbors() {
        return new TreeSet<>( _neighbors.values());        
    }

    public Set<Edge> getAdjacentEdges() {
        return new TreeSet(_adjacentEdges.values());
    }
    
    public boolean connectedTo(String to) {
        return _neighbors.containsKey(to);
    }

    public boolean connectedTo(Vertex to) {
        return _neighbors.containsKey(to.getName());
    }
    public int grade() {
        return _neighbors.size();
    }

    public int getColor() {
        return (int) getAttribute(VERTEX_ATTRIBUTE_COLOR);
    }

    public void setColor(int color) {
        setAttribute(VERTEX_ATTRIBUTE_COLOR, color);
    }

    public int getWeight() {
        return (int) getAttribute(VERTEX_ATTRIBUTE_WEIGHT);
    }

    public void setWeight(int color) {
        setAttribute(VERTEX_ATTRIBUTE_WEIGHT, color);
    }
    
    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return 1;
        }
        Vertex other = (Vertex) o;
        return _name.compareTo(other._name);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (_name.equals(((Vertex) obj)._name));
    }

    @Override
    public String toString() {
        return _name;
    }

}
