/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.utils;

import graphs.core.Edge;
import graphs.core.Vertex;
import java.util.List;

/**
 *
 * @author ranb
 */
public class Utils {
    public static<T> T getFirst(List<T> list) {
        if (list == null) {
            return null;
            
        }
        return list.get(0);
    }
    
    
    public static<T> T getLast(List<T> list) {
        if (list == null) {
            return null;
            
        }
        return list.get(list.size()-1);
    }

    public static String edgeName(Vertex from, Vertex to) {
        return edgeName(from.toString(), to.toString());
    }

    public static String edgeName(String from, String to) {
        return from.compareTo(to) < 0 ? (from + "-" + to) : (to + "-" + from);
    }
    
    public static double distance(double fromX, double fromY, double toX, double toY) {
        return Math.sqrt(Math.pow(toY - fromY,2 ) + Math.pow(toX - fromX, 2));
    }
      
    
    public static boolean inLine(double x, double y, double fromX, double fromY, double toX, double toY) {
        double fullDistance = distance(fromX, fromY, toX, toY);
        double fromDist = distance(x,y,fromX, fromY);
        double toDist = distance(x,y,toX, toY);
        
        return ( fromDist + toDist ) < ( fullDistance + 1 );
    }
}
