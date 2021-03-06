/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.algorithms.*;
import graphs.core.*;
import graphs.utils.Utils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static MainWindow _mainWindow;

    public static MainWindow instance() {
        if (_mainWindow == null) {
            _mainWindow = new MainWindow();
        }
        return _mainWindow;
    }

    private MainWindow() {
        super("Graphs Theory");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initMenu();

        _desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        _desktopPane.setVisible(true);

        getContentPane().add(_desktopPane, BorderLayout.CENTER);

        _toolbar.add(_selectModeButton);
        _toolbar.add(_addVertexModeButton);
        _toolbar.add(_addEdgeModeButton);
        _toolbar.addSeparator();

        _toolbar.add(_fontSizeIncrease);
        _toolbar.add(_fontSizeDecrease);
        _fontSizeIncrease.addActionListener(this);
        _fontSizeDecrease.addActionListener(this);
        _toolbar.addSeparator();

        _modeButtonGroup.add(_selectModeButton);
        _modeButtonGroup.add(_addVertexModeButton);
        _modeButtonGroup.add(_addEdgeModeButton);
        _selectModeButton.addActionListener(this);
        _addVertexModeButton.addActionListener(this);
        _addEdgeModeButton.addActionListener(this);
        getContentPane().add(_toolbar, BorderLayout.NORTH);

        setResizable(false);
        setVisible(true);
        setBounds(0, 0, 1440, 1080);

        getContentPane().add(_messagesConsoleScroll, BorderLayout.SOUTH);
        _messagesConsoleScroll.setPreferredSize(new Dimension(getWidth(), getHeight() / 5));
        _messagesConsole.setText("Messages Console");
        _messagesConsole.setEditable(false);
    }

    public void addMessage(String text) {
        _messagesConsole.setText(_messagesConsole.getText() + "\n" + text);
    }

    public static enum SelectionMode {
        Normal,
        AddVertex,
        AddEdge
    };

    private final JTextPane _messagesConsole = new JTextPane();
    private final JScrollPane _messagesConsoleScroll = new JScrollPane(_messagesConsole);
    private SelectionMode _selectionMode = SelectionMode.Normal;
    private final JToolBar _toolbar = new JToolBar();
    private final JToggleButton _selectModeButton = new JToggleButton("Normal");
    private final JToggleButton _addVertexModeButton = new JToggleButton("Add Vertex");
    private final JToggleButton _addEdgeModeButton = new JToggleButton("Add Edge");
    private final JButton _fontSizeIncrease = new JButton("Font Size +");
    private final JButton _fontSizeDecrease = new JButton("Font Size -");
    private final ButtonGroup _modeButtonGroup = new ButtonGroup();
    private final JDesktopPane _desktopPane = new JDesktopPane();
    private final JMenuBar _menuBar = new JMenuBar();
    private final JMenu _graphsMenu = new JMenu("Graphs");

    private final JMenu _newGraphMenu = new JMenu("New Graph");
    private final JMenuItem _newGraph = new JMenuItem("New Graph Dialog");
    private final JMenuItem _fromDegreesList = new JMenuItem("From Degrees Sequence");

    private final JMenuItem _saveGraphMenu = new JMenuItem("Save To File");
    private final JMenuItem _loadGraphMenu = new JMenuItem("Load From File");

    private final JMenu _algorithms = new JMenu("Algorithms");
    private final JMenuItem _colorGraphAlgorithm = new JMenuItem("Color Graph");
    private final JMenuItem _bfsAlgorithm = new JMenuItem("BFS");
    private final JMenuItem _dfsAlgorithm = new JMenuItem("DFS");

    private final JMenu _windowsMenu = new JMenu("Windows");
    private final JMenuItem _cascadeWindows = new JMenuItem("Cascade");
    private final JMenuItem _tileWindows = new JMenuItem("Tile");
    private final Map<GraphFrame, JMenuItem> _windowGraphMenus = new HashMap<>();
    private final BFSDialog _bfsDialog = new BFSDialog(this);
    private final DFSDialog _dfsDialog = new DFSDialog(this);
    private final NewGraphDialog _newGraphDialog = new NewGraphDialog(this);
    private final GraphPropertiesDialog _graphPropertiesDialog = new GraphPropertiesDialog(this);

    private void initMenu() {
        _newGraph.addActionListener(this);
        _newGraphMenu.add(_newGraph);

        _fromDegreesList.addActionListener(this);
        _newGraphMenu.add(_fromDegreesList);

        _graphsMenu.add(_newGraphMenu);

        _graphsMenu.add(_saveGraphMenu);
        _saveGraphMenu.addActionListener(this);
        _graphsMenu.add(_loadGraphMenu);
        _loadGraphMenu.addActionListener(this);

        _algorithms.add(_colorGraphAlgorithm);
        _colorGraphAlgorithm.addActionListener(this);
        _algorithms.add(_bfsAlgorithm);
        _bfsAlgorithm.addActionListener(this);
        _algorithms.add(_dfsAlgorithm);
        _dfsAlgorithm.addActionListener(this);

        _windowsMenu.add(_cascadeWindows);
        _cascadeWindows.addActionListener(this);

        _windowsMenu.add(_tileWindows);
        _tileWindows.addActionListener(this);

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
        _graphPropertiesDialog.setVisible(true);
    }

    void createGraphFromDegreesList(String degreesList) throws Exception {

        List<Integer> degreesSequence = Utils.parseList(degreesList);
        Utils.debug("parse degrees list " + degreesSequence);
        Graph graph = GraphFromDegreeSequence.fromDegreeSequence(degreesSequence);
        addGraphFrame(graph);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(_newGraph)) {
            newGraph();
            return;
        }

        if (source.equals(_loadGraphMenu)) {

            final JFileChooser fc = new JFileChooser("/home/me/new_site/graphs");
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                Graph graph;
                try {
                    FileInputStream fis = new FileInputStream(fc.getSelectedFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    graph = (Graph) ois.readObject();
                    ois.close();
                } catch (IOException | ClassNotFoundException ex) {
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
        if (source.equals(_tileWindows)) {
            tileWindows();
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
            Utils.debug("Selected adding vertex selection mode");
            updateSelectionMode();
            return;
        }
        if (source.equals(_addEdgeModeButton)) {
            _selectionMode = SelectionMode.AddEdge;
            updateSelectionMode();
            return;
        }

        GraphFrame graphFrame = getSelcetedFrame();

        if (graphFrame == null) {
            JOptionPane.showMessageDialog(this, "Please select a graph");
            return;
        }

        if (source.equals(_fromDegreesList)) {
            String degreesListStr = null;
            try {
                degreesListStr = JOptionPane.showInputDialog(this, "Please enter degrees list, seperated by comma", "3,3,3,3,3,3");
                createGraphFromDegreesList(degreesListStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to create a graph from degrees list : " + degreesListStr);
            }
            return;
        }
        if (source.equals(_colorGraphAlgorithm)) {

            Coloring.colorGraph_Greedy(graphFrame.getGraph());
            graphFrame.repaint();
            return;
        }
        if (source.equals(_colorGraphAlgorithm)) {
            Coloring.colorGraph_Greedy(graphFrame.getGraph());
            graphFrame.repaint();
            return;
        }
        if (source.equals(_bfsAlgorithm)) {
            _bfsDialog.setGraphFrame(graphFrame);
            _bfsDialog.setVisible(true);
            return;
        }
        if (source.equals(_dfsAlgorithm)) {
            _dfsDialog.setGraphFrame(graphFrame);
            _dfsDialog.setVisible(true);
            return;
        }
        
        if (source.equals(_saveGraphMenu)) {
            final JFileChooser fc = new JFileChooser("/home/me/new_site/graphs");
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(graphFrame.getGraph());
                    oos.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Graph Save Failed");
                    return;
                }
            }
            return;
        }

        if (source.equals(_fontSizeIncrease)) {
            graphFrame.increaseFontSize();
            graphFrame.repaint();
            return;
        }

        if (source.equals(_fontSizeDecrease)) {
            graphFrame.decreaseFontSize();
            graphFrame.repaint();
            return;
        }
    }

    private void updateSelectionMode() {
        GraphFrame graphFrame = getSelcetedFrame();
        if (graphFrame != null) {
            graphFrame.setSelectionMode(_selectionMode);
        }
    }

    private void resizeAndShowInternalFrame(JInternalFrame internalFrame, int x, int y, int width, int height) {
        try {
            internalFrame.setSize(width, height);
            internalFrame.setLocation(x, y);
            internalFrame.setIcon(false);
        } catch (PropertyVetoException ex) {
            Utils.exception(ex);
        }
    }

    public void cascadeWindows() {
        JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
        int numberOfFrames = internalFrames.length;
        if (numberOfFrames == 0) {
            return;
        }
        final int FRAMES_SEPERATION = 30;
        final Dimension FRAME_SIZE = new Dimension(_desktopPane.getWidth() - FRAMES_SEPERATION * (numberOfFrames - 1),
                _desktopPane.getHeight() - FRAMES_SEPERATION * (numberOfFrames - 1));
        int i = 0;
        for (JInternalFrame internalFrame : internalFrames) {
            resizeAndShowInternalFrame(internalFrame, i * FRAMES_SEPERATION, i * FRAMES_SEPERATION, FRAME_SIZE.width, FRAME_SIZE.height);
            internalFrame.moveToFront();
            i++;
        }
        selectGraphFrame((GraphFrame) internalFrames[numberOfFrames - 1]);
        repaint();
    }

    public void tileWindows() {

        JInternalFrame[] internalFrames = _desktopPane.getAllFrames();
        int numberOfFrames = internalFrames.length;
        if (numberOfFrames == 0) {
            return;
        }
        int cols = (int) Math.ceil(Math.sqrt(numberOfFrames));
        int rows = (int) Math.ceil((float) numberOfFrames / cols);

        Utils.debug("Organize " + numberOfFrames + " windows tile rows " + rows + " cols " + cols);
        int frameWidth = _desktopPane.getWidth() / cols;
        int frameHeight = _desktopPane.getHeight() / rows;

        int frameIndex = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (frameIndex >= internalFrames.length) {
                    break;
                }
                JInternalFrame internalFrame = internalFrames[frameIndex];
                resizeAndShowInternalFrame(internalFrame, j * frameWidth, i * frameHeight, frameWidth, frameHeight);
                frameIndex++;
            }
        }
        selectGraphFrame((GraphFrame) internalFrames[0]);

        repaint();
    }

    private void newGraph() {
        _newGraphDialog.showDialog();
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

    public int numberOfGraphFrames() {
        return _desktopPane.getComponentCount();
    }

    public GraphFrame addGraphFrame(Graph g) {
        GraphFrame graphFrame = new GraphFrame(this, g);
        setVisible(true);

        graphFrame.setLocation(0, 0);
        graphFrame.setSize(_desktopPane.getSize());
        graphFrame.show();

        graphFrame.addInternalFrameListener(this);
        _desktopPane.add(graphFrame);
        selectGraphFrame(graphFrame);
       
        if (_desktopPane.getAllFrames().length == 2) {
            tileWindows();
        }
        return graphFrame;
    }

    private static void setLookAndFeel(String lookAndFeel) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                if (lookAndFeel.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

    }

    public static void main(String[] args) {
        setLookAndFeel("Nimbus");
        MainWindow.instance();
        MessageListener.ENABLED = true;
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        GraphFrame graphFrame = (GraphFrame) e.getSource();
        JMenuItem windowMenuItem = _windowGraphMenus.get(graphFrame);
        _windowsMenu.remove(windowMenuItem);
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
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

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {

    }
}
