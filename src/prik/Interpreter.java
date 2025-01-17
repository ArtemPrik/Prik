package prik;

import prik.parser.visitors.AssignValidator;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import prik.parser.*;
import prik.parser.ast.Statement;
import prik.parser.visitors.*;
import prik.preprocessor.Preprocessor;
import prik.util.TimeMeasurement;

/**
 *
 * @author Professional
 */
public final class Interpreter {
    public static void run(String file) throws java.io.IOException {
        TimeMeasurement measurement = new TimeMeasurement();
        
        final String input = new String(Files.readAllBytes(Paths.get(file)), "UTF-8");
        
        measurement.start("Preprocess and Beautify time");
        final String preprocess = Preprocessor.preprocess(input);
//        Console.println(Beautifier.beautify(preprocess));
        measurement.stop("Preprocess and Beautify time");
        
        measurement.start("Tokenization time");
        final List<Token> tokens = Lexer.tokenize(preprocess);
        /* for (int i = 0; i < tokens.size(); i++) {
            Console.println(i + " " + tokens.get(i));
        } */
        measurement.stop("Tokenization time");
        
        measurement.start("Parsing time");
        final Statement program = Parser.parse(tokens);
//        Console.println(input);
//        Console.println(program.toString()); // Вывод команды в консоль
        program.accept(new FunctionAdder());
        program.accept(new AssignValidator());
        program.accept(new CodegenVisitor());
//        program.accept(new VariablePrinter());
        measurement.stop("Parsing time");

        measurement.start("Execution time");
        program.execute();
        measurement.stop("Execution time");
        
        Console.println("\n");
//        Console.print("\n\n\n" + measurement.summary(TimeUnit.MILLISECONDS, true));
//        Console.println("\nDate: " + new Date());
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