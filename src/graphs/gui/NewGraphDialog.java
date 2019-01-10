/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.algorithms.Factory;
import graphs.core.Graph;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
        setMinimumSize(new Dimension(640, 480));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setLayout(new GridLayout(6, 1));
        initComponents();
    }
    
    private void initComponents() {
        initGeneralInformationPanel();
        initButtonsPanel();
        _extraInformationPanel.setLayout(new CardLayout(0, 0));
        _extraInformationPanel.add(_emptyPanel, EMPTY_GRAPH);
        add(_extraInformationPanel);
        initRandomGraphPanel();
        initBiPartiteGraphPanel();
    }

    private static final String EMPTY_GRAPH = "Empty graph";
    private static final String COMPLETE_GRAPH = "Complete graph";
    private static final String CYCLE_GRAPH = "Cycle graph";
    private static final String LINE_GRAPH = "Line graph";
    private static final String RANDOM_GRAPH = "Random graph";
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
        _generalInformationPanel.add(_directedCheckBox);
        _directedCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
        
        for (int i = 0; i < 10; i++) {
            _numberOfVerticesComboBox.addItem(i);
        }
        for (int i = 10; i < 30; i += 5) {
            _numberOfVerticesComboBox.addItem(i);
        }
        _numberOfVerticesComboBox.setSelectedItem(10);
        _graphTypeComboBox.addItem(EMPTY_GRAPH);
        _graphTypeComboBox.addItem(COMPLETE_GRAPH);
        _graphTypeComboBox.addItem(CYCLE_GRAPH);
        _graphTypeComboBox.addItem(LINE_GRAPH);
        _graphTypeComboBox.addItem(RANDOM_GRAPH);
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

    private void initRandomGraphPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, RANDOM_GRAPH,
                TitledBorder.LEFT, TitledBorder.TOP);

        _randomGraphPanel.setBorder(titledBorder);
        _randomGraphPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        _randomGraphPanel.add(_randomGraphFullness);
        _randomGraphPanel.add(_randomGraphFullnessComboBox);

        for (int i = 0; i < 20; i++) {
            _randomGraphFullnessComboBox.addItem((i * 5) + "%");
        }
        _randomGraphFullnessComboBox.setSelectedItem("20%");
        _extraInformationPanel.add(_randomGraphPanel, RANDOM_GRAPH);
       
    }

    private void initBiPartiteGraphPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, BIPARTITE_GRAPH,
                TitledBorder.LEFT, TitledBorder.TOP);

        for (int i = 0; i < 10; i++) {
            _bipartiteGraphSideAVerticesComboBox.addItem(i);
        }
        for (int i = 10; i < 30; i += 5) {
            _bipartiteGraphSideAVerticesComboBox.addItem(i);
        }
        _bipartiteGraphSideAVerticesComboBox.setSelectedItem(10);
        _bipartiteGraphPanel.setBorder(titledBorder);
        _bipartiteGraphPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        _bipartiteGraphPanel.add(_bipartiteGraphSideAVertices);
        _bipartiteGraphPanel.add(_bipartiteGraphSideAVerticesComboBox);
        _bipartiteGraphPanel.add(_bipartiteGraphFullness);
        _bipartiteGraphPanel.add(_bipartiteGraphFullnessComboBox);

        for (int i = 0; i < 20; i++) {
            _bipartiteGraphFullnessComboBox.addItem((i * 5) + "%");
        }
        _bipartiteGraphFullnessComboBox.setSelectedItem("20%");
        _extraInformationPanel.add(_bipartiteGraphPanel, BIPARTITE_GRAPH);
        
    }

    void createEmptyGraph(int vertices, boolean directed) {
        Graph graph = Factory.buildEmptyGraph(vertices, directed);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.Circle);
        setVisible(false);
    }

    void createCompleteGraph(int vertices, boolean directed) {
        Graph graph = Factory.buildCompleteGraph(vertices, directed);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.Circle);
        setVisible(false);
    }

    void createCycleGraph(int vertices, boolean directed) {
        Graph graph = Factory.buildCycleGraph(vertices, directed);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.Circle);
        setVisible(false);
    }

    void createLineGraph(int vertices, boolean directed) {
        Graph graph = Factory.buildLineGraph(vertices, directed);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.Circle);
        setVisible(false);
    }
    
    void createRandomGraph(int vertices, boolean directed) {
        String densityStr = (String) _randomGraphFullnessComboBox.getSelectedItem();
        int densityPercent = Integer.parseInt(densityStr.substring(0, densityStr.length() - 1));

        Graph graph = Factory.buildRandomGraph(vertices, directed, (double) densityPercent / 100);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.Random);
        setVisible(false);
    }

    void createBiPartiteGraph(int vertices) {
        String densityStr = (String) _bipartiteGraphFullnessComboBox.getSelectedItem();
        int densityPercent = Integer.parseInt(densityStr.substring(0, densityStr.length() - 1));
        int verticesSideA = (Integer) _bipartiteGraphSideAVerticesComboBox.getSelectedItem();
        verticesSideA = Math.min(verticesSideA, vertices);
        Graph graph = Factory.buildRandomBiPartiteGraph(verticesSideA, vertices - verticesSideA, (double) densityPercent / 100);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.BiPartite);
        setVisible(false);
    }

    
      
    public void showDialog()
    {
        
        pack();        
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(_graphTypeComboBox)) {
           ((CardLayout)_extraInformationPanel.getLayout()).show(_extraInformationPanel, EMPTY_GRAPH);
            Object graphType = _graphTypeComboBox.getSelectedItem();
            if (BIPARTITE_GRAPH.equals(graphType)) {
                ((CardLayout)_extraInformationPanel.getLayout()).show(_extraInformationPanel, BIPARTITE_GRAPH);
            } else if (RANDOM_GRAPH.equals(graphType)) {
                ((CardLayout)_extraInformationPanel.getLayout()).show(_extraInformationPanel, RANDOM_GRAPH);
            }          
            repaint();
        } else if (o.equals(_createGraphButton)) {
            int vertices = (Integer) _numberOfVerticesComboBox.getSelectedItem();
            Object graphType = _graphTypeComboBox.getSelectedItem();

            boolean directed = _directedCheckBox.isSelected();
            
            if (EMPTY_GRAPH.equals(graphType)) {
                createEmptyGraph(vertices, directed);
            } else if (COMPLETE_GRAPH.equals(graphType)) {
                createCompleteGraph(vertices, directed);
            } else if (CYCLE_GRAPH.equals(graphType)) {
                createCycleGraph(vertices, directed);
            } else if (LINE_GRAPH.equals(graphType)) {
                createLineGraph(vertices, directed);
            } else if (RANDOM_GRAPH.equals(graphType)) {
                createRandomGraph(vertices, directed);
            } else if (BIPARTITE_GRAPH.equals(graphType)) {
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

    private JPanel _extraInformationPanel = new JPanel();
    private JPanel _emptyPanel = new JPanel();
    
    private JPanel _randomGraphPanel = new JPanel();
    private JLabel _randomGraphFullness = new JLabel("Graph fullness");
    private JComboBox _randomGraphFullnessComboBox = new JComboBox();

    private JPanel _bipartiteGraphPanel = new JPanel();
    private JLabel _bipartiteGraphSideAVertices = new JLabel("Vertices on first side");
    private JComboBox _bipartiteGraphSideAVerticesComboBox = new JComboBox();
    private JLabel _bipartiteGraphFullness = new JLabel("Graph fullness");
    private JComboBox _bipartiteGraphFullnessComboBox = new JComboBox();
    
    private JCheckBox _directedCheckBox = new JCheckBox("Directed ?");
}
