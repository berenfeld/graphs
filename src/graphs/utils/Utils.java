/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.utils;

import graphs.core.Edge;
import graphs.core.Vertex;
import graphs.gui.MainWindow;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ranb
 */
public class Utils {

    public static final Map<Integer, Color> VERTEX_COLORS = new TreeMap<>();

    public static final Random RANDOM = new Random();
    static {
        VERTEX_COLORS.put(0, Color.BLACK);
        VERTEX_COLORS.put(1, Color.WHITE);
        VERTEX_COLORS.put(2, Color.GRAY);
        VERTEX_COLORS.put(3, Color.RED);
        VERTEX_COLORS.put(4, Color.BLUE);
        VERTEX_COLORS.put(5, Color.GREEN);
        VERTEX_COLORS.put(6, Color.ORANGE);
        VERTEX_COLORS.put(7, Color.MAGENTA);
        VERTEX_COLORS.put(8, Color.CYAN);
        VERTEX_COLORS.put(9, Color.YELLOW);
    }

    public static final String VI = "\u2713";
    
    public static final int getColorNumber(Color color) {
        for (int colorNumber : VERTEX_COLORS.keySet()) {
            if (VERTEX_COLORS.get(colorNumber).equals(color)) {
                return colorNumber;
            }
        }
        return -1;
    }

    public static void warning(String message) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        Logger.getLogger(caller.getClass().getName()).log(Level.WARNING, "{0}:{1} {2} {3}", new Object[]{caller.getFileName(), caller.getLineNumber(), caller.getMethodName(), message});
    }
    public static void info(String message) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        Logger.getLogger(caller.getClass().getName()).log(Level.INFO, "{0}:{1} {2} {3}", new Object[]{caller.getFileName(), caller.getLineNumber(), caller.getMethodName(), message});
    }

    public static void debug(String message) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        Logger.getLogger(caller.getClass().getName()).log(Level.FINE, "{0}:{1} {2} {3}", new Object[]{caller.getFileName(), caller.getLineNumber(), caller.getMethodName(), message});
    }
    
    public static void exception(Throwable ex) {
        ex.printStackTrace();
    }

    public static <T> T getFirst(List<T> list) {
        if (list == null) {
            return null;

        }
        return list.get(0);
    }

    public static <T> T getLast(List<T> list) {
        if (list == null) {
            return null;

        }
        return list.get(list.size() - 1);
    }

    public static String edgeName(Vertex from, Vertex to, boolean sort) {
        return edgeName(from.toString(), to.toString(), sort);
    }

    public static String edgeName(String from, String to, boolean sort) {
        if ( ! sort ) {
            return (from + "->" + to);
        }
        return from.compareTo(to) < 0 ? (from + "-" + to) : (to + "-" + from);
    }

    public static double distance(double fromX, double fromY, double toX, double toY) {
        return Math.sqrt(Math.pow(toY - fromY, 2) + Math.pow(toX - fromX, 2));
    }

    public static boolean inLine(double x, double y, double fromX, double fromY, double toX, double toY) {
        double fullDistance = distance(fromX, fromY, toX, toY);
        double fromDist = distance(x, y, fromX, fromY);
        double toDist = distance(x, y, toX, toY);

        return (fromDist + toDist) < (fullDistance + 1);
    }
    
    public static List<Integer> parseList(String listStr)
    {
        List<String> tokens = Arrays.asList(listStr.split("\\s*,\\s*"));
        List<Integer> result = new ArrayList();
        for (String token : tokens){
            result.add(0, Integer.valueOf(token));
        }
        return result;
    }
    
    public static String join(List list)
    {
        return String.join(",", list);        
    }
}
