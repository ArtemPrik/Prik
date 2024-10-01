package prik.parser.ast;

import java.util.Collection;
import prik.lib.ModuleLoader;
import prik.lib.NumberValue;
import prik.lib.Value;
import prik.lib.modules.Module;

/**
 *
 * @author Professional
 */
public final class UseStatement extends InterruptableNode implements Statement {
    public static final String PACKAGE = "prik.lib.modules.";
    public final Expression expression;
    
    public UseStatement(Expression expression) {
        this.expression = expression;
    }
    
//    public String name;
//
//    public UseStatement(String name) {
//        this.name = name;
//        this.expression = null;
//    }
    
    @Override
    public void execute() {
        try {
//            Module module = ModuleLoader.load(PACKAGE + expression.eval().asString());
            Module module = ModuleLoader.load(expression.eval().asString());
            module.init();
//            else ModuleLoader.loadAndUse(PACKAGE + name);
        } catch (Exception ex) { }
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
        return visitor.visit(this, t);
    }

    @Override
    public String toString() {
        return "use \"" + expression.eval() + "\"";
    }
}
