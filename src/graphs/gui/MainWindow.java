/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import com.sun.java.accessibility.util.SwingEventMonitor;
import graphs.algorithms.*;
import graphs.core.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 *
 * @author ranb
 */
public class MainWindow extends JFrame implements ActionListener, InternalFrameListener {

    public MainWindow() {
        super("Graphs Theory");
        setSize(new Dimension(1280, 720));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initMenu();
        _desktopPane.setVisible(true);
        setContentPane(_desktopPane);

        setVisible(true);
        _desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
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
        
        _windowsMenu.add(_cascadeWindows);
        _cascadeWindows.addActionListener(this);
        _menuBar.add(_graphsMenu);
        _menuBar.add(_algorithms);
        _menuBar.add(_windowsMenu);
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
        if (source.equals(_cascadeWindows)) {
            cascadeWindows();
            return;
        }

        for (GraphFrame graphFrame : _windowGraphMenus.keySet()) {
            if (_windowGraphMenus.get(graphFrame).equals(source)) {
                selectGraphFrame(graphFrame);
                return;
            }
        }
        
    }

    private void cascadeWindows() {
        JInternalFrame[] internalFrames = _desktopPane.getAllFrames();        
        int numberOfFrames = internalFrames.length;
        if ( numberOfFrames == 0 ) {
            return;
        }
        final int FRAMES_SEPERATION = 30;        
        final Dimension FRAME_SIZE = new Dimension(_desktopPane.getWidth() - FRAMES_SEPERATION * numberOfFrames,
                 _desktopPane.getHeight() - FRAMES_SEPERATION * numberOfFrames );
        int i = 0;
        for (JInternalFrame internalFrame : internalFrames) {            
            internalFrame.setSize( FRAME_SIZE );
            internalFrame.setLocation(i * FRAMES_SEPERATION, i * FRAMES_SEPERATION );   
            selectGraphFrame((GraphFrame)internalFrame);
            i ++;
        }
        repaint();
    }
    private void newEmptyGraph() {
        int vertices = Integer.parseInt(JOptionPane.showInputDialog("How manyu vertice ?"));
        Graph g = Factory.buildEmptyGraph(vertices);

        
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

    private void selectGraphFrame(GraphFrame graphFrame)
    {
        try {
            graphFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }
    private void addGraphFrame(Graph g) {
        GraphFrame graphFrame = new GraphFrame(g);
        graphFrame.setSize(_desktopPane.getSize());
        graphFrame.setLocation(0, 0);        
        graphFrame.show();
        
        graphFrame.addInternalFrameListener(this);
        _desktopPane.add(graphFrame);                        
        selectGraphFrame(graphFrame);
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
    
    private JMenu _windowsMenu = new JMenu("Windows");
    private JMenuItem _cascadeWindows = new JMenuItem("Cascade");
    private Map<GraphFrame, JMenuItem> _windowGraphMenus = new HashMap<>();
    
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
    
    /**
     * Invoked when a internal frame has been opened.
     * @see javax.swing.JInternalFrame#show
     */
    public void internalFrameOpened(InternalFrameEvent e)
    {
        
    }

    /**
     * Invoked when an internal frame is in the process of being closed.
     * The close operation can be overridden at this point.
     * @see javax.swing.JInternalFrame#setDefaultCloseOperation
     */
    public void internalFrameClosing(InternalFrameEvent e)
    {
        
    }

    /**
     * Invoked when an internal frame has been closed.
     * @see javax.swing.JInternalFrame#setClosed
     */
    public void internalFrameClosed(InternalFrameEvent e)
    {
        GraphFrame graphFrame = (GraphFrame) e.getSource();        
        JMenuItem windowMenuItem = _windowGraphMenus.get(graphFrame);
        _windowsMenu.remove(windowMenuItem);        
    }

    /**
     * Invoked when an internal frame is iconified.
     * @see javax.swing.JInternalFrame#setIcon
     */
    public void internalFrameIconified(InternalFrameEvent e)
    {
        
    }

    /**
     * Invoked when an internal frame is de-iconified.
     * @see javax.swing.JInternalFrame#setIcon
     */
    public void internalFrameDeiconified(InternalFrameEvent e)
    {
    }

    /**
     * Invoked when an internal frame is activated.
     * @see javax.swing.JInternalFrame#setSelected
     */
    public void internalFrameActivated(InternalFrameEvent e)
    {
        
        GraphFrame graphFrame = (GraphFrame) e.getSource();
        if (! _windowGraphMenus.containsKey(graphFrame)) {
            JMenuItem windowMenuItem = new JMenuItem(graphFrame.getGraph().getName());        
            _windowsMenu.add(windowMenuItem);
            _windowGraphMenus.put(graphFrame, windowMenuItem );
            windowMenuItem.addActionListener(this);
        }
    }

    /**
     * Invoked when an internal frame is de-activated.
     * @see javax.swing.JInternalFrame#setSelected
     */
    public void internalFrameDeactivated(InternalFrameEvent e)
    {
        
    }
}
