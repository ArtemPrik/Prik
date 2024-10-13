package prik;

import java.io.IOException;

/**
 * @author Professional
 */
public final class Main {
    public static String file = "programs/test.prik";
    
    public static void main(String[] args) throws IOException {
        Interpreter.run(file);
//        Executor.execute();
        
//        Console.println("""
//                          _____   _  _   __  __     _          _____    ___    ___ 
//                         |_   _| | \\| | |  \\/  |   /_\\        |_   _|  / _ \\  | _ \\
//                           | |   | .` | | |\\/| |  / _ \\         | |   | (_) | |  _/
//                           |_|   |_|\\_| |_|  |_| /_/ \\_\\        |_|    \\___/  |_|  """);
    }
}
