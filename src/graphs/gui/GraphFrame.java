/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.core.Graph;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;

/**
 *
 * @author me
 */
public class GraphFrame extends JInternalFrame implements MouseListener, ActionListener {

    public GraphFrame(Graph graph) {
        super(graph.getName(), true, true, true, true);
        _graph = graph;
        setVisible(true);
        setLocation(0, 0);
        _canvas = new GraphPanel(graph);
        getContentPane().add(_canvas, BorderLayout.CENTER);

        _canvas.addMouseListener(this);
        initMenus();
    }



    private void initMenus() {
        _graphNameMenu.setText(_graph.getName());
        _graphNameMenu.setEnabled(false);
        _graphMenu.add(_graphNameMenu);
        _graphMenu.addSeparator();
        _graphMenu.add(_changeNameMenu);
        _changeNameMenu.addActionListener(this);
        _graphMenu.add(_verticesSizeMenu);
        for (int i = 3; i < 10; i++) {
            JMenuItem verticesSizeMenuItem = new JMenuItem(i + " Points");
            _verticesSizeMenus.add(verticesSizeMenuItem);
            verticesSizeMenuItem.addActionListener(this);
            _verticesSizeMenu.add(verticesSizeMenuItem);
        }

    }

    private Graph _graph;
    private JPopupMenu _graphMenu = new JPopupMenu();
    private JMenuItem _graphNameMenu = new JMenuItem();
    private JMenuItem _changeNameMenu = new JMenuItem("Change Name");
    private JMenu _verticesSizeMenu = new JMenu("Vertices Size");
    private List<JMenuItem> _verticesSizeMenus = new ArrayList<JMenuItem>();

    private GraphPanel _canvas;

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source.equals(_canvas)) {
            _graphMenu.show(_canvas, e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(_changeNameMenu)) {
            String newName = JOptionPane.showInputDialog("Replace Name '" + _graph.getName() + "' With :");
            _graph.setName(newName);
            setTitle(newName);
        } else {
            for (JMenuItem verticesSizeMenuItem : _verticesSizeMenus) {
                if (source.equals(verticesSizeMenuItem)) {
                    int pointSize = new Scanner(new StringReader(verticesSizeMenuItem.getText())).nextInt();
                    _canvas.setVerticesSize(pointSize);
                    break;
                }
            }
        }
    }
}
