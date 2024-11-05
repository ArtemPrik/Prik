package prik.lib;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Professional
 */
public class UserLibraries {
    private static final Map<String, prik.parser.ast.Statement> functions;
    static {
        functions = new HashMap<>();
    }
    
    public static void clear() {
        functions.clear();
    }

    
    public static boolean isExists(String key) {
        return functions.containsKey(key);
    }

    public static prik.parser.ast.Statement get(String name) {
        return (prik.parser.ast.Statement) functions.get(name);
    }
    
    
    public static void set(String key, prik.parser.ast.Statement statement) {
        functions.put(key, statement);
    }
}
