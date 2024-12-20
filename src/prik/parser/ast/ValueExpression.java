package prik.parser.ast;

import prik.lib.*;


/**
 *
 * @author Professional
 */
public final class ValueExpression implements Expression {
    public final Value value;
    
    public ValueExpression(Number value) {
        this.value = NumberValue.of(value);
    }
    
    public ValueExpression(String value) {
        this.value = new StringValue(value);
    }
    
    public ValueExpression(Function value) {
        this.value = new FunctionValue(value);
    }
    
    public ValueExpression(boolean value) {
        this.value = new BooleanValue(value);
    }
    
    public ValueExpression(char value) {
        this.value = new CharacterValue(value);
    }
    
    public ValueExpression(Value value) {
        this.value = value;
    }

    @Override
    public Value eval() {
        return value;
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
        return value.asString();
    }
}
