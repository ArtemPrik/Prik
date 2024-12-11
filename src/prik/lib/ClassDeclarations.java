package prik.lib;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import prik.exceptions.UnknownException;
import prik.parser.ast.AssignmentExpression;
import prik.parser.ast.ClassDeclarationStatement;
import prik.parser.ast.ValueExpression;
import prik.parser.ast.VariableExpression;

/**
 *
 * @author Professional
 */
public final class ClassDeclarations {
    private static final Map<String, ClassDeclarationStatement> declarations;
    static {
        declarations = new ConcurrentHashMap<>();
    }

    private ClassDeclarations() { }

    public static void clear() {
        declarations.clear();
    }

    public static Map<String, ClassDeclarationStatement> getAll() {
        return declarations;
    }
    
    public static ClassDeclarationStatement get(String key) {
        return declarations.get(key);
    }
    
    public static void set(String key, ClassDeclarationStatement classDef) {
        declarations.put(key, classDef);
    }
}
