package prik;

import java.io.File;
import java.io.IOException;
import prik.compiler.Compiler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Professional
 */
public final class Main {
    public static String file = "programs/test.prik";
    
    public static void main(String[] args) throws IOException {
        Interpreter.run(file);
//        Compiler.compile(new String(Files.readAllBytes(Paths.get(file))));
//        Interpreter.compile(file);
//        ConsoleExecutor.Start();
//        Executor.execute();
        
//        Console.println("""
//                          _____   _  _   __  __     _          _____    ___    ___ 
//                         |_   _| | \\| | |  \\/  |   /_\\        |_   _|  / _ \\  | _ \\
//                           | |   | .` | | |\\/| |  / _ \\         | |   | (_) | |  _/
//                           |_|   |_|\\_| |_|  |_| /_/ \\_\\        |_|    \\___/  |_|  """);
    }
}
