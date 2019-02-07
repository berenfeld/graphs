/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs.utils;

import graphs.core.Edge;
import graphs.core.Vertex;
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

    public static final Map<Integer, Color> COLORS = new TreeMap<>();
    public static final Color DEFAULT_COLOR = Color.BLACK;
    public static final Random RANDOM = new Random();

    static {
        COLORS.put(0, Color.BLACK);
        COLORS.put(1, Color.WHITE);
        COLORS.put(2, Color.GRAY);
        COLORS.put(3, Color.RED);
        COLORS.put(4, Color.BLUE);
        COLORS.put(5, Color.GREEN);
        COLORS.put(6, Color.ORANGE);
        COLORS.put(7, Color.MAGENTA);
        COLORS.put(8, Color.CYAN);
        COLORS.put(9, Color.YELLOW);
    }

    public static final List<Integer> FONT_SIZES = Arrays.asList(new Integer[]{10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30});
    public static final int DEFAULT_FONT_SIZE = 14;
    public static final ArrayList<String> DEFAULT_VERTICES_ATTRIBUTES_SHOWN = new ArrayList(Arrays.asList(new String[]{Vertex.VERTEX_ATTRIBUTE_NAME, Vertex.VERTEX_ATTRIBUTE_COLOR}));
    public static final ArrayList<String> DEFAULT_EDGES_ATTRIBUTES_SHOWN = new ArrayList(Arrays.asList(new String[]{Edge.EDGE_ATTRIBUTE_COLOR}));

    public static final String VI = "\u2713";

    public static final int getColorNumber(Color color) {
        for (int colorNumber : COLORS.keySet()) {
            if (COLORS.get(colorNumber).equals(color)) {
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
        StackTraceElement caller = ex.getStackTrace()[2];        
        Logger.getLogger(caller.getClass().getName()).log(Level.SEVERE, "{0}:{1} {2} {3} {4}", new Object[]{caller.getFileName(), caller.getLineNumber(), caller.getMethodName(), ex.getMessage(), ex.getStackTrace()});
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
        if (!sort) {
            return (from + "->" + to);
        }
        return from.compareTo(to) < 0 ? (from + "-" + to) : (to + "-" + from);
    }

    public static float distance(float fromX, float fromY, float toX, float toY) {
        return (float) Math.sqrt(Math.pow(toY - fromY, 2) + Math.pow(toX - fromX, 2));
    }

    public static boolean inLine(float x, float y, float fromX, float fromY, float toX, float toY) {
        float fullDistance = distance(fromX, fromY, toX, toY);
        float fromDist = distance(x, y, fromX, fromY);
        float toDist = distance(x, y, toX, toY);

        return (fromDist + toDist) < (fullDistance + 1);
    }

    public static List<Integer> parseList(String listStr) {
        List<String> tokens = Arrays.asList(listStr.split("\\s*,\\s*"));
        List<Integer> result = new ArrayList();
        for (String token : tokens) {
            result.add(0, Integer.valueOf(token));
        }
        return result;
    }

    public static String join(List list) {
        return String.join(",", list);
    }
}
