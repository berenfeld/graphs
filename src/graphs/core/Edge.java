/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.core;

import graphs.utils.Utils;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author me
 */
public class Edge extends BaseElement implements Comparable, Serializable {

    public Edge(Graph graph, Vertex v1, Vertex v2)  {
        _graph = graph;
        _from = v1;
        _to = v2;
    }

    private Graph _graph;
    private Vertex _from;
    private Vertex _to;

    public Vertex getFromVertex() {
        return _from;
    }

    public Vertex getToVertex() {
        return _to;
    }

    public Vertex getOtherVertex(Vertex other) {
        return getOtherVertex(other.getName());
    }
    
    public Vertex getOtherVertex(String other) {
        if (other.equals(_to.getName())) {
            return _from;
        } else if (other.equals(_from.getName())) {
            return _to;
        } else {
            return null;
        }
    }
    
    public Vertex getCommonVertex(Edge edge ) {
        if (edge.incidentIn(_from)) {
            return _from;
        } else if (edge.incidentIn(_to)) {
            return _to;
        } else {
            return null;
        }
                
    }
    public boolean incidentIn(Vertex v) {
        return v.equals(_to) || v.equals(_from);
    }

    public String getName() {
        return Utils.edgeName(_from, _to);
    }
    
    public Graph getGraph() {
        return _graph;
    }

    public static String EDGE_ATTRIBUTE_COLOR = "Color";
    public static int EDGE_COLOR_WHITE = 1;
    public static int EDGE_COLOR_GRAY = 2;
    public static int EDGE_COLOR_BLACK = 3;  
    public static String EDGE_ATTRIBUTE_WEIGHT = "Weight";

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Edge other = (Edge) obj;
        return ((_from.equals(other._from)) && (_to.equals(other._to)))
                || ((_from.equals(other._to)) && (_to.equals(other._from)));
    }

     @Override
    public int compareTo(Object o) {
        if (o == null) {
            return 1;
        }
        Edge other = (Edge) o;
        return getName().compareTo(other.getName());
    }
    
    public int getColor() {
        return (int) getAttribute(EDGE_ATTRIBUTE_COLOR);
    }

    public void setColor(int color) {
        setAttribute(EDGE_ATTRIBUTE_COLOR, color);
    }

        public int getWeight() {
        return (int) getAttribute(EDGE_ATTRIBUTE_WEIGHT);
    }

    public void setWeight(int color) {
        setAttribute(EDGE_ATTRIBUTE_WEIGHT, color);
    }
    @Override
    public String toString() {
        return Utils.edgeName(_from, _to);
    }

}
