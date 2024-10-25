package prik.parser.ast;

import prik.lib.Value;

/**
 *
 * @author Professional
 */
public class DatatypeExpression implements Expression {
    public static enum Datatypes {
        NUMBER("number"),
        STRING("string"),
        BOOLEAN("boolean");
        
        private final String name;

        private Datatypes(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    
    public final Datatypes datatype;

    public DatatypeExpression(Datatypes datatype) {
        this.datatype = datatype;
    }

    @Override
    public Value eval() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return datatype.toString();
    }
}
