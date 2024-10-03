package prik.lib;

import java.util.List;
import prik.exceptions.ArgumentsMismatchException;
import prik.parser.ast.Argument;
import prik.parser.ast.ReturnStatement;
import prik.parser.ast.Statement;

/**
 *
 * @author Professional
 */
public class UserDefinedFunction implements Function {
//    private final List<String> arguments;
    private final prik.parser.ast.Arguments arguments;
    private final Statement body;
    
    public UserDefinedFunction(prik.parser.ast.Arguments argNames, Statement body) {
        this.arguments = argNames;
        this.body = body;
    }
    
    public int getArgsCount() {
        return arguments.size();
    }
    
    public String getArgsName(int index) {
        if (index < 0 || index >= getArgsCount()) return "";
        return arguments.get(index).getName();
    }

    @Override
    public Value execute(Value... args) {
        final int size = args.length;
        final int requiredArgsCount = arguments.getRequiredArgumentsCount();
        if (size < requiredArgsCount) {
            throw new ArgumentsMismatchException(String.format("Arguments count mismatch. %d < %d", size, requiredArgsCount));
        }
        final int totalArgsCount = getArgsCount();
        if (size > totalArgsCount) {
            throw new ArgumentsMismatchException(String.format("Arguments count mismatch. %d > %d", size, totalArgsCount));
        }
        
        try {
            for (int i = size; i < totalArgsCount; i++) {
                final Argument arg = arguments.get(i);
                Variables.set(arg.getName(), arg.getValueExpr().eval());
            }
            body.execute();
            return NumberValue.ZERO;
        } catch (ReturnStatement rt) {
            return rt.getResult();
        }
    }
}
