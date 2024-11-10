package prik.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author Professional
 */
public class Compiler {
    public static void compile(String input) {
        PrintWriter pw = null;
        try {
            File output = new File("output.asclii");
            pw = new PrintWriter(output);
            for (String s : input.split("\\ ")) {
                pw.println(s);
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        } finally {
            pw.close();
        }
    }
}
