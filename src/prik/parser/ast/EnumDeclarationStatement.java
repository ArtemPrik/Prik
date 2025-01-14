package prik.parser.ast;

import prik.lib.EnumValue;
import prik.lib.MapValue;
import prik.lib.Variables;

/**
 *
 * @author Professional
 */
public class EnumDeclarationStatement implements Statement {
    public final String name;
    public final MapValue enumMap;

    public EnumDeclarationStatement(String name, MapValue enumMap) {
        this.name = name;
        this.enumMap = enumMap;
    }

    @Override
    public void execute() {
        Variables.setConstant(name, new EnumValue(enumMap));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
