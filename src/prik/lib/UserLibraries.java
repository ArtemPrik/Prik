package prik.lib;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Professional
 */
public class UserLibraries {
    private static final Map<String, prik.parser.ast.Statement> libraries;
    static {
        libraries = new HashMap<>();
    }
    
    public static void clear() {
        libraries.clear();
    }

    
    public static boolean isExists(String key) {
        return libraries.containsKey(key);
    }

    public static prik.parser.ast.Statement get(String name) {
        return libraries.get(name);
    }
    
    
    public static void set(String key, prik.parser.ast.Statement statement) {
        libraries.put(key, statement);
    }
}
