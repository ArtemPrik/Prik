package prik;

import java.util.List;
import java.util.Scanner;
import prik.parser.Lexer;
import prik.parser.Parser;
import prik.parser.Token;
import prik.parser.ast.Statement;
import prik.parser.visitors.AssignValidator;
import prik.parser.visitors.FunctionAdder;

/**
 *
 * @author Professional
 */
public final class Executor {
    public static void execute() {
        final String str = "prik>>>";
        
        Scanner sc = new Scanner(System.in);
        List<Token> tokens;
        String input = "";
        Statement program;
        while (true) {
            System.out.print(str);
            input = sc.nextLine();
            if ("exit".equals(input)) {
                System.exit(0);
            }
            tokens = Lexer.tokenize(input);
            program = new Parser(tokens).parse();
            program.accept(new FunctionAdder());
            program.accept(new AssignValidator());
            program.accept(new AssignValidator());
            program.execute();
        }
    }
}
