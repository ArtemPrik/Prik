package prik.parser.ast;

import prik.lib.MapValue;
import prik.lib.StringValue;
import prik.lib.Variables;

/**
 *
 * @author Professional
 */
public class TryCatchStatement implements Statement {
    public final Statement tryStatement;
    public final Statement catchStatement;

    public TryCatchStatement(Statement tryStatement, Statement catchStatement) {
        this.tryStatement = tryStatement;
        this.catchStatement = catchStatement;
    }
    
    @Override
    public void execute() {
        try {
            tryStatement.execute();
        } catch (Exception ex) {
            catchStatement.execute();
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
//        return visitor.visit(this, input);
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public String toString() {
        return "try {" + tryStatement + "} catch {" + catchStatement + "}";
    }
}
