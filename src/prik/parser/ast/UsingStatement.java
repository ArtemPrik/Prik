package prik.parser.ast;

import prik.lib.UserLibraries;
import prik.parser.visitors.FunctionAdder;

/**
 *
 * @author Professional
 */
public class UsingStatement implements Statement {
    private final String name;

    public UsingStatement(String name) {
        this.name = name;
    }
    
    @Override
    public void execute() {
        Statement lib = UserLibraries.get(name);
        lib.accept(new FunctionAdder());
        lib.execute();
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
        return "using " +  name;
    }
}
