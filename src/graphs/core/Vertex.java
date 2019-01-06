/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.core;

import graphs.gui.MessageListener;
import graphs.utils.Utils;
import java.awt.Color;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author me
 */
public class Vertex extends BaseElement implements Comparable, Serializable {

    public Vertex(Graph graph, String name, int index) {
        super();
        _graph = graph;
        _index = index;
        _name = name;
        setAttribute(VERTEX_ATTRIBUTE_NAME, name);
        setAttribute(VERTEX_ATTRIBUTE_COLOR, Utils.getColorNumber(Color.BLACK));
        setAttribute(VERTEX_ATTRIBUTE_WEIGHT, 0);
    }

    public static String VERTEX_ATTRIBUTE_NAME = "name";
    public static String VERTEX_ATTRIBUTE_COLOR = "color";
    public static String VERTEX_ATTRIBUTE_WEIGHT = "weight";
    public static String VERTEX_ATTRIBUTE_SIDE = "side";

    private Graph _graph;
    private int _index;
    private String _name;
    private Map<String, Vertex> _neighbors = new HashMap<String, Vertex>();
    private Map<String, Edge> _outgoingEdges = new HashMap<String, Edge>();
    private Map<String, Edge> _incomingEdges = new HashMap<String, Edge>();

    public Graph getGraph() {
        return _graph;
    }

    public int getIndex() {
        return _index;
    }

    public String getName() {
        return _name;
    }

    void setName(String name) {
        setAttribute(VERTEX_ATTRIBUTE_NAME, name);
        _name = name;
    }

    void connectTo(Vertex other, Edge byEdge) throws Exception {

        if (!_graph.equals(other._graph)) {
            throw new Exception("Can't connect vertex '" + this + "' vertex from another graph");
        }
        if (!byEdge.incidentIn(this)) {
            throw new Exception("Can't connect vertex '" + this + "' by edge '" + byEdge + "'");
        }
        if (!byEdge.incidentIn(other)) {
            throw new Exception("Can't connect vertex '" + this + "' by edge '" + byEdge + "'");
        }
        if (! byEdge.getGraph().equals(_graph)) {
             throw new Exception("Can't connect vertex '" + this + "' by edge from another graph '" + byEdge + "'");
        }
        if (other.equals(this)) {
            throw new Exception("Can't connect vertex '" + this + "' to itself");
        }

        boolean from = this.equals(byEdge.getFromVertex());
        if (from || (! _graph.isDirected()) ) {
            _neighbors.put(other.getName(), other);
            _outgoingEdges.put(other.getName(), byEdge);
        }
        else {        
            _incomingEdges.put(other.getName(), byEdge);
        }
    }

    void disconnectFrom(Vertex other, Edge removedEdge) throws Exception {
        if (other.equals(this)) {
            throw new Exception("Can't disconnect vertex '" + this + "' to itself");
        }
        if (!_graph.equals(other._graph)) {
            throw new Exception("Can't disconnect vertex '" + this + "' vertex from another graph");
        }
        if (!removedEdge.incidentIn(this)) {
            throw new Exception("Can't disconnected vertex '" + this + "' by edge '" + removedEdge + "'");
        }
        if (!removedEdge.incidentIn(other)) {
            throw new Exception("Can't disconnected vertex '" + this + "' by edge '" + removedEdge + "'");
        }
        
        boolean from = this.equals(removedEdge.getFromVertex());
        if (from || (! _graph.isDirected()) ) {
            _neighbors.remove(other.getName());
            _outgoingEdges.remove(other.getName());
        } else {
            _incomingEdges.remove(other.getName());                        
        }
    }

    public Set< Vertex> getNeighbors() {
        return new TreeSet<>(_neighbors.values());
    }

    public Set<Edge> getOutgoingEdges() {
        return new TreeSet(_outgoingEdges.values());
    }

    public Set<Edge> getIncomingEdges() {
        return new TreeSet(_incomingEdges.values());
    }

    public int getOutgoingDegree() {
        return _neighbors.size();
    }

    public int getIncomingDegree() {
        return _incomingEdges.size();
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
        return (int) getAttribute(VERTEX_ATTRIBUTE_COLOR, 0);
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
        return _index - other._index;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        Vertex other = (Vertex) o;
        return ((other != null) && (_index == other._index));
    }

    @Override
    public String toString() {
        return _name;
    }

}
