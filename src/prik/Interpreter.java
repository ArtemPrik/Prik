package prik;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import prik.parser.*;
import prik.parser.ast.Statement;
import prik.parser.visitors.*;
import prik.preprocessor.Preprocessor;

/**
 *
 * @author Professional
 */
public final class Interpreter {
    public static void run(String file) throws java.io.IOException {
        final String input = new String(Files.readAllBytes(Paths.get(file)), "UTF-8");
        final String preprocess = Preprocessor.preprocess(input);
        final List<Token> tokens = Lexer.tokenize(preprocess);
        /* for (int i = 0; i < tokens.size(); i++) {
            Console.println(i + " " + tokens.get(i));
        } */
        final Statement program = Parser.parse(tokens);
//        Console.println(input);
//        Console.println(program.toString()); // Вывод команды в консоль
        program.accept(new FunctionAdder());
        program.accept(new AssignValidator());
        program.accept(new CodegenVisitor());
//        program.accept(new VariablePrinter());
//        Optimizer.optimize(program, 2);
        program.execute();
        /*Functions.get("main").execute();*/
    }
}
   
/*public static void main(String[] args) throws IOException {
    final String fileName = "program_new.prik";
    final String input = new String(Files.readAllBytes(Paths.get(fileName)), "UTF-8");
    final List<Token> tokens = new Lexer(input).tokenize();
    // for (Token token : tokens) {
    //     Console.println(token);
    // }
    final Statement program = new Parser(tokens).parse();
    // Console.println(program.toString()); Вывод команды в консоль
    program.accept(new FunctionAdder());
    program.accept(new AssignValidator());
    // program.accept(new CodegenVisitor());
    // program.accept(new VariablePrinter());
    program.execute();
}*/