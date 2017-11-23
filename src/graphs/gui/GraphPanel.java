/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.core.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Ellipse2D;
import java.util.Map;
import java.util.TreeMap;
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
        Circle
    };
        
    private Graph _graph;
    private int _verticesSize = 5;
    
    
    public static final String GUI_LAYOUT = "GUI-Vertices-Layout";
    public static final String VERTEX_X = "GUI-Vertex-X";
    public static final String VERTEX_Y = "GUI-Vertex-Y";
    
    
    public void setVerticesLayout( VerticesLayout layout ) {
        switch (layout) {
            case Grid:    
                layoutVerticesGrid();
            break;
            case Circle:    
                layoutVerticesCircle();
            break;
        }
        repaint();
        _graph.setAttribute(GUI_LAYOUT, layout);
    }
    
    public void setVerticesSize(int size) {
        _verticesSize = size;
        repaint();
    }
    
    private void layoutVerticesGrid() {
        double panelWidth = getSize().getWidth();
        double panelHeight = getSize().getHeight();
        if ( ( panelWidth == 0 ) || ( panelHeight == 0 ) ) {
            return;
        }
    
        double ratio = panelWidth / panelHeight;
        double vertices = _graph.getNumberOfVertices();
        int gridWidth = (int) Math.ceil(Math.sqrt( vertices) * ratio);
        int gridHeight = (int) Math.ceil(vertices / gridWidth);

        int cellWidth = (int) (panelWidth / (gridWidth + 1));
        int cellHeight = (int) (panelHeight / (gridHeight + 1));

        int i = 0;
        for (Vertex v : _graph.getVertices()) {
            int x = (i % gridWidth) + 1;
            int y = (i / gridWidth) + 1;

            int vertexX = x * cellWidth;
            int vertexY = y * cellHeight;
            
            v.setAttribute(VERTEX_X, (double)vertexX  / panelWidth);
            v.setAttribute(VERTEX_Y, (double)vertexY / panelHeight);
            i++;
        }
        
        _graph.setAttribute(GUI_LAYOUT, VerticesLayout.Grid );
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
            
            v.setAttribute(VERTEX_X, vertexX );
            v.setAttribute(VERTEX_Y, vertexY );
            i++;
        }
        _graph.setAttribute(GUI_LAYOUT, VerticesLayout.Circle );
        
    }
    
    public static final Map<Integer, Color> VERTEX_COLORS = new TreeMap<>();
    static {
        VERTEX_COLORS.put(0, Color.BLACK);
        VERTEX_COLORS.put(1, Color.RED);
        VERTEX_COLORS.put(2, Color.BLUE);
        VERTEX_COLORS.put(3, Color.GREEN);        
        VERTEX_COLORS.put(4, Color.GRAY);
        VERTEX_COLORS.put(5, Color.ORANGE);
        VERTEX_COLORS.put(6, Color.WHITE);
        VERTEX_COLORS.put(7, Color.MAGENTA);
        VERTEX_COLORS.put(8, Color.CYAN);
        VERTEX_COLORS.put(9, Color.YELLOW);
    }
    
    private void repaintGraph(Graphics g) {

        g.setColor(Color.WHITE);
        g.fillRect(0,0,getWidth(), getHeight());
        g.setFont(new Font("Arial", Font.PLAIN, 16 ) );
        
        if ( getVerticesLayout() == VerticesLayout.None) {
            layoutVerticesGrid();
        }
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
            int vertexX = (int)((double)v.getAttribute(VERTEX_X ) * panelWidth );
            int vertexY = (int)((double)v.getAttribute(VERTEX_Y ) * panelHeight );
            
            g.setColor(VERTEX_COLORS.get(v.getColor() ));
            g.drawString(v.getName(), vertexX, vertexY );
            g2.fill(new Ellipse2D.Double(vertexX, vertexY, _verticesSize, _verticesSize ) );
                             
            i++;
        }

        g.setColor(Color.BLACK);
        for (Edge e : _graph.getEdges()) {
            g.setColor(VERTEX_COLORS.get(e.getColor() ));
            Vertex from = e.getFromVertex();
            Vertex to = e.getToVertex();
            
            int fromX = (int)((double)from.getAttribute(VERTEX_X ) * panelWidth );
            int fromY = (int)((double)from.getAttribute(VERTEX_Y ) * panelHeight );
            int toX = (int)((double)to.getAttribute(VERTEX_X ) * panelWidth );
            int toY = (int)((double)to.getAttribute(VERTEX_Y ) * panelHeight );
            
            g.drawLine(fromX, fromY, toX, toY);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        repaintGraph(g);
    }
    
    @Override
    public void componentResized(ComponentEvent e)
    {
        switch (getVerticesLayout()) {
            case Grid:
                layoutVerticesGrid();             
            break;
        }
    }

    @Override
    public void componentMoved(ComponentEvent e)
    {}
    
    @Override
    public void componentShown(ComponentEvent e)
    {}
    
    @Override
    public void componentHidden(ComponentEvent e)
    {}
}
