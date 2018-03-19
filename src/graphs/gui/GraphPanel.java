/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.algorithms.BiPartite;
import graphs.algorithms.MaximumCut;
import graphs.core.*;
import graphs.utils.Utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
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
        addComponentListener(this);
    }

    public enum VerticesLayout {
        None,
        Grid,
        Circle,
        BiPartite
    };

    private Graph _graph;
    private Vertex _selectedVertex;

    private int _verticesSize = 10;

    public static final String GUI_LAYOUT = "_GUI-Vertices-Layout";
    public static final String VERTEX_X = "_GUI-Vertex-X";
    public static final String VERTEX_Y = "_GUI-Vertex-Y";

    public void setSelectedVertex(Vertex v) {
        _selectedVertex = v;
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
        }
        repaint();
        _graph.setAttribute(GUI_LAYOUT, layout);
    }

    public void setVerticesSize(int size) {
        _verticesSize = size;
        repaint();
    }

    public void setFontSize(int size) {
        _fontSize = size;
        _regularFont = new Font("Arial", Font.PLAIN, _fontSize);
        _boldFont = new Font("Arial", Font.BOLD, _fontSize);
        repaint();
    }
    
    private void layoutVerticesGrid() {
        double panelWidth = getSize().getWidth();
        double panelHeight = getSize().getHeight();
        if ((panelWidth == 0) || (panelHeight == 0)) {
            return;
        }

        double ratio = panelWidth / panelHeight;
        double vertices = _graph.getNumberOfVertices();
        int gridWidth = (int) Math.ceil(Math.sqrt(vertices) * ratio);
        int gridHeight = (int) Math.ceil(vertices / gridWidth);

        int cellWidth = (int) (panelWidth / (gridWidth + 1));
        int cellHeight = (int) (panelHeight / (gridHeight + 1));

        int i = 0;
        for (Vertex v : _graph.getVertices()) {
            int x = (i % gridWidth) + 1;
            int y = (i / gridWidth) + 1;

            int vertexX = x * cellWidth;
            int vertexY = y * cellHeight;

            v.setAttribute(VERTEX_X, (double) vertexX / panelWidth);
            v.setAttribute(VERTEX_Y, (double) vertexY / panelHeight);
            i++;
        }

        _graph.setAttribute(GUI_LAYOUT, VerticesLayout.Grid);
    }

    public VerticesLayout getVerticesLayout() {
        return (VerticesLayout) _graph.getAttribute(GUI_LAYOUT, VerticesLayout.None);
    }

    private void layoutVerticesCircle() {
        int i = 0;
        int vertices = _graph.getNumberOfVertices();

        for (Vertex v : _graph.getVertices()) {
            double vertexX = 0.5 + 0.4 * Math.sin((i * Math.PI * 2) / vertices);
            double vertexY = 0.5 - 0.4 * Math.cos((i * Math.PI * 2) / vertices);

            v.setAttribute(VERTEX_X, vertexX);
            v.setAttribute(VERTEX_Y, vertexY);
            i++;
        }
        _graph.setAttribute(GUI_LAYOUT, VerticesLayout.Circle);

    }
    
    private void layoutVerticesBiPartite() {
        
        boolean hasSideAttribute = true;
        for (Vertex v : _graph.getVertices()) {
            if ( ! v.hasAttribute(Vertex.VERTEX_ATTRIBUTE_SIDE)) {
                hasSideAttribute = false;
            }
        }
        if ( ! hasSideAttribute ) {
            Graph maximumCut = MaximumCut.maximumCut(_graph);        
            List<Vertex> sideA = (List<Vertex>) maximumCut.getAttribute(MaximumCut.MAXIMUM_CUT_SIDE_A);
            List<Vertex> sideB = (List<Vertex>) maximumCut.getAttribute(MaximumCut.MAXIMUM_CUT_SIDE_B);
            for (Vertex v : sideA ) {
                v.setAttribute(Vertex.VERTEX_ATTRIBUTE_SIDE, 1 );                
            }
            for (Vertex v : sideB ) {
                v.setAttribute(Vertex.VERTEX_ATTRIBUTE_SIDE, 2 );                
            }
        }
        
        List<Vertex> sideA = _graph.getVerticesWith(Vertex.VERTEX_ATTRIBUTE_SIDE, 1 );
        List<Vertex> sideB = _graph.getVerticesWith(Vertex.VERTEX_ATTRIBUTE_SIDE, 2 );
        
        int aPosition = 0, bPosition = 0;
        for (Vertex v : _graph.getVertices()) {
            double vertexX, vertexY;
            if (v.getAttribute(Vertex.VERTEX_ATTRIBUTE_SIDE).equals(1))
            {
                vertexX = 0.1;        
                vertexY = 0.1 + ( aPosition * 0.8 / ( sideA.size() ) );
                aPosition++;
            }
            else
            {
                vertexX = 0.9;
                vertexY = 0.1 + ( bPosition * 0.8 / ( sideB.size() ) );
                bPosition++;
            }
            
            v.setAttribute(VERTEX_X, vertexX);
            v.setAttribute(VERTEX_Y, vertexY); 
        }
        _graph.setAttribute(GUI_LAYOUT, VerticesLayout.BiPartite);

    }

   
    
    private int _fontSize = 20;
    
    private Font _regularFont = new Font("Arial", Font.PLAIN, _fontSize);
    private Font _boldFont = new Font("Arial", Font.BOLD, _fontSize);

    private void repaintGraph(Graphics g) {

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
            int vertexX = (int) ((double) v.getAttribute(VERTEX_X) * panelWidth);
            int vertexY = (int) ((double) v.getAttribute(VERTEX_Y) * panelHeight);

            g.setColor(Utils.VERTEX_COLORS.get(v.getColor()));
            if (v.equals(_selectedVertex)) {
                g.setFont(_boldFont);
            } else {
                g.setFont(_regularFont);
            }

            int height = g.getFontMetrics().getHeight();
            Rectangle2D textRectAngle = g.getFontMetrics().getStringBounds(v.getName(), g);

            int startX = vertexX - (int) textRectAngle.getWidth() / 2;

            g.drawString(v.getName(), startX, vertexY + height);
            int line = 2;
            for (String attribute : v.attributeNames()) {
                if (attribute.startsWith("_")) {
                    continue;
                }
                String text = attribute + " = " + v.getAttribute(attribute);
                g.drawString(text, startX, vertexY + (line * height));
                line++;
            }
            g2.fill(new Ellipse2D.Double(vertexX - (_verticesSize / 2), vertexY - (_verticesSize / 2), _verticesSize, _verticesSize));

            i++;
        }

        g.setColor(Color.BLACK);
        for (Edge e : _graph.getEdges()) {
            g.setColor(Utils.VERTEX_COLORS.get(e.getColor()));
            Vertex from = e.getFromVertex();
            Vertex to = e.getToVertex();

            int fromX = (int) ((double) from.getAttribute(VERTEX_X) * panelWidth);
            int fromY = (int) ((double) from.getAttribute(VERTEX_Y) * panelHeight);
            int toX = (int) ((double) to.getAttribute(VERTEX_X) * panelWidth);
            int toY = (int) ((double) to.getAttribute(VERTEX_Y) * panelHeight);

            g.drawLine(fromX, fromY, toX, toY);
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
