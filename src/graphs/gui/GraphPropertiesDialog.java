/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.core.Graph;
import graphs.core.Vertex;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author me
 */
public class GraphPropertiesDialog extends JDialog {

    public GraphPropertiesDialog(MainWindow mainWindow) {
        super(mainWindow, "Graph Properties", true);
        _mainWindow = mainWindow;
        setSize(640, 480);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setLayout(new GridLayout(5, 1));
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        initGeneralInformationPanel();
        initConnectivityPanel();
        initDegreesPanel();
    }

    private void initGeneralInformationPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, "General Information",
                TitledBorder.LEFT, TitledBorder.TOP);
        _generalInformationPanel.setBorder(titledBorder);
        _generalInformationPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 5));
        _generalInformationPanel.add(_numberOfVertices);
        _generalInformationPanel.add(_numberOfEdges);       
        add(_generalInformationPanel);
    }

    private void initConnectivityPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, "Connectivity",
                TitledBorder.LEFT, TitledBorder.TOP);
        _connectivityPanel.setBorder(titledBorder);
        _connectivityPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 5));
        _isConnected.setEnabled(false);
        _connectivityPanel.add(_isConnected);
        _isTree.setEnabled(false);
        _connectivityPanel.add(_isTree);
        _connectivityPanel.add(_numberOfConnectedComponents);
        _connectivityPanel.add(_connectedComponents);
        _connectivityPanel.add(_diameter);
        _connectivityPanel.add(_diameterPath);

        add(_connectivityPanel);
    }
    
    private void initDegreesPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, "Degrees",
                TitledBorder.LEFT, TitledBorder.TOP);
        _degreesPanel.setBorder(titledBorder);
        _degreesPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 5));                
        _degreesPanel.add(_maxDegree);        
        _degreesPanel.add(_minDegree);
        _degreesPanel.add(_degreesList);
        _degreesPanel.add(_averageDegree);
        add(_degreesPanel);
    }

    public void setGraphFrame(GraphFrame graphFrame) {
        _graphFrame = graphFrame;
        _graph = graphFrame.getGraph();

        setTitle("Graph '" + _graph.getName() + "' Properties");
        _numberOfVertices.setText("Number of vertices : " + _graph.getNumberOfVertices());
        _numberOfEdges.setText("Number of edges : " + _graph.getNumberOfEdges());
        List<Integer> degrees = _graph.getDegrees();
        Collections.sort(degrees);
        Collections.reverse(degrees);

        Map<Vertex, Map<String, Vertex>> connectedComponents = _graph.getConnectedComponents();
        int numberOfConnectedComponents = connectedComponents.size();
        _isConnected.setSelected(numberOfConnectedComponents == 1);        
        _isTree.setSelected(false);
        if ( numberOfConnectedComponents == 1) {
            if ( _graph.getNumberOfEdges() == ( _graph.getNumberOfVertices() - 1 ) ) {
                _isTree.setSelected(true);
            }
        }
        _numberOfConnectedComponents.setText("Number of connected components : " + numberOfConnectedComponents);
        String components = " { ";
      
        for (Map<String, Vertex> component : connectedComponents.values()) {
            components += component.values().toString();
            components += ",";
        }
        components += " } ";
        _connectedComponents.setText("Connected components : " + components);
        if (_graph.diameter() != Integer.MAX_VALUE)
        {
            _diameter.setText("Diameter : " + _graph.diameter());
            _diameterPath.setText("Diameter Path: " + _graph.diameterPath());
        } else
        {
            _diameter.setText("Diameter : INFINITY");
            _diameterPath.setText("");
        }
        
        _maxDegree.setText("Maximum Degree : " + _graph.getMaximumDegree());        
        _minDegree.setText("Minimum Degree : " + _graph.getMinimumDegree());
        _degreesList.setText("Degrees List : " + degrees);
        _averageDegree.setText( "Average Degree : " + ( (float)_graph.getNumberOfEdges() * 2 ) / (float)_graph.getNumberOfVertices());
                
        repaint();
    }

    private final MainWindow _mainWindow;
    private Graph _graph;
    private GraphFrame _graphFrame;
    private final JPanel _generalInformationPanel = new JPanel();
    private final JLabel _numberOfVertices = new JLabel();
    private final JLabel _numberOfEdges = new JLabel();    
    private final JPanel _connectivityPanel = new JPanel();    
    private JCheckBox _isConnected = new JCheckBox("Connected ?");
    private JCheckBox _isTree = new JCheckBox("Tree ?");
    private JLabel _numberOfConnectedComponents = new JLabel("Connected ?");
    private JLabel _connectedComponents = new JLabel();
    private JLabel _diameter = new JLabel();
    private JLabel _diameterPath = new JLabel();
    private JPanel _degreesPanel = new JPanel();
    private JLabel _maxDegree = new JLabel();
    private JLabel _minDegree = new JLabel();
    private JLabel _averageDegree = new JLabel();
    private JLabel _degreesList = new JLabel();
}
