package prik.lib;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Professional
 */
public final class Variables {
    private static final Stack<Map<String, Value>> stack;
    private static Map<String, Value> variables;
    private static Map<String, StringValue> stringVariables;
    private static Map<String, NumberValue> numberVariables;
    private static Map<String, BooleanValue> booleanVariables;
    private static Map<String, Value> constants;
    
    static {
        stack = new Stack<>();
        variables = new ConcurrentHashMap<>();
        constants = new ConcurrentHashMap<>();
    }
    
    public static void push() {
        stack.push(new ConcurrentHashMap<>(variables));
    }
    
    public static void pop() {
        variables = stack.pop();
    }
    
    public static boolean isExists(String key) {
        return variables.containsKey(key) || constants.containsKey(key);
    }
    
    public static boolean isConstant(String key) {
        return constants.containsKey(key);
    }
    
    public static Value get(String key) {
        if (!isExists(key)) return NumberValue.ZERO;
        return variables.get(key);
    }
    
    public static void set(String key, Value value) {
        variables.put(key, value);
    }
    
    public static Value getConstant(String key) {
        if (!isExists(key)) return NumberValue.ZERO;
        return constants.get(key);
    }
    
    public static void setConstant(String key, Value value) {
        constants.put(key, value);
    }
    
    public static void define(String key, Value value) {
        variables.put(key, value);
    }
    
    public static void remove(String key) {
        variables.remove(key);
    }
    
    public static void clear() {
        variables.clear();
    }
}
