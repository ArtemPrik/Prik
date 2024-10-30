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
public final class UseStatement extends InterruptableNode implements Statement {
    public static final String PACKAGE = "prik.lib.modules.";
    public final Expression expression;
    
    public UseStatement(Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public void execute() {
        final Value value = expression.eval();
        switch (value.type()) {
            case Types.ARRAY:
                for (Value module : ((ArrayValue) value)) {
                    try {
                        load(module);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                break;
            case Types.STRING:
            {
                try {
                    load(value);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
                break;

            default:
                throw typeException(value);
        }
    }
    
    private void load(Value path) throws IOException {
        if (path.asString().contains("prik.")) {
            ModuleLoader.loadAndUse(path.asString());
        }
        else loadPrikFile(path.asString());
    }
    
    private Statement loadPrikFile(String path) throws IOException {
        String input = SourceLoader.readSource(path);
        String preprocess = Preprocessor.preprocess(input);
        List<Token> tokens = Lexer.tokenize(preprocess);
        Parser parser = new Parser(tokens);
        Statement program = parser.parse();
        if (parser.getParseErrors().hasErrors()) {
           throw new ParseException(parser.getParseErrors().toString());
        }
        program.accept(new FunctionAdder());
        return program;
    }
        
    private TypeException typeException(Value value) {
        return new TypeException("Array or string required in 'use' statement, " +
                "got " + Types.typeToString(value.type()) + " " + value);
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
        return "use " + expression.eval();
    }
}
