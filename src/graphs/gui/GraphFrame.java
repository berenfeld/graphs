/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.algorithms.Factory;
import graphs.core.*;
import graphs.utils.Utils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author me
 */
public class GraphFrame extends JInternalFrame implements MouseListener, ActionListener, MouseMotionListener {

    public GraphFrame(Graph graph) {
        super(graph.getName(), true, true, true, true);
        _graph = graph;
        setVisible(true);
        setLocation(0, 0);
        _canvas = new GraphPanel(graph);
        getContentPane().add(_canvas, BorderLayout.CENTER);

        _canvas.addMouseListener(this);
        _canvas.addMouseMotionListener(this);
        initMenus();
    }

    private void initMenus() {
        _graphNameMenu.setEnabled(false);
        _graphNameMenu.setText(_graph.getName());
        _graphNameMenu.setEnabled(false);
        _graphMenu.add(_graphNameMenu);
        _graphMenu.addSeparator();
        _graphMenu.add(_changeGraphNameMenu);
        _changeGraphNameMenu.addActionListener(this);
        _graphMenu.add(_addVertexMenu);
        _addVertexMenu.addActionListener(this);        
        _graphMenu.add(_verticesSizeMenu);
        for (int i = 3; i < 10; i++) {
            JMenuItem verticesSizeMenuItem = new JMenuItem(i + " Points");
            _verticesSizeMenus.add(verticesSizeMenuItem);
            verticesSizeMenuItem.addActionListener(this);
            _verticesSizeMenu.add(verticesSizeMenuItem);
        }
        _verticesLayoutMenu.add(_verticesLayoutGridMenu);
        _verticesLayoutGridMenu.addActionListener(this);
        _verticesLayoutMenu.add(_verticesLayoutCircleMenu);
        _verticesLayoutCircleMenu.addActionListener(this);
        _graphMenu.add(_verticesLayoutMenu);

        _vertexNameMenu.setEnabled(false);
        _vertexMenu.add(_vertexNameMenu);
        _vertexMenu.addSeparator();
        _vertexMenu.add(_changeVertexNameMenu);        
        _changeVertexNameMenu.addActionListener(this);
        _vertexMenu.add(_removeVertexMenu);
        _removeVertexMenu.addActionListener(this);
        
    }

    public Graph getGraph() {
        return _graph;
    }
    private Graph _graph;
    private JPopupMenu _graphMenu = new JPopupMenu();
    private JPopupMenu _vertexMenu = new JPopupMenu();
    private JMenuItem _graphNameMenu = new JMenuItem();
    private JMenuItem _vertexNameMenu = new JMenuItem();
    private Vertex _selectedVertex;
    private JMenuItem _changeGraphNameMenu = new JMenuItem("Change Name");
    private JMenuItem _addVertexMenu = new JMenuItem("Add Vertex");
    private JMenuItem _changeVertexNameMenu = new JMenuItem("Change Name");
    private JMenuItem _removeVertexMenu = new JMenuItem("Remove");
    private JMenu _verticesSizeMenu = new JMenu("Vertices Size");
    private JMenu _verticesLayoutMenu = new JMenu("Vertices Layout");
    private JMenuItem _verticesLayoutGridMenu = new JMenuItem("Grid");
    private JMenuItem _verticesLayoutCircleMenu = new JMenuItem("Circle");
    private List<JMenuItem> _verticesSizeMenus = new ArrayList<JMenuItem>();

    private GraphPanel _canvas;
    private int _clickX, _clickY;
    private Vertex _draggedVertex;
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (! SwingUtilities.isRightMouseButton(e) ) {
            return;
        }
        _clickX = e.getX();
        _clickY = e.getY();
        try {
            handleMouseClicked(e);
        } catch (Exception ex ) {
            Logger.getLogger(GraphFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void handleMouseClicked(MouseEvent e ) throws Exception 
    {
        Object source = e.getSource();
        
        if (source.equals(_canvas)) {
            
            double panelWidth = _canvas.getSize().getWidth();
            double panelHeight = _canvas.getSize().getHeight();
            
            for (Vertex v : _graph.getVertices()) {
                int vertexX = (int)((double) v.getAttribute(GraphPanel.VERTEX_X) * panelWidth);
                int vertexY = (int)((double) v.getAttribute(GraphPanel.VERTEX_Y) * panelHeight);
                                
                if ( Utils.distance( vertexX, vertexY, _clickX, _clickY ) < 5 ) {
                    _vertexNameMenu.setText(v.getName());
                    _vertexMenu.show(_canvas, e.getX(), e.getY());
                    _selectedVertex = v;
                    return;
                }
                
            }                        
            _graphMenu.show(_canvas, e.getX(), e.getY());
            return;
        }
         
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Object source = e.getSource();
        double panelWidth = _canvas.getSize().getWidth();
        double panelHeight = _canvas.getSize().getHeight();
        if (source.equals(_canvas)) {            
            for (Vertex v : _graph.getVertices()) {
                int vertexX = (int)((double) v.getAttribute(GraphPanel.VERTEX_X) * panelWidth);
                int vertexY = (int)((double) v.getAttribute(GraphPanel.VERTEX_Y) * panelHeight);
                
                if (    ( Math.abs(vertexX - e.getX()) < 10 ) &&
                        ( Math.abs(vertexY - e.getY()) < 10 ) ) {
                    _draggedVertex = v;
                    return;
                }
                
            }
            return;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        _draggedVertex = null;
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
        } catch (Exception ex ) {
            
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {    
        if ( _draggedVertex != null) {
            double panelWidth = _canvas.getSize().getWidth();
            double panelHeight = _canvas.getSize().getHeight();
            
            double fromX = (double) _draggedVertex.getAttribute(GraphPanel.VERTEX_X) * panelWidth;
            double fromY = (double) _draggedVertex.getAttribute(GraphPanel.VERTEX_Y) * panelHeight;
            
            double toX = (double)e.getX() / panelWidth;
            double toY = (double)e.getY() / panelHeight ;
            
            _draggedVertex.setAttribute( GraphPanel.VERTEX_X, toX ) ;
            _draggedVertex.setAttribute( GraphPanel.VERTEX_Y, toY ) ;
            repaint();
        }
        
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {}
    
    private void  handleEvent(ActionEvent e) throws Exception 
    {
        double panelWidth = _canvas.getSize().getWidth();
        double panelHeight = _canvas.getSize().getHeight();
        Object source = e.getSource();
        if (source.equals(_changeVertexNameMenu)) {
            String newName = JOptionPane.showInputDialog("Replace Name '" + _selectedVertex.getName() + "' With :");
            _graph.setVertexName(_selectedVertex, newName);
            setTitle(newName);
            repaint();
            return;
        } 
         if (source.equals(_removeVertexMenu)) {
            if ( JOptionPane.YES_NO_OPTION == JOptionPane.showConfirmDialog(null, "Remove vertex " + _selectedVertex.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION ) )
            {
                _graph.removeVertex(_selectedVertex);       
                repaint();
            }
            return;
        } 
        if (source.equals(_changeGraphNameMenu)) {
            String newName = JOptionPane.showInputDialog("Replace Name '" + _graph.getName() + "' With :");
            _graph.setName(newName);
            setTitle(newName);
            return;
        } 
        if (source.equals(_addVertexMenu)) {                        
            Vertex newVertex = _graph.addVertex();
            newVertex.setAttribute(GraphPanel.VERTEX_X, (double)_clickX / panelWidth);
            newVertex.setAttribute(GraphPanel.VERTEX_Y, (double)_clickY / panelHeight );
            
            repaint();
            return;
        }
        if (source.equals(_verticesLayoutGridMenu)) {
            _canvas.setVerticesLayout(GraphPanel.VerticesLayout.Grid);
            return;
        }
        if (source.equals(_verticesLayoutCircleMenu)) {
            _canvas.setVerticesLayout(GraphPanel.VerticesLayout.Circle);
            return;
        }
        for (JMenuItem verticesSizeMenuItem : _verticesSizeMenus) {
            if (source.equals(verticesSizeMenuItem)) {
                int pointSize = new Scanner(new StringReader(verticesSizeMenuItem.getText())).nextInt();
                _canvas.setVerticesSize(pointSize);
                return;
            }
        }
         
    }
}
