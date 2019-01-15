/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.algorithms.DFS;
import graphs.core.Graph;
import graphs.core.Vertex;
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
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author me
 */
public class DFSDialog extends JDialog implements ActionListener {

    public DFSDialog(MainWindow mainWindow) {
        super(mainWindow, "DFS Algoritm", true);
        _mainWindow = mainWindow;
        setSize(640, 480);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setLayout(new GridLayout(5, 1));

        initComponents();
    }

    public void setGraphFrame(GraphFrame graphFrame) {
        _graphFrame = graphFrame;
        _graph = graphFrame.getGraph();
        _vertexSelectComboBox.removeAllItems();
        for (Vertex v : _graph.getVertices()) {
            _vertexSelectComboBox.addItem(v.getName());

        }
        repaint();
    }

    private void initComponents() {
        initInputParametersPanel();
        initButtonsPanel();
    }

    private void initInputParametersPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, "Input Parameters",
                TitledBorder.LEFT, TitledBorder.TOP);
        _sourceVertexPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        _sourceVertexPanel.setBorder(titledBorder);
        _sourceVertexPanel.add(_vertexSelectLabel);
        _sourceVertexPanel.add(_vertexSelectComboBox);
        _sourceVertexPanel.add(_copyGraphCheckBox);
        _sourceVertexPanel.add(_deleteNonTreeEdges);
        _copyGraphCheckBox.setSelected(true);
        add(_sourceVertexPanel);
    }

    private void initButtonsPanel() {
        Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(lowerEtched, "Start Algorithm",
                TitledBorder.LEFT, TitledBorder.TOP);
        _buttonsPanel.setBorder(titledBorder);
        _buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        _buttonsPanel.add(_startButton);
        _startButton.addActionListener(this);
        add(_buttonsPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(_startButton)) {
            Vertex source = _graph.getVertex( (String) _vertexSelectComboBox.getSelectedItem());
            boolean copy = _copyGraphCheckBox.isSelected();
            Graph dfsGraph = DFS.dfs(_graph, source, copy);
            if ( copy ) {
                GraphFrame graphFrame = _mainWindow.addGraphFrame(dfsGraph, GraphPanel.VerticesLayout.None);
                graphFrame.showVerticesAttributes(DFS.DFS_DISCOVERY_TIME);
                graphFrame.showVerticesAttributes(DFS.DFS_FINISH_TIME);
                
                if ( _mainWindow.numberOfGraphFrames() == 2) {
                    _mainWindow.tileWindows();
                }
            } else {
                _graphFrame.repaint();
            }
            setVisible(false);
        }
    }
    
    private final MainWindow _mainWindow;
    private Graph _graph;
    private GraphFrame _graphFrame;
    private final JPanel _buttonsPanel = new JPanel();
    private final JButton _startButton = new JButton("Start");
    private final JPanel _sourceVertexPanel = new JPanel();
    private final JLabel _vertexSelectLabel = new JLabel("Select source vertex : ");
    private final JCheckBox _copyGraphCheckBox = new JCheckBox("Put result in new graph ?");
    private final JCheckBox _deleteNonTreeEdges = new JCheckBox("Delete Non-Tree Edges ?");
    private final JComboBox _vertexSelectComboBox = new JComboBox();
}
