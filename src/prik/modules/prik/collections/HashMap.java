package prik.modules.prik.collections;

import prik.lib.ClassDeclarations;
import prik.lib.Functions;
import prik.modules.Module;
import static prik.modules.prik.collections.mapFunctions.mapFunction;
import prik.parser.ast.ClassDeclarationStatement;
import prik.parser.ast.FunctionDefineStatement;

/**
 *
 * @author Professional
 */
public class HashMap implements Module {
    @Override
    public void init() {
        Functions.set("hashMap", mapFunction(java.util.HashMap::new));
//        ClassDeclarationStatement hashMap = new ClassDeclarationStatement("HashMap");
//        hashMap.addMethod(new FunctionDefineStatement());
//        ClassDeclarations.set("HashMap", hashMap);
    }
}
