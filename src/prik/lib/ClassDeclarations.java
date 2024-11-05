package prik.lib;

import java.util.HashMap;
import java.util.Map;
import prik.exceptions.UnknownFunctionException;
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
        declarations = new HashMap<>();
        
        ClassDeclarationStatement clas = new ClassDeclarationStatement("Integer");
        clas.addField(new AssignmentExpression(null, new VariableExpression("MAX_VALUE"), new ValueExpression(Integer.MAX_VALUE)));
        
        declarations.put("Integer", clas);
    }

    private ClassDeclarations() { }

    public static void clear() {
        declarations.clear();
    }

    public static Map<String, ClassDeclarationStatement> getAll() {
        return declarations;
    }
    
    public static boolean isExists(String key) {
        return declarations.containsKey(key);
    }
    
    public static ClassDeclarationStatement get(String key) {
        if (!isExists(key)) throw new UnknownFunctionException(key);
        return declarations.get(key);
    }
    
    public static void set(String key, ClassDeclarationStatement classDef) {
        declarations.put(key, classDef);
    }
}
