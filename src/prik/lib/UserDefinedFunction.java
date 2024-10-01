package prik.lib;

import java.util.List;
import prik.parser.ast.ReturnStatement;
import prik.parser.ast.Statement;

/**
 *
 * @author Professional
 */
public class UserDefinedFunction implements Function {
    private final List<String> arguments;
    private final Statement body;
    
    public UserDefinedFunction(List<String> argNames, Statement body) {
        this.arguments = argNames;
        this.body = body;
    }
    
    public int getArgsCount() {
        return arguments.size();
    }
    
    public String getArgsName(int index) {
        if (index < 0 || index >= getArgsCount()) return "";
        return arguments.get(index);
    }

    @Override
    public Value execute(Value... args) {
        try {
            body.execute();
            return NumberValue.ZERO;
        } catch (ReturnStatement rt) {
            return rt.getResult();
        }
    }
}
