package prik.parser.ast;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import prik.Console;
import prik.exceptions.TypeException;
import prik.lib.ArrayValue;
import prik.lib.ModuleLoader;
import prik.lib.NumberValue;
import prik.lib.Types;
import prik.lib.Value;
import prik.lib.modules.Module;
import prik.namespaces.Namespace;
import prik.parser.Lexer;
import prik.parser.ParseException;
import prik.parser.Parser;
import prik.parser.SourceLoader;
import prik.parser.Token;
import prik.parser.visitors.FunctionAdder;
import prik.preprocessor.Preprocessor;

/**
 *
 * @author Professional
 */
public final class UsingNamespaceStatement extends InterruptableNode implements Statement {
    public static final String PACKAGE = "prik.namespaces.";
    public final String namespace;

    public UsingNamespaceStatement(String namespace) {
        this.namespace = namespace;
    }
    
    @Override
    public void execute() {
        try {
            Namespace module = (Namespace) Class.forName(PACKAGE + namespace).newInstance();
            module.init();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
//        return visitor.visit(this, t);
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public String toString() {
        return "using namespace " + namespace;
    }
}
