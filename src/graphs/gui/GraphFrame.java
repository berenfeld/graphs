/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

import graphs.core.Graph;
import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 *
 * @author me
 */
public class GraphFrame extends JInternalFrame {
    public GraphFrame(Graph g) {
        super(g.getName(), true, true, true, true);
        setVisible(true); 
        setLocation(0,0);
        _canvas = new GraphPanel(g);
        getContentPane().add(_canvas, BorderLayout.CENTER);
    }
    
    private GraphPanel _canvas;
}
