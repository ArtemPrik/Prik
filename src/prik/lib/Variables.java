package prik.lib;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Professional
 */
public final class Variables {
    private static final Object lock = new Object();

    private static class Scope {
        final Scope parent;
        final Map<String, Value> variables;

        Scope() {
            this(null);
        }

        Scope(Scope parent) {
            this.parent = parent;
            variables = new ConcurrentHashMap<>();
        }
    }

    private static class ScopeFindData {
        boolean isFound;
        Scope scope;
    }

    private static volatile Scope scope;
    static {
        Variables.clear();
    }

    private Variables() { }

    public static Map<String, Value> variables() {
        return scope.variables;
    }

    public static void clear() {
        scope = new Scope();
        scope.variables.clear();
    }
    
    public static void push() {
        synchronized (lock) {
            scope = new Scope(scope);
        }
    }
    
    public static void pop() {
        synchronized (lock) {
            if (scope.parent != null) {
                scope = scope.parent;
            }
        }
    }
    
    public static boolean isExists(String key) {
        synchronized (lock) {
            return findScope(key).isFound;
        }
    }
    
    public static Value get(String key) {
        synchronized (lock) {
            final ScopeFindData scopeData = findScope(key);
            if (scopeData.isFound) {
                return scopeData.scope.variables.get(key);
            }
        }
        return NumberValue.ZERO;
    }
    
    public static void set(String key, Value value) {
        synchronized (lock) {
            findScope(key).scope.variables.put(key, value);
        }
    }
    public static void setNumber(String key, NumberValue value) {
        synchronized (lock) {
            findScope(key).scope.variables.put(key, value);
        }
    }
    public static void set(String key, StringValue value) {
        synchronized (lock) {
            findScope(key).scope.variables.put(key, value);
        }
    }
    public static void set(String key, BooleanValue value) {
        synchronized (lock) {
            findScope(key).scope.variables.put(key, value);
        }
    }
    
    public static void define(String key, Value value) {
        synchronized (lock) {
            scope.variables.put(key, value);
        }
    }

    private static ScopeFindData findScope(String variable) {
        final ScopeFindData result = new ScopeFindData();

        Scope current = scope;
        do {
            if (current.variables.containsKey(variable)) {
                result.isFound = true;
                result.scope = current;
                return result;
            }
        } while ((current = current.parent) != null);
        
        result.isFound = false;
        result.scope = scope;
        return result;
    }
    
    public static void remove(String key) {
        synchronized (lock) {
            findScope(key).scope.variables.remove(key);
        }
    }
}
