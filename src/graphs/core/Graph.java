/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.core;

import graphs.algorithms.BFS;
import graphs.algorithms.DFS;
import graphs.algorithms.Factory;
import graphs.gui.MessageListener;
import graphs.utils.Utils;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author me
 */
public class Graph extends BaseElement implements Serializable {

    public Graph(String name, boolean directed) {
        _name = name;
        _directed = directed;
        MessageListener.message("Graph '" + name + "' created.");
    }

    public Graph(String name) {
        this(name, false);
    }
    private boolean _directed;
    private String _name;

    // structure
    private int _vertexIndex = 1;
    private final Map<String, Vertex> _vertices = new TreeMap<>();
    private final Map<String, Edge> _edges = new TreeMap<>();
    private final ArrayList<String> _vertexNames = new ArrayList<>();

    private final ArrayList<String> _edgeNames = new ArrayList<>();

    // connectivity
    private boolean _connectivityCalculated = false;
    private final Map<Vertex, Map<String, Vertex>> _connectedComponents = new TreeMap<>();

    private int _numberOfConnectedComponents = 0;

    // diameter
    private boolean _diameterCalculated = false;
    private int _diameter = 0;
    private Path _diameterPath;

    public boolean isDirected() {
        return _directed;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public boolean isComplete() {
        if (_directed) {
            return getNumberOfEdges() == (getNumberOfVertices() * (getNumberOfVertices() - 1));
        } else {
            return getNumberOfEdges() == (getNumberOfVertices() * (getNumberOfVertices() - 1)) / 2;
        }
    }

    public boolean isEmpty() {
        return getNumberOfEdges() == 0;
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
        return new TreeSet<>(_vertices.values());
    }

    public Set<String> getVerticesNames() {
        return new TreeSet<>(_vertexNames);
    }

    public List<Vertex> getVerticesWith(String attribute, Object value) {
        List<Vertex> result = new ArrayList<>();
        for (Vertex v : getVertices()) {
            if (value.equals(v.getAttribute(attribute))) {
                result.add(v);
            }
        }
        return result;
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
        return getVertex(_vertexNames.get(index - 1));
    }

    public Vertex getFirstVertex() {
        return getVertex(1);
    }

    public Vertex getRandomVertex() {
        int index = Utils.RANDOM.nextInt(_vertices.size());
        return getVertex(_vertexNames.get(index));
    }

    public Vertex addVertex() throws Exception {
        String name = "v" + String.valueOf((_vertexIndex));
        return addVertex(name);
    }

    public Vertex addVertex(String name) throws Exception {
        if (_vertices.containsKey(name)) {
            throw new Exception("Vertex '" + name + "' already exists");
        }
        Vertex newVertex = new Vertex(this, name, _vertexIndex);

        _vertices.put(name, newVertex);
        _vertexNames.add(name);
        _connectivityCalculated = false;
        _diameterCalculated = false;
        ++_vertexIndex;
        MessageListener.message("Graph '" + _name + "' : Vertex '" + name + "' created.");
        return newVertex;
    }

    public void removeVertex(String name) throws Exception {
        // structure
        if (!_vertices.containsKey(name)) {
            throw new Exception("Vertex '" + name + "' does not exists");
        }
        Vertex vertex = _vertices.get(name);
        Set< Edge> outgoingEdges = vertex.getOutgoingEdges();
        for (Edge edge : outgoingEdges) {
            removeEdge(edge);
        }
        if ( _directed ) {
            Set< Edge> incomingEdges = vertex.getIncomingEdges();
            for (Edge edge : incomingEdges) {
                removeEdge(edge);
            }
        }
        _vertices.remove(name);
        _vertexNames.remove(name);
        _connectivityCalculated = false;
        _diameterCalculated = false;
        MessageListener.message("Graph '" + _name + "' : Vertex '" + name + "' removed.");
    }

    public void removeVertex(Vertex v) throws Exception {
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

        String edgeName = Utils.edgeName(fromVertex, toVertex, !_directed);
        if (_edges.containsKey(edgeName)) {
            throw new Exception("Edge '" + edgeName + "' already exists");
        }
        Edge newEdge = new Edge(this, fromVertex, toVertex);
        _edges.put(edgeName, newEdge);
        _edgeNames.add(edgeName);
        fromVertex.connectTo(toVertex, newEdge);
        toVertex.connectTo(fromVertex, newEdge);         

        _connectivityCalculated = false;
        _diameterCalculated = false;
        MessageListener.message("Graph '" + _name + "' : Edge '" + edgeName + "' created.");
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
        String edgeName = Utils.edgeName(fromVertex, toVertex, !_directed);
        Edge removedEdge = _edges.get(edgeName);
        if (removedEdge == null) {
            throw new Exception("Edge '" + removedEdge + "' does not exist");
        }

        fromVertex.disconnectFrom(toVertex, removedEdge);
        toVertex.disconnectFrom(fromVertex, removedEdge);

        _edges.remove(edgeName);
        _edgeNames.remove(edgeName);

        _connectivityCalculated = false;
        _diameterCalculated = false;
        MessageListener.message("Graph '" + _name + "' : Edge '" + edgeName + "' removed.");
    }

    public boolean hasEdge(Vertex from, Vertex to) {
        return hasEdge(Utils.edgeName(from.getName(), to.getName(), !_directed));
    }

    public boolean hasEdge(String from, String to) {
        return hasEdge(Utils.edgeName(from, to, !_directed));
    }

    public boolean hasEdge(String name) {
        return _edges.containsKey(name);
    }

    public Edge getEdge(Vertex from, Vertex to) {
        return getEdge(from.getName(), to.getName());
    }

    public Edge getEdge(String from, String to) {
        return getEdge(Utils.edgeName(from, to, !_directed));
    }

    public Edge getEdge(String name) {
        return _edges.get(name);
    }

    public Edge getEdge(int index) {
        return getEdge(_edgeNames.get(index));
    }

    public Edge getRandomEdge() {
        int index = Utils.RANDOM.nextInt(_edgeNames.size());
        return getEdge(index);
    }

    public int getSumOfWeightsOfEdges() {
        int sum = 0;
        for (Edge edge : _edges.values()) {
            sum += edge.getWeight();
        }
        return sum;
    }

    public List<Integer> getDegrees() {
        List<Integer> result = new ArrayList<>();
        _vertices.values().forEach((v) -> {
            result.add(v.getOutgoingDegree());
        });
        return result;
    }

    public int getMaximumDegree() {
        int maximum = 0;
        for (Vertex v : _vertices.values()) {
            maximum = Math.max(maximum, v.getOutgoingDegree());
        }
        return maximum;
    }

    public int getMinimumDegree() {
        int minimum = _vertexNames.size() - 1;
        for (Vertex v : _vertices.values()) {
            minimum = Math.min(minimum, v.getOutgoingDegree());
        }
        return minimum;
    }

    private void calculateConnectivity() {
        if (_connectivityCalculated) {
            return;
        }

        if (_directed){
            calculateConnectivityDirected();
        } else {
            calculateConnectivityUnDirected();
        }
    }
    
    private void calculateConnectivityDirected()
    {
         _connectedComponents.clear();
        _numberOfConnectedComponents = 0;
        ArrayList<String> leftVertices = new ArrayList<>(_vertexNames);
        Graph dfs = DFS.dfs(this, null, true, true);
        ArrayList<Vertex> visited = (ArrayList<Vertex>)dfs.getAttribute(DFS.DFS_VERTICES_VISIT_LIST);
        
        Utils.info("visited " + visited);
        Graph transpose = Factory.transposeOf(this);
        int index = getNumberOfVertices() - 1;
        while (! leftVertices.isEmpty())
        {
            Vertex v = visited.get(index);
            index --;
            if ( leftVertices.contains(v.getName()))
            {
                
                Graph dfsT = DFS.dfs(transpose, v, true, false );
                ArrayList<Vertex> visitedT = (ArrayList<Vertex>)dfsT.getAttribute(DFS.DFS_VERTICES_VISIT_LIST);
                Utils.info("doing dfs from " + v + " visited " + visitedT);
                Map<String, Vertex> connectedComponent = new TreeMap<>();
                for (Vertex connectedToV : visitedT)
                {
                    if ( leftVertices.contains(connectedToV.getName())) {
                        connectedComponent.put(connectedToV.getName(), getVertex(connectedToV.getName()));
                        leftVertices.remove(connectedToV.getName());
                    }                        
                }
                _connectedComponents.put(v, connectedComponent);
                ++_numberOfConnectedComponents;
            }
        }
        _connectivityCalculated = true;
    }
    
    private void calculateConnectivityUnDirected()
    {
        _connectedComponents.clear();
        _numberOfConnectedComponents = 0;
        ArrayList<String> leftVertices = new ArrayList<>(_vertexNames);
        while (!leftVertices.isEmpty()) {
            Vertex v = getVertex(leftVertices.get(0));
            Graph bfs = BFS.bfs(this, v, true);
 
            Map<String, Vertex> connectedComponent = new TreeMap<>();
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
            _diameterPath = null;
            return;
        }

        int maximumBfsDepth = 0;
        for (Vertex v : _vertices.values()) {
            Graph bfsOfV = BFS.bfs(this, v, true);
            int vertexDepth = (int) bfsOfV.getAttribute(BFS.BFS_MAXIMUM_DEPTH);
            if (vertexDepth > maximumBfsDepth) {
                maximumBfsDepth = vertexDepth;
                Vertex maximumDepthVertex = (Vertex) bfsOfV.getAttribute(BFS.BFS_MAXIMUM_DEPTH_VERTEX);
                _diameterPath = (Path) maximumDepthVertex.getAttribute(BFS.BFS_VERTEX_PATH_FROM_ROOT);
            }
        }
        _diameter = maximumBfsDepth;
    }

    // diameter
    public int diameter() {
        calcualteDiameter();
        return _diameter;
    }

    public Path diameterPath() {
        calcualteDiameter();
        return _diameterPath;
    }

    class IntComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    public ArrayList<Integer> gradeSequence() {
        ArrayList<Integer> result = new ArrayList<>(_vertices.size());
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
        if (! ( obj instanceof Graph)) {
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
