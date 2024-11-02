package prik.parser.ast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import prik.exceptions.TypeException;
import prik.lib.ArrayValue;
import prik.lib.ModuleLoader;
import prik.lib.Types;
import prik.lib.Value;
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
public final class ImportStatement extends InterruptableNode implements Statement {
    public static final String PACKAGE = "prik.lib.modules.";
    public final Expression expression;
    
    public ImportStatement(Expression expression) {
        this.expression = expression;
    }
    
    public ImportStatement(String name) {
        this.expression = new ValueExpression(name);
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
//        try {
//            ModuleLoader.loadAndUse(path.asString());
//        } catch (Exception ex) {
//            loadPrikFile(path.asString());
//        }
        try {
            loadPrikFile(path.asString());
        } catch (FileNotFoundException e) {
            ModuleLoader.loadAndUse(path.asString());
        }
    }
    
    public Statement loadPrikFile(String path) throws IOException {
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
    
    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
        return visitor.visit(this, t);
    }

    @Override
    public String toString() {
        return "import " + expression.eval();
    }
}
