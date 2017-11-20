/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.core.*;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 *
 * @author me
 */
public class GraphPanel extends JPanel {

    public GraphPanel(Graph graph) {
        super();
        _graph = graph;

    }

    private Graph _graph;
    private int _verticesSize = 5;
    
    public void setVerticesSize(int size) {
        _verticesSize = size;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        
        layoutVertices(g);

    }

    private void layoutVertices(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        
        double panelWidth = getSize().getWidth();
        double panelHeight = getSize().getHeight();

        double vertices = _graph.getNumberOfVertices();
        int gridWidth = (int) Math.max(1, Math.sqrt(vertices * 2));
        int gridHeight = (int) Math.ceil(vertices / gridWidth);

        int cellWidth = (int) (panelWidth / (gridWidth + 1));
        int cellHeight = (int) (panelHeight / (gridHeight + 1));

        int i = 0;
        for (Vertex v : _graph.getVertices()) {
            int x = (i % gridWidth) + 1;
            int y = (i / gridWidth) + 1;

            int vertexX = x * cellWidth;
            int vertexY = y * cellHeight;
            
            v.setAttribute("panel-x", vertexX);
            v.setAttribute("panel-y", vertexY);
            g.drawString(v.getName(), vertexX, vertexY );
            g2.fill(new Ellipse2D.Double(vertexX, vertexY, _verticesSize, _verticesSize ) );
                             
            i++;
        }

        for (Edge e : _graph.getEdges()) {
            Vertex from = e.getFromVertex();
            Vertex to = e.getToVertex();
            
            int fromX = (int) from.getAttribute("panel-x");
            int fromY = (int) from.getAttribute("panel-y");
            int toX = (int) to.getAttribute("panel-x");
            int toY = (int) to.getAttribute("panel-y");
            
            g.drawLine(fromX, fromY, toX, toY);
        }
    }
    

}
