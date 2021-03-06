/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.algorithms.BFS;
import graphs.algorithms.MaximumCut;
import graphs.core.*;
import graphs.utils.Pair;
import graphs.utils.Utils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author me
 */
public class GraphPanel extends JPanel implements ComponentListener {

    public GraphPanel(Graph graph) {
        super();
        _graph = graph;

    }

    public enum VerticesLayout implements Serializable {
        None,
        Grid,
        Circle,
        BiPartite,
        Tree,
        Random,
    };

    public void init() {
        addComponentListener(this);
    }

    private final Graph _graph;
    private Vertex _selectedVertex;

    //private final List<String> _showVertexAttributes = new ArrayList<>();
    private int _edgesSize = 2;
    private int _verticesSize = 12;

    public static final String GUI_LAYOUT = "_gui-vertices-layout";
    public static final String GUI_SELECTED_VERTEX = "_gui-selected-vertex";
    public static final String VERTEX_X = "_gui-vertex-x";
    public static final String VERTEX_Y = "_gui-vertex-y";
    public static final String GUI_FONT_SIZE = "_gui-font-size";
    public static final String GUI_VERTICES_ATTRIBUTES = "_gui-vertices-attributes";
    public static final String GUI_EDGES_ATTRIBUTES = "_gui-edges-attributes";

    public void setSelectedVertex(Vertex v) {
        _selectedVertex = v;
        _graph.setAttribute(GUI_SELECTED_VERTEX, v);
    }

    public void setVerticesLayout(VerticesLayout layout) {
        switch (layout) {
            case Grid:
                layoutVerticesGrid();
                break;
            case Circle:
                layoutVerticesCircle();
                break;
            case BiPartite:
                layoutVerticesBiPartite();
                break;
            case Tree:
                layoutVerticesTree();
                break;
            case Random:
                layoutVerticesRandom();
                break;
        }
        _graph.setAttribute(GUI_LAYOUT, layout);
        repaint();
    }

    public void setVerticesSize(int size) {
        _verticesSize = size;
        repaint();
    }

    public void setFontSize(int size) {
        _graph.setAttribute(GUI_FONT_SIZE, size);
        repaint();
    }

    public void setEdgesSize(int size) {
        _edgesSize = size;
        repaint();
    }

    public void showVertexAttribute(String attribute) {
        ArrayList<String> showVertexAttributes = (ArrayList<String>) _graph.getAttribute(GUI_VERTICES_ATTRIBUTES, Utils.DEFAULT_VERTICES_ATTRIBUTES_SHOWN);
        if (!showVertexAttributes.contains(attribute)) {
            showVertexAttributes.add(attribute);
            _graph.setAttribute(GUI_VERTICES_ATTRIBUTES, showVertexAttributes);
        }
    }

    public void hideVertexAttribute(String attribute) {
        ArrayList<String> showVertexAttributes = (ArrayList<String>) _graph.getAttribute(GUI_VERTICES_ATTRIBUTES, Utils.DEFAULT_VERTICES_ATTRIBUTES_SHOWN);
        showVertexAttributes.remove(attribute);
        _graph.setAttribute(GUI_VERTICES_ATTRIBUTES, showVertexAttributes);
    }

    public boolean vertexAttributesShown(String attribute) {
        ArrayList<String> showVertexAttributes = (ArrayList<String>) _graph.getAttribute(GUI_VERTICES_ATTRIBUTES, Utils.DEFAULT_VERTICES_ATTRIBUTES_SHOWN);
        return showVertexAttributes.contains(attribute);
    }

    public boolean toggleShowVertexAttribute(String attribute) {
        ArrayList<String> showVertexAttributes = (ArrayList<String>) _graph.getAttribute(GUI_VERTICES_ATTRIBUTES, Utils.DEFAULT_VERTICES_ATTRIBUTES_SHOWN);
        if (showVertexAttributes.contains(attribute)) {
            hideVertexAttribute(attribute);
            return false;
        }
        showVertexAttribute(attribute);
        return true;
    }

    private void layoutVerticesGrid() {
        float panelWidth = (float) getSize().getWidth();
        float panelHeight = (float) getSize().getHeight();
        if ((panelWidth == 0) || (panelHeight == 0)) {
            return;
        }

        float ratio = panelWidth / panelHeight;
        float vertices = _graph.getNumberOfVertices();
        int gridWidth = (int) Math.ceil(Math.sqrt(vertices) * ratio);
        int gridHeight = (int) Math.ceil(vertices / gridWidth) + 1;

        int cellWidth = (int) (panelWidth / (gridWidth + 1));
        int cellHeight = (int) (panelHeight / (gridHeight + 1));

        int i = 0;
        for (Vertex v : _graph.getVertices()) {
            int x = (i % gridWidth) + 1;
            int y = (i / gridWidth) + 1;

            int vertexX = x * cellWidth;
            int vertexY = y * cellHeight;

            v.setAttribute(VERTEX_X, (float) vertexX / panelWidth);
            v.setAttribute(VERTEX_Y, (float) vertexY / panelHeight);
            i++;
        }
    }

    public VerticesLayout getVerticesLayout() {
        return (VerticesLayout) _graph.getAttribute(GUI_LAYOUT, VerticesLayout.None);
    }

    private void layoutVerticesCircle() {
        int i = 0;
        int vertices = _graph.getNumberOfVertices();

        for (Vertex v : _graph.getVertices()) {
            float vertexX = (float) (0.5 + 0.4 * Math.sin((i * Math.PI * 2) / vertices));
            float vertexY = (float) (0.5 - 0.4 * Math.cos((i * Math.PI * 2) / vertices));

            v.setAttribute(VERTEX_X, vertexX);
            v.setAttribute(VERTEX_Y, vertexY);
            i++;
        }

    }

    private void layoutVerticesBiPartite() {

        boolean hasSideAttribute = true;
        for (Vertex v : _graph.getVertices()) {
            if (!v.hasAttribute(Vertex.VERTEX_ATTRIBUTE_SIDE)) {
                hasSideAttribute = false;
            }
        }
        if (!hasSideAttribute) {
            Graph maximumCut = MaximumCut.maximumCut(_graph);
            List<Vertex> sideA = (List<Vertex>) maximumCut.getAttribute(MaximumCut.MAXIMUM_CUT_SIDE_A);
            List<Vertex> sideB = (List<Vertex>) maximumCut.getAttribute(MaximumCut.MAXIMUM_CUT_SIDE_B);
            for (Vertex v : sideA) {
                v.setAttribute(Vertex.VERTEX_ATTRIBUTE_SIDE, 1);
            }
            for (Vertex v : sideB) {
                v.setAttribute(Vertex.VERTEX_ATTRIBUTE_SIDE, 2);
            }
        }

        List<Vertex> sideA = _graph.getVerticesWith(Vertex.VERTEX_ATTRIBUTE_SIDE, 1);
        List<Vertex> sideB = _graph.getVerticesWith(Vertex.VERTEX_ATTRIBUTE_SIDE, 2);

        int aPosition = 0, bPosition = 0;
        for (Vertex v : _graph.getVertices()) {
            float vertexX, vertexY;
            if (v.getAttribute(Vertex.VERTEX_ATTRIBUTE_SIDE).equals(1)) {
                vertexX = 0.1f;
                vertexY = 0.1f + (aPosition * 0.8f / (sideA.size()));
                aPosition++;
            } else {
                vertexX = 0.9f;
                vertexY = 0.1f + (bPosition * 0.8f / (sideB.size()));
                bPosition++;
            }

            v.setAttribute(VERTEX_X, vertexX);
            v.setAttribute(VERTEX_Y, vertexY);
        }
    }

    private void layoutVerticesTree() {
        BFS.Configuration configuration = new BFS.Configuration();
        configuration.putResultsInNewGraph = true;
        configuration.initialVertex = _selectedVertex;
        configuration.createBFSForest = true;
        Graph bfs = BFS.bfs(_graph, configuration);

        int levels = (int) bfs.getAttribute(BFS.BFS_MAXIMUM_DEPTH) + 1;
        float levelHeight = 1.0f / (float) (levels + 1);

        Map<Integer, Integer> verticesInEachLevel = (Map<Integer, Integer>) bfs.getAttribute(BFS.BFS_NUMBER_OF_VERTICES_IN_EACH_LEVEL);

        for (Vertex v : bfs.getVertices()) {
            int depth = (int) v.getAttribute(BFS.BFS_VERTEX_DEPTH);
            int verticesInLevel = verticesInEachLevel.get(depth);
            int indexInLevel = (int) v.getAttribute(BFS.BFS_VERTEX_NUMBER_IN_LEVEL);

            float space = 1.0f / (verticesInLevel + 1);

            Vertex vInGraph = _graph.getVertex(v.getName());
            vInGraph.setAttribute(VERTEX_X, space * indexInLevel);
            vInGraph.setAttribute(VERTEX_Y, levelHeight * (depth + 1));
        }

    }

    private void layoutVerticesRandom() {
        int N = _graph.getNumberOfVertices();
        int gridWidth = (int) Math.sqrt(N) * 2;

        Set<Pair<Integer>> locations = new HashSet<>();
        do {
            Pair<Integer> location = new Pair<>();
            location.first = Utils.RANDOM.nextInt(gridWidth);
            location.second = Utils.RANDOM.nextInt(gridWidth);
            if (!locations.contains(location)) {
                locations.add(location);
            }
        } while (locations.size() < N);

        int index = 1;
        float gridUnit = 0.8f / gridWidth;
        for (Pair<Integer> location : locations) {
            Vertex v = _graph.getVertex(index);
            v.setAttribute(VERTEX_X, 0.1f + (gridUnit * location.first));
            v.setAttribute(VERTEX_Y, 0.1f + (gridUnit * location.second));
            index++;
        }
    }

    public List<String> shownVerticesAttribtes() {
        return (ArrayList<String>) _graph.getAttribute(GUI_VERTICES_ATTRIBUTES, Utils.DEFAULT_VERTICES_ATTRIBUTES_SHOWN);
    }

    private void repaintGraph(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Utils.DEFAULT_COLOR);
        g2.setStroke(new BasicStroke(_edgesSize));
        float panelWidth = (float) getSize().getWidth();
        float panelHeight = (float) getSize().getHeight();

        ArrayList<String> showVertexAttributes = (ArrayList<String>) _graph.getAttribute(GUI_VERTICES_ATTRIBUTES, Utils.DEFAULT_VERTICES_ATTRIBUTES_SHOWN);
        boolean showVerticesColors = showVertexAttributes.contains(Vertex.VERTEX_ATTRIBUTE_COLOR);

        for (Vertex v : _graph.getVertices()) {
            int vertexX = (int) ((float) v.getAttribute(VERTEX_X) * panelWidth);
            int vertexY = (int) ((float) v.getAttribute(VERTEX_Y) * panelHeight);

            if (showVerticesColors) {
                g.setColor(Utils.COLORS.get(v.getColor()));
            }

            int fontSize = (int) _graph.getAttribute(GUI_FONT_SIZE, Utils.DEFAULT_FONT_SIZE);
            g.setFont(new Font("Arial", Font.PLAIN, fontSize));

            int height = g.getFontMetrics().getHeight();
            int line = 1;
            if (showVertexAttributes.contains(Vertex.VERTEX_ATTRIBUTE_NAME)) {
                g.drawString(v.getName(), vertexX, vertexY + (line * height));
                line++;
            }
            g.setColor(Utils.COLORS.get(v.getColor()));

            for (String attribute : v.attributeNames()) {
                if (Vertex.VERTEX_ATTRIBUTE_NAME.equals(attribute)) {
                    continue;
                }
                if (Vertex.VERTEX_ATTRIBUTE_COLOR.equals(attribute)) {
                    continue;
                }
                if (!showVertexAttributes.contains(attribute)) {
                    continue;
                }

                String text = attribute + " : " + v.getAttribute(attribute);

                g.drawString(text, vertexX, vertexY + (line * height));
                line++;
            }
            g2.fill(new Ellipse2D.Float(vertexX - (_verticesSize / 2), vertexY - (_verticesSize / 2), _verticesSize, _verticesSize));
        }

        g.setColor(Utils.DEFAULT_COLOR);

        ArrayList<String> showEdgesAttributes = (ArrayList<String>) _graph.getAttribute(GUI_EDGES_ATTRIBUTES, Utils.DEFAULT_EDGES_ATTRIBUTES_SHOWN);
        boolean showEdgesColor = showEdgesAttributes.contains(Edge.EDGE_ATTRIBUTE_COLOR);

        for (Edge edge : _graph.getEdges()) {
            if (showEdgesColor) {
                g.setColor(Utils.COLORS.get(edge.getColor()));
            }
            Vertex from = edge.getFromVertex();
            Vertex to = edge.getToVertex();

            int fromX = (int) ((float) from.getAttribute(VERTEX_X) * panelWidth);
            int fromY = (int) ((float) from.getAttribute(VERTEX_Y) * panelHeight);
            int toX = (int) ((float) to.getAttribute(VERTEX_X) * panelWidth);
            int toY = (int) ((float) to.getAttribute(VERTEX_Y) * panelHeight);

            g.drawLine(fromX, fromY, toX, toY);
            if (_graph.isDirected()) {
                int middleX = (fromX + toX)/2;
                int middleY = (fromY + toY)/2;
                int y = toY - fromY;
                int x = toX - fromX;
                if (x == 0) {
                    x = 1;
                }
                float angle = (float)Math.atan((float) y / x);
                if (x > 0) {
                    angle += Math.PI;
                }
                float angle1 = (float) (angle - (Math.PI / 8));
                float angle2 = (float) (angle + (Math.PI / 8));
                float arrowLength = 30;

                g.drawLine(middleX, middleY, middleX + (int) (arrowLength * Math.cos(angle1)), middleY + (int) (arrowLength * Math.sin(angle1)));
                g.drawLine(middleX, middleY, middleX + (int) (arrowLength * Math.cos(angle2)), middleY + (int) (arrowLength * Math.sin(angle2)));

            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        repaintGraph(g);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        switch (getVerticesLayout()) {
            case Grid:
                layoutVerticesGrid();
                break;
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
