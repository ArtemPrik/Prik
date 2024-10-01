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
    public final Collection<String> modules;
    
    public UseStatement(Collection<String> modules) {
        this.modules = modules;
    }

    @Override
    public void execute() {
        eval();
    }
    
    @Override
    public Value eval() {
        super.interruptionCheck();
        for (String module : modules) {
            ModuleLoader.loadAndUse(module);
        }
        return NumberValue.ZERO;
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
        return "use " + String.join(", ", modules);
    }
}
