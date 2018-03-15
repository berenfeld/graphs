/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.algorithms.BFS;
import graphs.algorithms.Factory;
import graphs.core.Graph;
import graphs.core.Vertex;
import graphs.utils.Utils;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author me
 */
public class NewGraphDialog extends JDialog implements ActionListener {

    public NewGraphDialog(MainWindow mainWindow) {
        super(mainWindow, "New graph", true);
        _mainWindow = mainWindow;
        setSize(640, 480);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setLayout(new GridLayout(5, 1));

        initComponents();
    }



    private void initComponents() {
        initGeneralInformationPanel();
        initBiPartiteGraphPanel();
        initButtonsPanel();
    }

    private static final String EMPTY_GRAPH = "Empty graph";
    private static final String COMPLETE_GRAPH = "Complete graph";   
    private static final String BIPARTITE_GRAPH = "Bi-Partite graph";
    
    private void initGeneralInformationPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, "Input Parameters",
                TitledBorder.LEFT, TitledBorder.TOP);
        _generalInformationPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        _generalInformationPanel.setBorder(titledBorder);
        _generalInformationPanel.add(_numberOfVertices);
        _generalInformationPanel.add(_numberOfVerticesComboBox);
        _generalInformationPanel.add(_graphTypeComboBox);
        
        for (int i=0;i<10;i++) {
            _numberOfVerticesComboBox.addItem(i);
        }
        for (int i=10;i<30;i+=5) {
            _numberOfVerticesComboBox.addItem(i);
        }
        _numberOfVerticesComboBox.setSelectedItem(10);
        _graphTypeComboBox.addItem(EMPTY_GRAPH);
        _graphTypeComboBox.addItem(COMPLETE_GRAPH);
        _graphTypeComboBox.addItem(BIPARTITE_GRAPH);
        _graphTypeComboBox.addActionListener(this);
        add(_generalInformationPanel);
    }

    private void initButtonsPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, "Create graph",
                TitledBorder.LEFT, TitledBorder.TOP);
        _buttonsPanel.setBorder(titledBorder);
        _buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        _buttonsPanel.add(_createGraphButton);
        _createGraphButton.addActionListener(this);
        add(_buttonsPanel);
    }

    private void initBiPartiteGraphPanel()
    {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, BIPARTITE_GRAPH,
                TitledBorder.LEFT, TitledBorder.TOP);
        for (int i=0;i<20;i++) {
            _bipartiteGraphFullnessComboBox.addItem((i*5) + "%");
        }
        _bipartiteGraphFullnessComboBox.setSelectedItem("50%");
        _bipartiteGraphPanel.setBorder(titledBorder);
        _bipartiteGraphPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        _bipartiteGraphPanel.add(_bipartiteGraphFullness);
        _bipartiteGraphPanel.add(_bipartiteGraphFullnessComboBox);
        _bipartiteGraphPanel.setVisible(true);
        
    }
    
    void createEmptyGraph( int vertices ) {        
        Graph graph = Factory.buildEmptyGraph(vertices);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.Circle);
        setVisible(false);
    }
    
    void createCompleteGraph( int vertices ) {        
        Graph graph = Factory.buildCompleteGraph(vertices);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.Circle);
        setVisible(false);
    }
    
    void createBiPartiteGraph( int vertices ) {        
        String densityStr = (String)_bipartiteGraphFullnessComboBox.getSelectedItem();
        int densityPercent = Integer.parseInt(densityStr.substring(0, densityStr.length() - 1));
        
        Graph graph = Factory.buildRandomBiPartiteGraph(vertices / 2, vertices / 2, (double)densityPercent / 100);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.BiPartite);
        setVisible(false);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        if (o.equals(_graphTypeComboBox)) {
            remove(_bipartiteGraphPanel);
            Object graphType = _graphTypeComboBox.getSelectedItem();
            if (BIPARTITE_GRAPH.equals(graphType)) {                
                add(_bipartiteGraphPanel);        
            }
            repaint();
        }
        else if (o.equals(_createGraphButton)) {
            int vertices = (Integer)_numberOfVerticesComboBox.getSelectedItem();            
            Object graphType = _graphTypeComboBox.getSelectedItem();
            
            if (EMPTY_GRAPH.equals(graphType)) {
                createEmptyGraph(vertices);
            }
            else if (COMPLETE_GRAPH.equals(graphType)) {
                createCompleteGraph(vertices);
            }
            else if (BIPARTITE_GRAPH.equals(graphType)) {
                createBiPartiteGraph(vertices);
            }
        }
    }
    
    private MainWindow _mainWindow;
    private Graph _graph;
    private GraphFrame _graphFrame;
    private JPanel _buttonsPanel = new JPanel();
    private JButton _createGraphButton = new JButton("Create graph");
    private JPanel _generalInformationPanel = new JPanel();
    private JLabel _numberOfVertices = new JLabel("Number of vertices : ");    
    private JComboBox _numberOfVerticesComboBox = new JComboBox();
    private JComboBox _graphTypeComboBox = new JComboBox();
    
    private JPanel _bipartiteGraphPanel = new JPanel();
    private JLabel _bipartiteGraphFullness = new JLabel("Graph fullness");    
    private JComboBox _bipartiteGraphFullnessComboBox = new JComboBox();
}
