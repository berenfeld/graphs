/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.algorithms.BFS;
import graphs.core.Graph;
import graphs.core.Vertex;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
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

        initComponents();
    }

    private void initComponents() {
        initGeneralInformationPanel();
        initConnectivityPanel();
    }

    private void initGeneralInformationPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, "General Information",
                TitledBorder.LEFT, TitledBorder.TOP);
        _generalInformationPanel.setBorder(titledBorder);
        _generalInformationPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 5));
        _generalInformationPanel.add(_numberOfVertices);
        _generalInformationPanel.add(_numberOfEdges);
        _generalInformationPanel.add(_minimumVertexDegree);
        _generalInformationPanel.add(_maximumVertexDegree);
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

        add(_connectivityPanel);
    }

    public void setGraphFrame(GraphFrame graphFrame) {
        _graphFrame = graphFrame;
        _graph = graphFrame.getGraph();

        setTitle("Graph '" + _graph.getName() + "' Properties");
        _numberOfVertices.setText("Number of vertices : " + _graph.getNumberOfVertices());
        _numberOfEdges.setText("Number of edges : " + _graph.getNumberOfEdges());
        List<Integer> degrees = _graph.getDegrees();
        Collections.sort(degrees);
        _minimumVertexDegree.setText("Minimum vertex degree : " + degrees.get(0));
        _maximumVertexDegree.setText("Maximum vertex degree : " + degrees.get(degrees.size() - 1));

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
        repaint();
    }

    private MainWindow _mainWindow;
    private Graph _graph;
    private GraphFrame _graphFrame;
    private JPanel _generalInformationPanel = new JPanel();
    private JLabel _numberOfVertices = new JLabel();
    private JLabel _numberOfEdges = new JLabel();
    private JLabel _minimumVertexDegree = new JLabel();
    private JLabel _maximumVertexDegree = new JLabel();
    private JPanel _connectivityPanel = new JPanel();
    private JCheckBox _isConnected = new JCheckBox("Connected ?");
    private JCheckBox _isTree = new JCheckBox("Tree ?");
    private JLabel _numberOfConnectedComponents = new JLabel("Connected ?");
    private JLabel _connectedComponents = new JLabel();
}
