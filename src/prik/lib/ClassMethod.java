package prik.lib;

import prik.parser.ast.Statement;

/**
 *
 * @author Professional
 */
public final class ClassMethod extends UserDefinedFunction {
   
    public final ClassInstanceValue classInstance;
    
    public ClassMethod(prik.parser.ast.Arguments arguments, Statement body, ClassInstanceValue classInstance) {
        super(arguments, body);
        this.classInstance = classInstance;
    }
    
    @Override
    public Value execute(Value[] values) {
        Variables.push();
        Variables.define("this", classInstance.getThisMap());
        
        try {
            return super.execute(values);
        } finally {
            Variables.pop();
        }
    }
}
