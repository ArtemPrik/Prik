package prik.parser.ast;

/**
 *
 * @author Professional
 */
public final class ContinueStatement extends RuntimeException implements Statement {
    @Override
    public void execute() {
        throw this;
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
        return "continue";
    }
}
