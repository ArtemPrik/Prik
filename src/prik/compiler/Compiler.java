package prik.compiler;

import java.io.File;
import java.io.PrintWriter;

/**
 *
 * @author Professional
 */
public class Compiler {
    public static void compile(String input) {
        try {
            File output = new File("output.asclii");
            PrintWriter pw = new PrintWriter(output);
            for (int ch : input.toCharArray()) {
                pw.println(ch + " ");
            }
            pw.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
