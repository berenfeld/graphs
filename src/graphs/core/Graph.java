/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.core;

import graphs.algorithms.BFS;
import graphs.utils.Utils;
import java.util.*;

/**
 *
 * @author me
 */
public class Graph extends BaseElement {

    public Graph(String name) {
        _name = name;
    }

    private String _name;

    // structure
    private int _vertexIndex = 0;
    private final Map<String, Vertex> _vertices = new TreeMap<String, Vertex>();
    private final Map<String, Edge> _edges = new TreeMap<String, Edge>();
    private final ArrayList<String> _vertexNames = new ArrayList<String>();
    private final ArrayList<String> _edgeNames = new ArrayList<String>();

    private final Random rand = new Random();

    // connectivity
    private boolean _connectivityCalculated = false;
    private Map<Vertex, Map<String, Vertex>> _connectedComponents = new TreeMap<Vertex, Map<String, Vertex>>();
   
    private int _numberOfConnectedComponents = 0;

    // diameter
    private boolean _diameterCalculated = false;
    private int _diameter = 0;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public boolean isComplete() {
        return getNumberOfEdges() == (getNumberOfVertices() * (getNumberOfVertices() - 1)) / 2;
    }

    public int getNumberOfVertices() {
        return _vertices.size();
    }

    public void setVertexName(Vertex v, String newName) {
        setVertexName(v.getName(), newName);
    }
    
    public void setVertexName(String oldName, String newName) {
        _vertices.get(oldName).setName(newName);        
    }
    public Set<Vertex> getVertices() {
        return new TreeSet<>( _vertices.values());
    }

    public Set<String> getVerticesNames() {
        return new TreeSet<>( _vertexNames );
    }
    
    public int getNumberOfEdges() {
        return _edges.size();
    }

    public Set<Edge> getEdges() {
        return new TreeSet<>(_edges.values());
    }

    public Vertex getVertex(String name) {
        return _vertices.get(name);
    }

    public Vertex getVertex(int index) {
        return _vertices.get(_vertexNames.get(index));
    }

    public Vertex getRandomVertex() {
        int index = rand.nextInt(_vertexNames.size());
        return getVertex(index);
    }

    public Vertex addVertex(String name) throws Exception {
        // structure
        if (_vertices.containsKey(name)) {
            throw new Exception("Vertex '" + name + "' already exists");
        }
        ++ _vertexIndex;
        Vertex newVertex = new Vertex(this, name, _vertexIndex);
        _vertices.put(name, newVertex);
        _vertexNames.add(name);
        _connectivityCalculated = false;
        _diameterCalculated = false;
        return newVertex;
    }

    public void removeVertex(String name) throws Exception {
        // structure
        if (!_vertices.containsKey(name)) {
            throw new Exception("Vertex '" + name + "' does not exists");
        }
        Vertex vertex = _vertices.get(name);
        Set< Edge> adjacents = vertex.getAdjacentEdges();
        for (Edge edge : adjacents) {
            removeEdge(edge);
        }
        _vertices.remove(name);
        _vertexNames.remove(name);
        _connectivityCalculated = false;
        _diameterCalculated = false;
    }

    public void removeVertex(Vertex v) throws Exception {
        if (!v.getGraph().equals(this)) {
            throw new Exception("Vertex '" + v.getName() + "' does not belong to this graph");
        }
        removeVertex(v.getName());
    }

    public Edge addEdge(Edge edge) throws Exception {
        return addEdge(edge.getFromVertex(), edge.getToVertex());
    }

    public Edge addEdge(Vertex from, Vertex to) throws Exception {
        return addEdge(from.getName(), to.getName());
    }

    public Edge addEdge(String from, String to) throws Exception {
        // structure
        Vertex fromVertex = _vertices.get(from);
        if (fromVertex == null) {
            throw new Exception("Vertex '" + from + "' does not exist");
        }
        Vertex toVertex = _vertices.get(to);
        if (toVertex == null) {
            throw new Exception("Vertex '" + to + "' does not exist");
        }

        String edgeName = Utils.edgeName(fromVertex, toVertex);
        Edge newEdge = new Edge(this, fromVertex, toVertex);
        if (_edges.containsKey(edgeName)) {
            throw new Exception("Edge '" + newEdge + "' already exists");
        }
        _edges.put(edgeName, newEdge);
        _edgeNames.add(edgeName);
        fromVertex.connectTo(toVertex, newEdge);
        toVertex.connectTo(fromVertex, newEdge);

        _connectivityCalculated = false;
        _diameterCalculated = false;
        return newEdge;
    }

    public void removeEdge(Vertex from, Vertex to) throws Exception {
        removeEdge(from.getName(), to.getName());
    }

    public void removeEdge(Edge edge) throws Exception {
        removeEdge(edge.getFromVertex(), edge.getToVertex());
    }

    public void removeEdge(String from, String to) throws Exception {
        Vertex fromVertex = _vertices.get(from);
        if (fromVertex == null) {
            throw new Exception("Vertex '" + from + "' does not exist");
        }
        Vertex toVertex = _vertices.get(to);
        if (toVertex == null) {
            throw new Exception("Vertex '" + to + "' does not exist");
        }
        String edgeName = Utils.edgeName(fromVertex, toVertex);
        Edge edge = _edges.get(edgeName);
        if (edge == null) {
            throw new Exception("Edge '" + edge + "' does not exist");
        }
        fromVertex.disconnectFrom(toVertex);
        toVertex.disconnectFrom(fromVertex);
        _edges.remove(edgeName);
        _edgeNames.remove(edgeName);

        _connectivityCalculated = false;
        _diameterCalculated = false;
    }

    public boolean hasEdge(Vertex from, Vertex to) {
        return hasEdge(Utils.edgeName(from.getName(), to.getName()));
    }

    public boolean hasEdge(String from, String to) {
        return hasEdge(Utils.edgeName(from, to));
    }

    public boolean hasEdge(String name) {
        return _edges.containsKey(name);
    }

    public Edge getEdge(Vertex from, Vertex to) {
        return getEdge(from.getName(), to.getName());
    }

    public Edge getEdge(String from, String to) {
        return getEdge(Utils.edgeName(from, to));
    }

    public Edge getEdge(String name) {
        return _edges.get(name);
    }

    public Edge getEdge(int index) {
        return getEdge(_edgeNames.get(index));
    }

    public Edge getRandomEdge() {
        int index = rand.nextInt(_edgeNames.size());
        return getEdge(index);
    }

    public int getSumOfWeightsOfEdges() {
        int sum = 0;
        for (Edge edge : _edges.values()) {
            sum += edge.getWeight();
        }
        return sum;
    }
    
    private void calculateConnectivity() {
        if (_connectivityCalculated) {
            return;
        }
        _connectedComponents.clear();
        _numberOfConnectedComponents = 0;
        ArrayList<String> leftVertices = new ArrayList<String>(_vertexNames);
        while (!leftVertices.isEmpty()) {
            Vertex v = getVertex(leftVertices.get(0));
            Graph bfs = BFS.bfs(this, v);
            Vertex bfsRoot = bfs.getVertex(v.getName());

            Map<String, Vertex> connectedComponent = new TreeMap<String, Vertex>();
            for (String vertexName : bfs.getVerticesNames()) {
                connectedComponent.put(vertexName, getVertex(vertexName));
                leftVertices.remove(vertexName);
            }
            _connectedComponents.put(v, connectedComponent);
            ++_numberOfConnectedComponents;
        }
        _connectivityCalculated = true;
    }

    // connectivity
    public boolean isConnected() {
        calculateConnectivity();
        return _connectedComponents.size() == 1;
    }

    public int getNumberOfConnectedComponents() {
        calculateConnectivity();
        return _numberOfConnectedComponents;
    }

    public Map<Vertex, Map<String, Vertex>> getConnectedComponents() {
        calculateConnectivity();
        return _connectedComponents;
    }

    void calcualteDiameter() {
        if (_diameterCalculated) {
            return;
        }
        _diameterCalculated = true;
        if (!isConnected()) {
            _diameter = Integer.MAX_VALUE;
            return;
        }

        
        int maximumBfsDepth = 0;
        for (Vertex v : _vertices.values()) {
            Graph bfsOfV = BFS.bfs(this, v);
            Vertex bfsRoot = bfsOfV.getVertex(v.getName());
            maximumBfsDepth = Math.max(maximumBfsDepth, (int) bfsRoot.getAttribute(BFS.BFS_MAXIMUM_DEPTH));
            
        }
        _diameter = maximumBfsDepth;
    }

    // diameter
    public int diameter() {
        calcualteDiameter();
        return _diameter;
    }

    class IntComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    public Vector<Integer> gradeSequence() {
        Vector<Integer> result = new Vector<Integer>(_vertices.size());
        for (Vertex v : _vertices.values()) {
            result.add(v.grade());

        }
        Collections.sort(result, Collections.reverseOrder());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Graph other = (Graph) obj;
        return (_vertices.equals(other._vertices)) && (_edges.equals(other._edges));
    }

    @Override
    public String toString() {
        return "Graph '" + _name + "' V ( " + _vertices.size() + " ) = {" + _vertices.keySet() + "} E ( " + _edges.size() + " ) = { " + _edges.values() + " }";
    }

}
