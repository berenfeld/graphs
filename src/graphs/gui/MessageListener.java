/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.gui;

/**
 *
 * @author ranb
 */
public class MessageListener {
    
    public static boolean ENABLED = false;
    
    public static void message(String text)
    {
        if (! ENABLED ) {
            return;
        }
        MainWindow.instance().addMessage(text);
    }
}
