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
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        _graphsMenu.add(_saveGraphMenu);
        _saveGraphMenu.addActionListener(this);
        _graphsMenu.add(_loadGraphMenu);
        _loadGraphMenu.addActionListener(this);
        
        _algorithms.add(_colorGraphAlgorithm);
        _colorGraphAlgorithm.addActionListener(this);
        _algorithms.add(_bfsAlgorithm);
        _bfsAlgorithm.addActionListener(this);
        
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
            GraphFrame graphFrame = (GraphFrame) _desktopPane.getSelectedFrame();
            if (graphFrame == null) {
                JOptionPane.showMessageDialog(this, "Please select a graph" );
                return;
            }
            Coloring.colorGraph_Greedy(graphFrame.getGraph());
            graphFrame.repaint();
            return;
        }
        if (source.equals(_colorGraphAlgorithm)) {
            GraphFrame graphFrame = (GraphFrame) _desktopPane.getSelectedFrame();
            if (graphFrame == null) {
                JOptionPane.showMessageDialog(this, "Please select a graph" );
                return;
            }
            Coloring.colorGraph_Greedy(graphFrame.getGraph());
            graphFrame.repaint();
            return;
        }
        if (source.equals(_bfsAlgorithm)) {
            GraphFrame graphFrame = (GraphFrame) _desktopPane.getSelectedFrame();
            if (graphFrame == null) {
                JOptionPane.showMessageDialog(this, "Please select a graph" );
                return;
            }
            BFS.bfs(graphFrame.getGraph());
            graphFrame.repaint();
            return;
        }
        if (source.equals(_saveGraphMenu)) {
            GraphFrame graphFrame = (GraphFrame) _desktopPane.getSelectedFrame();
            if (graphFrame == null) {
                JOptionPane.showMessageDialog(this, "Please select a graph" );
                return;
            }
            final JFileChooser fc = new JFileChooser("/home/me/new_site/graphs");
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(graphFrame.getGraph());
                    oos.close();
                } catch (Exception ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Graph Save Failed" );
                    return;
                }			
            }
            return;
        }
        if (source.equals(_loadGraphMenu)) {
            
            final JFileChooser fc = new JFileChooser("/home/me/new_site/graphs");
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                Graph graph = null;
                try {
                    FileInputStream fis = new FileInputStream(fc.getSelectedFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    graph = (Graph) ois.readObject();
                    ois.close();                    
                } catch (Exception ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Graph Open Failed" );
                    return;
                }
                addGraphFrame(graph);
            }
            return;
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
        int vertices;
        try {
            vertices = Integer.parseInt(JOptionPane.showInputDialog("How manyu vertice ?"));
        } catch (NumberFormatException ex ) {
            JOptionPane.showMessageDialog(this, "Illegal number of vertices");
            return;
        }
        if ( vertices < 0 ) {
            JOptionPane.showMessageDialog(this, "Illegal number of vertices");
            return;
        }
        Graph g = Factory.buildRandomGraph(vertices, 0.2);
        addGraphFrame(g);
    }

    private void addGraphFrame(Graph g) {
        GraphFrame graphFrame = new GraphFrame(g);

        _desktopPane.add(graphFrame);
        graphFrame.setSize(_desktopPane.getSize());
        graphFrame.setLocation(0, 0);                
        _desktopPane.setSelectedFrame(graphFrame);
    }    
    
    private JDesktopPane _desktopPane = new JDesktopPane();
    private JMenuBar _menuBar = new JMenuBar();
    private JMenu _graphsMenu = new JMenu("Graphs");
    
    private JMenu _newGraphMenu = new JMenu("New Graph");
    private JMenuItem _newEmptyGraph = new JMenuItem("Empty Graph");
    private JMenuItem _newRandomGraph = new JMenuItem("Random Graph");

    private JMenuItem _saveGraphMenu = new JMenuItem("Save To File");
    private JMenuItem _loadGraphMenu = new JMenuItem("Load From File");
    
    private JMenu _algorithms = new JMenu("Algorithms");
    private JMenuItem _colorGraphAlgorithm = new JMenuItem("Color Graph");
    private JMenuItem _bfsAlgorithm = new JMenuItem("BFS");
    
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
