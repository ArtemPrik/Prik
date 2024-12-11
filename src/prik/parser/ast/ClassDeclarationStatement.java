package prik.parser.ast;

import java.util.ArrayList;
import java.util.List;
import prik.lib.ClassDeclarations;

/**
 *
 * @author Professional
 */
public final class ClassDeclarationStatement implements Statement {
    public final String name;
    public final List<FunctionDefineStatement> methods;
    public final List<Statement> fields;
    
    public ClassDeclarationStatement(String name) {
        this.name = name;
        methods = new ArrayList<>();
        fields = new ArrayList<>();
    }
    
    public void addField(Statement expr) {
        fields.add(expr);
    }

    public void addMethod(FunctionDefineStatement statement) {
        methods.add(statement);
    }

    @Override
    public void execute() {
        ClassDeclarations.set(name, this);
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
        return visitor.visit(this, t);
    }

    @Override
    public String toString() {
        return String.format("class %s {%s  %s}", name, fields, methods);
    }
}
