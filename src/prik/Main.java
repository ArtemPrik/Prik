package prik;

import java.io.File;
import java.io.IOException;
import prik.compiler.Compiler;
import prik.compiler.asclii.ASCLII;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Professional
 */
public final class Main {
    public static String file = "programs/test.prik";
    
    public static void main(String[] args) throws IOException {
//        Interpreter.run(file);
        Interpreter.compile(file);
//        ConsoleExecutor.Start();
//        Executor.execute();
        
//        Console.println("""
//                          _____   _  _   __  __     _          _____    ___    ___ 
//                         |_   _| | \\| | |  \\/  |   /_\\        |_   _|  / _ \\  | _ \\
//                           | |   | .` | | |\\/| |  / _ \\         | |   | (_) | |  _/
//                           |_|   |_|\\_| |_|  |_| /_/ \\_\\        |_|    \\___/  |_|  """);
    }
}
