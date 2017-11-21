/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.algorithms.*;
import graphs.core.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author ranb
 */
public class MainWindow extends JFrame implements ActionListener {

    public MainWindow() {
        super("Graphs Theory");
        setSize(new Dimension(1280, 720));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initMenu();
        _desktopPane.setVisible(true);
        setContentPane(_desktopPane);

        setVisible(true);
    }

    private void initMenu() {
        _newEmptyGraph.addActionListener(this);
        _newRandomGraph.addActionListener(this);
        _newGraphMenu.add(_newEmptyGraph);
        _newGraphMenu.add(_newRandomGraph);
        _graphsMenu.add(_newGraphMenu);
        
        _algorithms.add(_colorGraphAlgorithm);
        _colorGraphAlgorithm.addActionListener(this);
        
        _menuBar.add(_graphsMenu);
        _menuBar.add(_algorithms);
        setJMenuBar(_menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(_newEmptyGraph)) {
            newEmptyGraph();
            return;
        } 
        if (source.equals(_newRandomGraph)) {
            newRandomGraph();
            return;
        }
        if (source.equals(_colorGraphAlgorithm)) {
            if (_currentGraph == null) {
                JOptionPane.showMessageDialog(this, "Please select a graph" );
                return;
            }
            Coloring.colorGraph_Greedy(_currentGraph);
        }
    }

    private void newEmptyGraph() {
        int vertices = Integer.parseInt(JOptionPane.showInputDialog("How manyu vertice ?"));
        Graph g = Factory.buildEmptyGraph(vertices);
        GraphFrame graphFrame = new GraphFrame(g);

        _desktopPane.add(graphFrame);
        addGraphFrame(g);

    }

    private void newRandomGraph() {
        int vertices = Integer.parseInt(JOptionPane.showInputDialog("How manyu vertice ?"));
        Graph g = Factory.buildRandomGraph(vertices, 0.2);
        addGraphFrame(g);
    }

    private void addGraphFrame(Graph g) {
        GraphFrame graphFrame = new GraphFrame(g);

        _desktopPane.add(graphFrame);
        graphFrame.setSize(_desktopPane.getSize());
        graphFrame.setLocation(0, 0);
        
        _currentGraph = g;
    }

    private Graph _currentGraph;
    
    private JDesktopPane _desktopPane = new JDesktopPane();
    private JMenuBar _menuBar = new JMenuBar();
    private JMenu _graphsMenu = new JMenu("Graphs");
    

    private JMenu _newGraphMenu = new JMenu("New Graph");
    private JMenuItem _newEmptyGraph = new JMenuItem("Empty Graph");
    private JMenuItem _newRandomGraph = new JMenuItem("Random Graph");

    private JMenu _algorithms = new JMenu("Algorithms");
    private JMenuItem _colorGraphAlgorithm = new JMenuItem("Color Graph");
    
    private static void setLookAndFeel(String lookAndFeel)
    {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                
                if (lookAndFeel.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        
    }
    public static void main(String[] args) {

        setLookAndFeel( "Nimbus" );
        new MainWindow();

    }
}
