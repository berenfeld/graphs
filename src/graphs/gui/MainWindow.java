/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import com.sun.java.accessibility.util.SwingEventMonitor;
import graphs.algorithms.*;
import graphs.core.*;
import graphs.utils.Utils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initMenu();

         _desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        _desktopPane.setVisible(true);

        getContentPane().add(_desktopPane, BorderLayout.CENTER);

        _toolbar.add(_selectModeButton);
        _toolbar.add(_addVertexModeButton);
        _toolbar.add(_addEdgeModeButton);
        _modeButtonGroup.add(_selectModeButton);
        _modeButtonGroup.add(_addVertexModeButton);
        _modeButtonGroup.add(_addEdgeModeButton);
        _selectModeButton.addActionListener(this);
        _addVertexModeButton.addActionListener(this);
        _addEdgeModeButton.addActionListener(this);
        getContentPane().add(_toolbar, BorderLayout.NORTH);
        
        setResizable(false);
        Dimension size = new Dimension(1280, 720);
        setSize(size.width, size.height);        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - size.width / 2, screenSize.height / 2 - size.height / 2);
        setVisible(true);       
    }

    public static enum SelectionMode {
        Normal,
        AddVertex,
        AddEdge
    };

    private SelectionMode _selectionMode = SelectionMode.Normal;
    private JToolBar _toolbar = new JToolBar();
    private JToggleButton _selectModeButton = new JToggleButton("Normal");
    private JToggleButton _addVertexModeButton = new JToggleButton("Add Vertex");
    private JToggleButton _addEdgeModeButton = new JToggleButton("Add Edge");
    private ButtonGroup _modeButtonGroup = new ButtonGroup();
    private JDesktopPane _desktopPane = new JDesktopPane();
    private JMenuBar _menuBar = new JMenuBar();
    private JMenu _graphsMenu = new JMenu("Graphs");

    private JMenu _newGraphMenu = new JMenu("New Graph");
    private JMenuItem _newGraph = new JMenuItem("New Graph");    
    
    private JMenuItem _saveGraphMenu = new JMenuItem("Save To File");
    private JMenuItem _loadGraphMenu = new JMenuItem("Load From File");

    private JMenu _algorithms = new JMenu("Algorithms");
    private JMenuItem _colorGraphAlgorithm = new JMenuItem("Color Graph");
    private JMenuItem _bfsAlgorithm = new JMenuItem("BFS");    

    private JMenu _windowsMenu = new JMenu("Windows");
    private JMenuItem _cascadeWindows = new JMenuItem("Cascade");
    private Map<GraphFrame, JMenuItem> _windowGraphMenus = new HashMap<>();
    private BFSDialog _bfsDialog = new BFSDialog(this);
    private NewGraphDialog _newGraphDialog = new NewGraphDialog(this);
    private GraphPropertiesDialog _graphPropertiesDialog = new GraphPropertiesDialog(this);

    private void initMenu() {
        _newGraph.addActionListener(this);              
        _newGraphMenu.add(_newGraph);             
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

    private GraphFrame getSelcetedFrame() {
        return (GraphFrame) _desktopPane.getSelectedFrame();
    }

    public void showGraphProperties(GraphFrame graphFrame) {
        _graphPropertiesDialog.setGraphFrame(graphFrame);
        _graphPropertiesDialog.show();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(_newGraph)) {
            newGraph();
            return;
        }         
        if (source.equals(_colorGraphAlgorithm)) {
            GraphFrame graphFrame = (GraphFrame) _desktopPane.getSelectedFrame();
            if (graphFrame == null) {
                JOptionPane.showMessageDialog(this, "Please select a graph");
                return;
            }
            Coloring.colorGraph_Greedy(graphFrame.getGraph());
            graphFrame.repaint();
            return;
        }
        if (source.equals(_colorGraphAlgorithm)) {
            GraphFrame graphFrame = (GraphFrame) _desktopPane.getSelectedFrame();
            if (graphFrame == null) {
                JOptionPane.showMessageDialog(this, "Please select a graph");
                return;
            }
            Coloring.colorGraph_Greedy(graphFrame.getGraph());
            graphFrame.repaint();
            return;
        }
        if (source.equals(_bfsAlgorithm)) {
            GraphFrame graphFrame = (GraphFrame) _desktopPane.getSelectedFrame();
            if (graphFrame == null) {
                JOptionPane.showMessageDialog(this, "Please select a graph");
                return;
            }
            _bfsDialog.setGraphFrame(graphFrame);
            _bfsDialog.setVisible(true);
            return;
        }
        if (source.equals(_saveGraphMenu)) {
            GraphFrame graphFrame = (GraphFrame) _desktopPane.getSelectedFrame();
            if (graphFrame == null) {
                JOptionPane.showMessageDialog(this, "Please select a graph");
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
                    JOptionPane.showMessageDialog(this, "Graph Save Failed");
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
                    JOptionPane.showMessageDialog(this, "Graph Open Failed");
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

        if (source.equals(_selectModeButton)) {
            _selectionMode = SelectionMode.Normal;
            Utils.info("Selected normal selection mode");
            updateSelectionMode();
            return;
        }
        if (source.equals(_addVertexModeButton)) {
            _selectionMode = SelectionMode.AddVertex;
            Utils.info("Selected adding vertex selection mode");
            updateSelectionMode();
            return;
        }
        if (source.equals(_addEdgeModeButton)) {
            _selectionMode = SelectionMode.AddEdge;
            updateSelectionMode();
            return;
        }
    }

    private void updateSelectionMode() {
        GraphFrame graphFrame = getSelcetedFrame();
        if (graphFrame != null) {
            graphFrame.setSelectionMode(_selectionMode);
        }
    }

    private void cascadeWindows() {
        JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
        int numberOfFrames = internalFrames.length;
        if (numberOfFrames == 0) {
            return;
        }
        final int FRAMES_SEPERATION = 30;
        final Dimension FRAME_SIZE = new Dimension(_desktopPane.getWidth() - FRAMES_SEPERATION * numberOfFrames,
                _desktopPane.getHeight() - FRAMES_SEPERATION * numberOfFrames);
        int i = 0;
        for (JInternalFrame internalFrame : internalFrames) {
            internalFrame.setSize(FRAME_SIZE);
            internalFrame.setLocation(i * FRAMES_SEPERATION, i * FRAMES_SEPERATION);
            selectGraphFrame((GraphFrame) internalFrame);
            i++;
        }
        repaint();
    }

    private void newGraph() {
        _newGraphDialog.setVisible(true);        
    }
    
    private void newEmptyGraph() {
        int vertices = Integer.parseInt(JOptionPane.showInputDialog(this, "How manyu vertice ?", 10));
        Graph g = Factory.buildEmptyGraph(vertices);
        addGraphFrame(g);

    }   
    
    private void selectGraphFrame(GraphFrame graphFrame) {
        try {
            graphFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    public void addGraphFrame(Graph g) {
        addGraphFrame(g, GraphPanel.VerticesLayout.Grid);
    }

    public void addGraphFrame(Graph g, GraphPanel.VerticesLayout layout) {
        GraphFrame graphFrame = new GraphFrame(this, g);
        setVisible(true);

        graphFrame.setLocation(0, 0);
        graphFrame.setSize(_desktopPane.getSize());
        graphFrame.show();

        graphFrame.addInternalFrameListener(this);
        _desktopPane.add(graphFrame);
        selectGraphFrame(graphFrame);

        if (layout != GraphPanel.VerticesLayout.None) {
            graphFrame.setVerticesLayout(layout);
        }
    }

    private static void setLookAndFeel(String lookAndFeel) {
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
        setLookAndFeel("Nimbus");
        new MainWindow();

    }

    /**
     * Invoked when a internal frame has been opened.
     *
     * @see javax.swing.JInternalFrame#show
     */
    public void internalFrameOpened(InternalFrameEvent e) {

    }

    /**
     * Invoked when an internal frame is in the process of being closed. The
     * close operation can be overridden at this point.
     *
     * @see javax.swing.JInternalFrame#setDefaultCloseOperation
     */
    public void internalFrameClosing(InternalFrameEvent e) {

    }

    /**
     * Invoked when an internal frame has been closed.
     *
     * @see javax.swing.JInternalFrame#setClosed
     */
    public void internalFrameClosed(InternalFrameEvent e) {
        GraphFrame graphFrame = (GraphFrame) e.getSource();
        JMenuItem windowMenuItem = _windowGraphMenus.get(graphFrame);
        _windowsMenu.remove(windowMenuItem);
    }

    /**
     * Invoked when an internal frame is iconified.
     *
     * @see javax.swing.JInternalFrame#setIcon
     */
    public void internalFrameIconified(InternalFrameEvent e) {

    }

    /**
     * Invoked when an internal frame is de-iconified.
     *
     * @see javax.swing.JInternalFrame#setIcon
     */
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    /**
     * Invoked when an internal frame is activated.
     *
     * @see javax.swing.JInternalFrame#setSelected
     */
    public void internalFrameActivated(InternalFrameEvent e) {

        GraphFrame graphFrame = (GraphFrame) e.getSource();
        if (!_windowGraphMenus.containsKey(graphFrame)) {
            JMenuItem windowMenuItem = new JMenuItem(graphFrame.getGraph().getName());
            _windowsMenu.add(windowMenuItem);
            _windowGraphMenus.put(graphFrame, windowMenuItem);
            windowMenuItem.addActionListener(this);
        }
        graphFrame.setSelectionMode(_selectionMode);
    }

    /**
     * Invoked when an internal frame is de-activated.
     *
     * @see javax.swing.JInternalFrame#setSelected
     */
    public void internalFrameDeactivated(InternalFrameEvent e) {

    }
}
