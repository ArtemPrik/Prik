package prik.executors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import prik.Console;
import prik.Interpreter;

/**
 *
 * @author Professional
 */
public final class ConsoleExecutor {
    private static final String version = "beta 0.1";
    
    public static void Start() throws IOException {
//        System.out.println("\n");
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        String cmd = sc.readLine();
        if (cmd.contains("prik")) {
            String[] objs = cmd.split(" ");
            
            if (objs[1].equals("--run") || objs[1].equals("--r")) {
                System.out.print("\nEnter path to your file: ");
                Scanner scan = new Scanner(System.in);
                String in = scan.nextLine();
                Interpreter.run(in);
            } else if (objs[1].equals("--help") || objs[1].equals("--h")) {
                // Helper
            } else if (objs[1].equals("--version") || objs[1].equals("--v")) {
                // Version
                Console.println("Prik version: " + version);
            } else if (objs[1].equals("cls") || objs[1].equals("clear")) {
//                clear();
            } else {
                // About
            }
        }
    }
}
