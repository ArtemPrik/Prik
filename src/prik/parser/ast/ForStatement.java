package prik.parser.ast;

import java.util.Iterator;


/**
 *
 * @author Professional
 */
public final class ForStatement extends InterruptableNode implements Statement {
    public final Statement initialization;
    public final Expression termination;
    public final Statement increment;
    public final Statement statement;

    public ForStatement(Statement initialization, Expression termination, Statement increment, Statement block) {
        this.initialization = initialization;
        this.termination = termination;
        this.increment = increment;
        this.statement = block;
    }

    @Override
    public void execute() {
        super.interruptionCheck();
        for (initialization.execute(); termination.eval().asNumber() != 0; increment.execute()) {
            try {
                statement.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                // continue;
            }
        }
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }

    @Override
    public String toString() {
        return "for " + initialization + ", " + termination + ", " + increment + " " + statement;
    }
}
