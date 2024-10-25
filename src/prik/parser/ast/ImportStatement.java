package prik.parser.ast;

import java.io.IOException;
import java.util.List;
import prik.parser.Lexer;
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
    public final Expression expression;
    
    public ImportStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute() {
        super.interruptionCheck();
        try {
            final Statement program = loadProgram(expression.eval().asString());
            if (program != null) {
                program.accept(new FunctionAdder());
                program.execute();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Statement loadProgram(String path) throws IOException {
        final String input = SourceLoader.readSource(path);
        final String preprocess = Preprocessor.preprocess(input);
        final List<Token> tokens = Lexer.tokenize(preprocess);
        final Parser parser = new Parser(tokens);
        final Statement program = parser.parse();
        if (parser.getParseErrors().hasErrors()) {
            return null;
        }
        return program;
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
        return "include " + expression;
    }
}
