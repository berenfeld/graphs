/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.algorithms.BFS;
import graphs.algorithms.Factory;
import graphs.algorithms.GraphFromDegreeSequence;
import graphs.core.Graph;
import graphs.core.Vertex;
import graphs.utils.Utils;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import javax.swing.JTextField;
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
        initGraphFromDegreesListPanel();
    }

    private static final String EMPTY_GRAPH = "Empty graph";
    private static final String COMPLETE_GRAPH = "Complete graph";
    private static final String CYCLE_GRAPH = "Cycle graph";
    private static final String RANDOM_GRAPH = "Random graph";
    private static final String BIPARTITE_GRAPH = "Bi-Partite graph";
    private static final String GRAPH_FROM_DEGREES_LIST = "Graph from degrees list";

    private void initGeneralInformationPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, "Input Parameters",
                TitledBorder.LEFT, TitledBorder.TOP);
        _generalInformationPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        _generalInformationPanel.setBorder(titledBorder);
        _generalInformationPanel.add(_numberOfVertices);
        _generalInformationPanel.add(_numberOfVerticesComboBox);
        _generalInformationPanel.add(_graphTypeComboBox);

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
        _graphTypeComboBox.addItem(RANDOM_GRAPH);
        _graphTypeComboBox.addItem(BIPARTITE_GRAPH);
        _graphTypeComboBox.addItem(GRAPH_FROM_DEGREES_LIST);
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
        _randomGraphFullnessComboBox.setSelectedItem("50%");
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
        _bipartiteGraphFullnessComboBox.setSelectedItem("50%");
        _extraInformationPanel.add(_bipartiteGraphPanel, BIPARTITE_GRAPH);
        
    }

    private void initGraphFromDegreesListPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, GRAPH_FROM_DEGREES_LIST,
                TitledBorder.LEFT, TitledBorder.TOP);
        _graphFromDegreesListPanel.setBorder(titledBorder);
        _graphFromDegreesListPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        _graphFromDegreesListPanel.add(_graphFromDegreesListLabel);
        _graphFromDegreesListPanel.add(_graphFromDegreesListTextField);
         _extraInformationPanel.add(_graphFromDegreesListPanel, GRAPH_FROM_DEGREES_LIST);
        
    }

    void createEmptyGraph(int vertices) {
        Graph graph = Factory.buildEmptyGraph(vertices);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.Circle);
        setVisible(false);
    }

    void createCompleteGraph(int vertices) {
        Graph graph = Factory.buildCompleteGraph(vertices);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.Circle);
        setVisible(false);
    }

    void createCycleGraph(int vertices) {
        Graph graph = Factory.buildCycleGraph(vertices);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.Circle);
        setVisible(false);
    }

    void createRandomGraph(int vertices) {
        String densityStr = (String) _randomGraphFullnessComboBox.getSelectedItem();
        int densityPercent = Integer.parseInt(densityStr.substring(0, densityStr.length() - 1));

        Graph graph = Factory.buildRandomGraph(vertices, (double) densityPercent / 100);
        _mainWindow.addGraphFrame(graph, GraphPanel.VerticesLayout.Circle);
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

    void createGraphFromDegreesList(int vertices) {
        String degreesList = _graphFromDegreesListTextField.getText();
        try {
            List<String> list = Arrays.asList(degreesList.split("\\s*,\\s*"));
            List<Integer> degreesSequence = new ArrayList<>();
            for (String s : list) {
                degreesSequence.add(Integer.parseInt(s));
            }        
            Graph graph = GraphFromDegreeSequence.fromDegreeSequence(degreesSequence);
            _mainWindow.addGraphFrame(graph );
            setVisible(false);
        } catch ( Exception ex) {
            Utils.exception(ex);
            JOptionPane.showMessageDialog(this, "Could not build graph from degree sequence "+ degreesList);          
        }
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
            } else if (GRAPH_FROM_DEGREES_LIST.equals(graphType)) {
                ((CardLayout)_extraInformationPanel.getLayout()).show(_extraInformationPanel, GRAPH_FROM_DEGREES_LIST);
            }            
            repaint();
        } else if (o.equals(_createGraphButton)) {
            int vertices = (Integer) _numberOfVerticesComboBox.getSelectedItem();
            Object graphType = _graphTypeComboBox.getSelectedItem();

            if (EMPTY_GRAPH.equals(graphType)) {
                createEmptyGraph(vertices);
            } else if (COMPLETE_GRAPH.equals(graphType)) {
                createCompleteGraph(vertices);
            } else if (CYCLE_GRAPH.equals(graphType)) {
                createCycleGraph(vertices);
            } else if (RANDOM_GRAPH.equals(graphType)) {
                createRandomGraph(vertices);
            } else if (BIPARTITE_GRAPH.equals(graphType)) {
                createBiPartiteGraph(vertices);
            } else if (GRAPH_FROM_DEGREES_LIST.equals(graphType)) {
                createGraphFromDegreesList(vertices);
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

    private JPanel _graphFromDegreesListPanel = new JPanel();
    private JLabel _graphFromDegreesListLabel = new JLabel("Enter degrees list");
    private JTextField _graphFromDegreesListTextField = new JTextField(20);

}
