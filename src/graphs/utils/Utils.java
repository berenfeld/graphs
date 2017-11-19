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
    
    public static String vertexName(int index) {
        return String.format("v%s", index);
    }
        
}
