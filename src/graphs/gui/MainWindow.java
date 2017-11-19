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
        _menuBar.add(_graphsMenu);

        setJMenuBar(_menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(_newEmptyGraph)) {
            newEmptyGraph();

        } else if (source.equals(_newRandomGraph)) {
            newRandomGraph();

        }
    }

    private void newEmptyGraph() {
        int vertices = Integer.parseInt(JOptionPane.showInputDialog("How manyu vertice ?"));
        Graph g = Factory.buildEmptyGraph(vertices);
        GraphFrame graphFrame = new GraphFrame(g);

        _desktopPane.add(graphFrame);
        try {
            graphFrame.setMaximum(true);
        } catch (PropertyVetoException ex) {

        }
    }

    private void newRandomGraph() {
        int vertices = Integer.parseInt(JOptionPane.showInputDialog("How manyu vertice ?"));
        Graph g = Factory.buildRandomGraph(vertices, 0.2);
        GraphFrame graphFrame = new GraphFrame(g);

        _desktopPane.add(graphFrame);
        try {
            graphFrame.setMaximum(true);
        } catch (PropertyVetoException ex) {

        }
    }
    private JDesktopPane _desktopPane = new JDesktopPane();
    private JMenuBar _menuBar = new JMenuBar();
    private JMenu _graphsMenu = new JMenu("Graphs");

    private JMenu _newGraphMenu = new JMenu("New Graph");
    private JMenuItem _newEmptyGraph = new JMenuItem("Empty Graph");
    private JMenuItem _newRandomGraph = new JMenuItem("Random Graph");

    public static void main(String[] args) {
        MainWindow window = new MainWindow();

    }
}
