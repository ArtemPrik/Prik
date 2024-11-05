package prik.parser.ast;

import java.util.List;
import prik.lib.UserLibraries;

/**
 *
 * @author Professional
 */
public class LibDeclarationStatement implements Statement {
    private final String name;
    public final Statement statement;

    public LibDeclarationStatement(String name, Statement statement) {
        this.name = name;
        this.statement = statement;
    }
    
    @Override
    public void execute() {
        UserLibraries.set(name, statement);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String toString() {
        return "lib " + name + " {" + statement + '}';
    }
}
