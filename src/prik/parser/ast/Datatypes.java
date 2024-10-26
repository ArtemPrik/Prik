package prik.parser.ast;

/**
 *
 * @author Professional
 */
public class Datatypes {
    public static enum Datatype {
        NUMBER("number"),
        STRING("string"),
        BOOLEAN("boolean");
        
        public final String name;
        
        private Datatype(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    
    
}
