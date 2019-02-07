/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.algorithms.Factory;
import graphs.core.*;
import static graphs.gui.GraphPanel.VERTEX_X;
import static graphs.gui.GraphPanel.VERTEX_Y;
import graphs.utils.Utils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.*;

/**
 *
 * @author me
 */
public final class GraphFrame extends JInternalFrame implements MouseListener, ActionListener, MouseMotionListener, AttributesListener {

    public GraphFrame(MainWindow mainWindow, Graph graph) {
        super(graph.getName(), true, true, true, true);
        _mainWindow = mainWindow;
        _graph = graph;
        _canvas = new GraphPanel(_graph);

        setLocation(0, 0);
        for (Vertex v : _graph.getVertices()) {
            v.addAttributeListener(this);
        }

        _canvas.init();
        _canvas.addMouseListener(this);
        _canvas.addMouseMotionListener(this);
        getContentPane().add(_canvas, BorderLayout.CENTER);
        initMenus();
    }

    private final MainWindow _mainWindow;
    private MainWindow.SelectionMode _selectionMode = MainWindow.SelectionMode.Normal;
    private final Graph _graph;
    private final JPopupMenu _graphMenu = new JPopupMenu();
    private final JPopupMenu _vertexMenu = new JPopupMenu();
    private final JPopupMenu _edgeMenu = new JPopupMenu();
    private final JMenuItem _graphNameMenu = new JMenuItem();
    private final JMenuItem _vertexNameMenu = new JMenuItem();
    private final JMenuItem _edgeNameMenu = new JMenuItem();
    private Vertex _selectedVertex;
    private Edge _selectedEdge;
    private final JMenuItem _changeGraphNameMenu = new JMenuItem("Change Name");
    private final JMenuItem _graphPropertiesMenu = new JMenuItem("Properties");
    private final JMenuItem _addVertexMenu = new JMenuItem("Add Vertex");
    private final JMenuItem _changeVertexNameMenu = new JMenuItem("Change Name");
    private final JMenuItem _removeVertexMenu = new JMenuItem("Remove");
    private final JMenu _showVerticesAttributes2 = new JMenu("Show Attributes");
    private final JMenuItem _removeEdgeMenu = new JMenuItem("Remove");
    private final JMenu _verticesSizeMenu = new JMenu("Vertices Size");
    private final JMenu _edgesSizeMenu = new JMenu("Edges Size");
    private final JMenu _fontSizeMenu = new JMenu("Font Size");
    private final JMenu _showVerticesAttributes = new JMenu("Show Vertices Attributes");
    private final JMenu _graphOperations = new JMenu("Graph Operations");
    private final JMenuItem _complementMenuItem = new JMenuItem("Complement");
    private final JMenuItem _transposeMenuItem = new JMenuItem("Transpose");    
    private final JMenu _verticesLayoutMenu = new JMenu("Vertices Layout");
    private final Map<String, List< JMenuItem>> _verticesAttributesMenu = new HashMap<>();
    private final JMenuItem _verticesLayoutGridMenu = new JMenuItem("Grid");
    private final JMenuItem _verticesLayoutCircleMenu = new JMenuItem("Circle");
    private final JMenuItem _verticesLayoutBiPartiteMenu = new JMenuItem("Bi-Partite");
    private final JMenuItem _verticesLayoutTreeMenu = new JMenuItem("Tree");
    private final JMenuItem _verticesLayoutRandomMenu = new JMenuItem("Random");
    private final List<JMenuItem> _verticesSizeMenus = new ArrayList<>();
    private final List<JMenuItem> _edgesSizeMenus = new ArrayList<>();
    private final List<JMenuItem> _fontSizeMenus = new ArrayList<>();
    private final GraphPanel _canvas;
    private int _clickX, _clickY;
    private float _panelWidth, _panelHeight;
    private Vertex _draggedVertex;
    private Vertex _newEdgeFromVertex;
    private Vertex _newEdgeToVertex;
    private Edge _newEdge;
    private long _lastRepaint;

    private static final int CLOSE_DISTANCE = 20;

    public void setSelectionMode(MainWindow.SelectionMode selectionMode) {
        Utils.debug("Graph Frame '" + _graph.getName() + "' selection mode set to " + selectionMode);
        _selectionMode = selectionMode;
    }

    
    
    private void initMenus() {
        _graphNameMenu.setEnabled(false);
        _graphNameMenu.setText(_graph.getName());
        _graphNameMenu.setEnabled(false);
        _graphMenu.add(_graphNameMenu);
        _graphMenu.addSeparator();
        _graphMenu.add(_changeGraphNameMenu);
        _changeGraphNameMenu.addActionListener(this);
        _graphMenu.add(_graphPropertiesMenu);
        _graphPropertiesMenu.addActionListener(this);
        _graphMenu.add(_addVertexMenu);
        _addVertexMenu.addActionListener(this);
        _graphMenu.add(_verticesSizeMenu);
        _graphMenu.add(_edgesSizeMenu);

        for (int i = 3; i <= 15; i++) {
            JMenuItem verticesSizeMenuItem = new JMenuItem(i + " Point");
            _verticesSizeMenus.add(verticesSizeMenuItem);
            verticesSizeMenuItem.addActionListener(this);
            _verticesSizeMenu.add(verticesSizeMenuItem);
        }

        for (int i = 1; i <= 5; i++) {
            JMenuItem edgesSizeMenuItem = new JMenuItem(i + " Point");
            _edgesSizeMenus.add(edgesSizeMenuItem);
            edgesSizeMenuItem.addActionListener(this);
            _edgesSizeMenu.add(edgesSizeMenuItem);
        }
        _graphMenu.add(_fontSizeMenu);
        for (int fontSize : Utils.FONT_SIZES) {
            JMenuItem fontSizeMenuItem = new JMenuItem(fontSize + " Points");
            _fontSizeMenus.add(fontSizeMenuItem);
            fontSizeMenuItem.addActionListener(this);
            _fontSizeMenu.add(fontSizeMenuItem);
        }        

        _verticesLayoutMenu.add(_verticesLayoutGridMenu);
        _verticesLayoutGridMenu.addActionListener(this);
        _verticesLayoutMenu.add(_verticesLayoutCircleMenu);
        _verticesLayoutCircleMenu.addActionListener(this);
        _verticesLayoutMenu.add(_verticesLayoutBiPartiteMenu);
        _verticesLayoutBiPartiteMenu.addActionListener(this);
        _verticesLayoutMenu.add(_verticesLayoutTreeMenu);
        _verticesLayoutTreeMenu.addActionListener(this);
        _verticesLayoutMenu.add(_verticesLayoutRandomMenu);
        _verticesLayoutRandomMenu.addActionListener(this);
        _graphMenu.add(_verticesLayoutMenu);

        _graphMenu.add(_showVerticesAttributes);
        createVerticesAttributesMenu();

        _graphOperations.add(_complementMenuItem);
        _complementMenuItem.addActionListener(this);
        _graphOperations.add(_transposeMenuItem);
        _transposeMenuItem.addActionListener(this);
        _graphMenu.add(_graphOperations);        
        
        _vertexNameMenu.setEnabled(false);
        _vertexMenu.add(_vertexNameMenu);
        _vertexMenu.addSeparator();
        _vertexMenu.add(_changeVertexNameMenu);
        _changeVertexNameMenu.addActionListener(this);
        _vertexMenu.add(_removeVertexMenu);
        _removeVertexMenu.addActionListener(this);
        _vertexMenu.add(_showVerticesAttributes2);

        _edgeNameMenu.setEnabled(false);
        _edgeMenu.add(_edgeNameMenu);
        _edgeMenu.addSeparator();
        _edgeMenu.add(_removeEdgeMenu);
        _removeEdgeMenu.addActionListener(this);
    }

    public Graph getGraph() {
        return _graph;
    }

    private void handleLeftMouseClicked(MouseEvent e) throws Exception {
        switch (_selectionMode) {
            case Normal:
                break;
            case AddVertex:
                if (_selectedVertex == null) {
                    addVertex(e);
                }
                break;
        }
    }

    private void addVertex(MouseEvent e) throws Exception {
        Vertex v = _graph.addVertex();
        v.setAttribute(VERTEX_X, (float) _clickX / _panelWidth);
        v.setAttribute(VERTEX_Y, (float) _clickY / _panelHeight);
        v.addAttributeListener(this);
        repaint();
    }

    private void updateSelectdVertex() {

        for (Vertex v : _graph.getVertices()) {
            int vertexX = (int) ((float) v.getAttribute(GraphPanel.VERTEX_X) * _panelWidth);
            int vertexY = (int) ((float) v.getAttribute(GraphPanel.VERTEX_Y) * _panelHeight);

            if (Utils.distance(vertexX, vertexY, _clickX, _clickY) < CLOSE_DISTANCE) {
                _selectedVertex = v;
                setSelectedVertex(v);
                return;
            }
        }
        _selectedVertex = null;
        setSelectedVertex(null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        updateSelectdVertex();

        try {
            if (SwingUtilities.isLeftMouseButton(e)) {
                handleLeftMouseClicked(e);
                return;
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                handleRightMouseClicked(e);
            }
        } catch (Exception ex) {
            Utils.exception(ex);
        }
    }

    private void handleRightMouseClicked(MouseEvent e) throws Exception {
        if (_selectedVertex != null) {

            _vertexNameMenu.setText("Vertex " + _selectedVertex.getName());
            _vertexMenu.show(_canvas, _clickX, _clickY);
            return;
        }
        for (Edge edge : _graph.getEdges()) {
            Vertex v1 = edge.getFromVertex();
            Vertex v2 = edge.getToVertex();
            int vertex1X = (int) ((float) v1.getAttribute(GraphPanel.VERTEX_X) * _panelWidth);
            int vertex1Y = (int) ((float) v1.getAttribute(GraphPanel.VERTEX_Y) * _panelHeight);
            int vertex2X = (int) ((float) v2.getAttribute(GraphPanel.VERTEX_X) * _panelWidth);
            int vertex2Y = (int) ((float) v2.getAttribute(GraphPanel.VERTEX_Y) * _panelHeight);

            if (Utils.inLine(_clickX, _clickY, vertex1X, vertex1Y, vertex2X, vertex2Y)) {
                _edgeNameMenu.setText("Edge " + edge.getName());
                _edgeMenu.show(_canvas, e.getX(), e.getY());
                _selectedEdge = edge;
                return;
            }
        }
        _graphMenu.show(_canvas, e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            mousePressedInternal(e);
        } catch (Exception ex) {
            Utils.exception(ex);
        }
    }

    public void mousePressedInternal(MouseEvent e) throws Exception {
        _panelWidth = (float) _canvas.getSize().getWidth();
        _panelHeight = (float) _canvas.getSize().getHeight();

        _clickX = e.getX();
        _clickY = e.getY();

        Utils.debug("clicked " + _clickX + "," + _clickY + " canvas size " + _panelWidth + " x " + _panelHeight);
        updateSelectdVertex();

        if (_selectedVertex == null) {
            return;
        }
        if (_selectionMode == MainWindow.SelectionMode.Normal) {
            _draggedVertex = _selectedVertex;
            setSelectedVertex(_draggedVertex);
            return;
        }

        if (_selectionMode == MainWindow.SelectionMode.AddEdge) {
            _newEdgeFromVertex = _selectedVertex;
            _newEdgeToVertex = _graph.addVertex();
            _newEdgeToVertex.setAttribute(GraphPanel.VERTEX_X, (float) _clickX / _panelWidth);
            _newEdgeToVertex.setAttribute(GraphPanel.VERTEX_Y, (float) _clickY / _panelHeight);
            _newEdge = _graph.addEdge(_newEdgeFromVertex, _newEdgeToVertex);
            repaint();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        try {
            mouseReleasedInternal(e);

        } catch (Exception ex) {
            Utils.exception(ex);
        }
    }

    private void mouseReleasedInternal(MouseEvent e) throws Exception {

        _clickX = e.getX();
        _clickY = e.getY();

        _draggedVertex = null;

        if (_newEdge != null) {
            for (Vertex v : _graph.getVertices()) {
                int vertexX = (int) ((float) v.getAttribute(GraphPanel.VERTEX_X) * _panelWidth);
                int vertexY = (int) ((float) v.getAttribute(GraphPanel.VERTEX_Y) * _panelHeight);

                if (Utils.distance(vertexX, vertexY, _clickX, _clickY) < CLOSE_DISTANCE) {
                    if (!v.equals(_newEdgeToVertex)) {
                        _graph.removeVertex(_newEdgeToVertex);
                        if ((!v.equals(_newEdgeFromVertex)) && (!_graph.hasEdge(_newEdgeFromVertex.getName(), v.getName()))) {
                            _graph.addEdge(_newEdgeFromVertex, v);
                        }
                        repaint();
                        break;
                    }

                }
            }
            _newEdgeFromVertex = null;
            _newEdgeToVertex = null;
            _newEdge = null;
        }
        _selectedVertex = null;
        setSelectedVertex(null);

    }

    public void setSelectedVertex(Vertex v) {
        _canvas.setSelectedVertex(v);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            handleEvent(e);
        } catch (Exception ex) {
            Utils.exception(ex);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        _clickX = e.getX();
        _clickY = e.getY();

        if (_draggedVertex != null) {
            _draggedVertex.setAttribute(GraphPanel.VERTEX_X, (float) _clickX / _panelWidth);
            _draggedVertex.setAttribute(GraphPanel.VERTEX_Y, (float) _clickY / _panelHeight);
            repaint();
            return;
        }

        if (_newEdgeToVertex != null) {

            _newEdgeToVertex.setAttribute(GraphPanel.VERTEX_X, (float) _clickX / _panelWidth);
            _newEdgeToVertex.setAttribute(GraphPanel.VERTEX_Y, (float) _clickY / _panelHeight);
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public void setVerticesLayout(GraphPanel.VerticesLayout layout, Vertex sourceVertex) {
        if (sourceVertex != null) {
            setSelectedVertex(sourceVertex);
        }
        _canvas.setVerticesLayout(layout);
        repaint();
    }

    public void setVerticesLayout(GraphPanel.VerticesLayout layout) {
        setVerticesLayout(layout, null);
    }

    private void handleEvent(ActionEvent e) throws Exception {
        float panelWidth = (float) _canvas.getSize().getWidth();
        float panelHeight = (float) _canvas.getSize().getHeight();
        Object source = e.getSource();
        if (source.equals(_changeVertexNameMenu)) {
            String newName = JOptionPane.showInputDialog("Replace Name '" + _selectedVertex.getName() + "' With :", _selectedVertex.getName());
            _graph.setVertexName(_selectedVertex, newName);
            setTitle(newName);
            repaint();
            return;
        }
        if (source.equals(_removeVertexMenu)) {
            if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null, "Remove Vertex " + _selectedVertex.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION)) {
                _graph.removeVertex(_selectedVertex);
                repaint();
            }
            return;
        }
        if (source.equals(_removeEdgeMenu)) {
            if (JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null, "Remove Edge " + _selectedEdge.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION)) {
                _graph.removeEdge(_selectedEdge);
                repaint();
            }
            return;
        }
        if (source.equals(_changeGraphNameMenu)) {
            String newName = JOptionPane.showInputDialog("Replace Name '" + _graph.getName() + "' With :", _graph.getName());
            _graph.setName(newName);
            setTitle(newName);
            return;
        }
        if (source.equals(_graphPropertiesMenu)) {
            _mainWindow.showGraphProperties(this);
            return;
        }
        if (source.equals(_addVertexMenu)) {
            Vertex newVertex = _graph.addVertex();
            newVertex.setAttribute(GraphPanel.VERTEX_X, (float) _clickX / panelWidth);
            newVertex.setAttribute(GraphPanel.VERTEX_Y, (float) _clickY / panelHeight);
            newVertex.addAttributeListener(this);
            repaint();
            return;
        }
        if (source.equals(_verticesLayoutGridMenu)) {
            setVerticesLayout(GraphPanel.VerticesLayout.Grid);
            return;
        }
        if (source.equals(_verticesLayoutCircleMenu)) {
            setVerticesLayout(GraphPanel.VerticesLayout.Circle);
            return;
        }
        if (source.equals(_verticesLayoutBiPartiteMenu)) {
            setVerticesLayout(GraphPanel.VerticesLayout.BiPartite);
            return;
        }
        if (source.equals(_verticesLayoutTreeMenu)) {
            Object[] verticesNames = _graph.getVerticesNames().toArray();
            String vertexName = (String) JOptionPane.showInputDialog(this, "Root Vertex :", "Select Root Vertex",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    verticesNames,
                    null);
            setSelectedVertex(_graph.getVertex(vertexName));
            setVerticesLayout(GraphPanel.VerticesLayout.Tree);
            return;
        }
        if (source.equals(_verticesLayoutRandomMenu)) {
            setVerticesLayout(GraphPanel.VerticesLayout.Random);
            return;
        }
        if (source.equals(_complementMenuItem)) {
            _mainWindow.addGraphFrame(Factory.complementOf(_graph));
            return;
        }
        if (source.equals(_transposeMenuItem)) {
            _mainWindow.addGraphFrame(Factory.transposeOf(_graph));
            return;
        }
        for (List<JMenuItem> showVerticesAttributeMenuItems : _verticesAttributesMenu.values()) {
            for (JMenuItem showVerticesAttributeMenuItem : showVerticesAttributeMenuItems) {
                if (source.equals(showVerticesAttributeMenuItem)) {
                    String attribute = showVerticesAttributeMenuItem.getToolTipText();
                    boolean shown = _canvas.toggleShowVertexAttribute(attribute);
                    if (shown) {
                        showVerticesAttributes(attribute);
                    } else {
                        hideVerticesAttributes(attribute);
                    }
                    repaint();
                    return;
                }
            }
        }

        for (JMenuItem verticesSizeMenuItem : _verticesSizeMenus) {
            if (source.equals(verticesSizeMenuItem)) {
                int pointSize = new Scanner(new StringReader(verticesSizeMenuItem.getText())).nextInt();
                _canvas.setVerticesSize(pointSize);
                return;
            }
        }

        for (JMenuItem edgesSizeMenuItem : _edgesSizeMenus) {
            if (source.equals(edgesSizeMenuItem)) {
                int pointSize = new Scanner(new StringReader(edgesSizeMenuItem.getText())).nextInt();
                _canvas.setEdgesSize(pointSize);
                return;
            }
        }

        for (JMenuItem fontSizeMenuItem : _fontSizeMenus) {
            if (source.equals(fontSizeMenuItem)) {
                int fontSize = new Scanner(new StringReader(fontSizeMenuItem.getText())).nextInt();
                _canvas.setFontSize(fontSize);
                return;
            }
        }
    }

    public void increaseFontSize() {
        int fontSize = (int) _graph.getAttribute(GraphPanel.GUI_FONT_SIZE, Utils.DEFAULT_FONT_SIZE);
        int index = Utils.FONT_SIZES.indexOf(fontSize);
        if (index == Utils.FONT_SIZES.size() - 1) {
            return;
        }
        index++;
        fontSize = Utils.FONT_SIZES.get(index);
        _canvas.setFontSize(fontSize);
    }

    public void decreaseFontSize() {
        int fontSize = (int) _graph.getAttribute(GraphPanel.GUI_FONT_SIZE, Utils.DEFAULT_FONT_SIZE);
        int index = Utils.FONT_SIZES.indexOf(fontSize);
        if (index == 0 ){
            return;
        }
        index--;
        fontSize = Utils.FONT_SIZES.get(index);
        _canvas.setFontSize(fontSize);
    }

    @Override
    public void attributeRemoved(String name) {
        for (Vertex v : _graph.getVertices()) {
            if (v.hasAttribute(name)) {
                return;
            }
        }
        List<JMenuItem> menuItems = _verticesAttributesMenu.get(name);
        for (JMenuItem menuItem : menuItems) {
            _showVerticesAttributes.remove(menuItem);
            _showVerticesAttributes2.remove(menuItem);
        }
        _verticesAttributesMenu.remove(name);
    }

    @Override
    public void attributeValueChanged(String name, Object value) {
        if (name.startsWith("_")) {
            return;
        }
        if (!_verticesAttributesMenu.containsKey(name)) {
            addVerticesAttribute(name);
        }
    }

    private void addVerticesAttribute(String name) {
        if (name.startsWith("_")) {
            return;
        }

        List<JMenuItem> menuItems = new ArrayList<>();

        JMenuItem showAttributeMenuItem = new JMenuItem(name);
        showAttributeMenuItem.setToolTipText(name);
        showAttributeMenuItem.addActionListener(this);
        _showVerticesAttributes.add(showAttributeMenuItem);
        menuItems.add(showAttributeMenuItem);

        JMenuItem showAttributeMenuItem2 = new JMenuItem(name);
        showAttributeMenuItem2.setToolTipText(name);
        showAttributeMenuItem2.addActionListener(this);
        _showVerticesAttributes2.add(showAttributeMenuItem2);
        menuItems.add(showAttributeMenuItem2);

        _verticesAttributesMenu.put(name, menuItems);
    }

    private void createVerticesAttributesMenu() {
        for (Vertex v : _graph.getVertices()) {
            for (String attribute : v.attributeNames()) {
                if (attribute.startsWith("_")) {
                    continue;
                }
                if (_verticesAttributesMenu.containsKey(attribute)) {
                    continue;
                }
                addVerticesAttribute(attribute);
            }
        }
        List<String> shownVerticesAttributes = _canvas.shownVerticesAttribtes();
        for (String attribute : shownVerticesAttributes) {
            showVerticesAttributes(attribute);
        }
    }

    public void showVerticesAttributes(String attribute) {
        _canvas.showVertexAttribute(attribute);
        List<JMenuItem> menuItems = _verticesAttributesMenu.get(attribute);
        if (menuItems == null) {
            return;
        }
        for (JMenuItem showVerticesAttributeMenuItem : menuItems) {
            showVerticesAttributeMenuItem.setText(attribute + " " + Utils.VI);
        }
    }

    public void hideVerticesAttributes(String attribute) {
        _canvas.hideVertexAttribute(attribute);
        List<JMenuItem> menuItems = _verticesAttributesMenu.get(attribute);
        if (menuItems == null) {
            return;
        }
        for (JMenuItem showVerticesAttributeMenuItem : menuItems) {
            showVerticesAttributeMenuItem.setText(attribute);
        }

    }
}
