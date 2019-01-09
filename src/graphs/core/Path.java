/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.core;

import graphs.utils.*;
import java.util.*;

/**
 *
 * @author me
 */
public class Path {
        
    public Path(Path path) {
        _graph = path._graph;
        _vertices = new LinkedList(path._vertices);
        _edges = new LinkedList(path._edges);
        _cycle = path._cycle;
    }
    
    public Path(Graph g) {
        this._graph = g;
    }
    private Graph _graph;
    private boolean _cycle;
    private List<Edge> _edges = new LinkedList<>();
    private List<Vertex> _vertices = new LinkedList<>();
    
    public void add(Edge edge) throws Exception {
        if (edge == null) {
            throw new Exception("Edge '" + edge + "' is null" );
        }
        
        if (! edge.getGraph().equals(_graph)) {
            throw new Exception("Edge '" + edge + "' from different graph" );
        }
        if ( _edges.contains(edge)) {
            throw new Exception("Edge '" + edge + "' already in path " + this );
        }
        if ( _edges.isEmpty()) {
            _edges.add(edge);
            _vertices.add(edge.getFromVertex());
            _vertices.add(edge.getToVertex());
            _cycle = false;
            return;
        }
        
        Vertex firstVertex = getFirstVertex();
        Vertex lastVertex = getLastVertex();
        Edge lastEdge = getLastEdge();
        Vertex common = edge.getCommonVertex(lastEdge);
        if (common == null) {
            throw new Exception("Edge '" + edge + "' hsd no common vertex with " + lastEdge );
        }
        
        if (_edges.size() == 1) {
            if (common.equals(firstVertex)) {
                Vertex temp = firstVertex;
                firstVertex = lastVertex;
                lastVertex = temp;
                _vertices.set(0, firstVertex);          
                _vertices.set(1, lastVertex);                
            }
        } 
        if (! edge.incidentIn(lastVertex)) {
            throw new Exception("Edge '" + edge + "' does not incident in last vertex of " + this );
        }
        
        Vertex otherVertex = edge.getOtherVertex(common);
        _vertices.add(otherVertex);
        _edges.add(edge);
        
        if (otherVertex.equals(firstVertex)) {
            _cycle = true;
        }         
    }
    
    public Vertex getFirstVertex() {
        return Utils.getFirst(_vertices);
    }
    
    public Vertex getLastVertex() {
        return Utils.getLast(_vertices);
    }
        
    public Edge getLastEdge() {
        return Utils.getLast(_edges);
        
    }
    
    public List<Edge> edges() {
        return _edges;
    }
    public Edge removeLastEdge() throws Exception {
        if ( _edges.isEmpty() ) {
            throw new Exception("Path is empty" );
        }
        Edge lastEdge = Utils.getLast(_edges);        
       
         _edges.remove(lastEdge);
       
        _cycle = false;            
        if (_edges.isEmpty()) {
            _vertices.clear();
        }
        return lastEdge;
    }
    
    public boolean isEmpty() { 
        return _edges.isEmpty();
    }
    public boolean isCycle() {
        return ( _vertices.size() > 1 ) && ( getLastVertex().equals(getFirstVertex()) );
    }

    @Override
    public String toString() {
        return _vertices.toString();
    }

    @Override
    public boolean equals(Object obj) {
        Path other = (Path)obj;
        return _edges.equals(other._edges);
    }
        
}
