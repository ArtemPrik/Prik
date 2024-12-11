package prik.exceptions;

import java.time.LocalDateTime;
import java.util.Date;
import prik.Console;

/**
 *
 * @author Professional
 */
public class PrikException extends RuntimeException {
    public PrikException() {
    }
    
    public PrikException(String errorName, Object message) {
        Console.error("┌──────────────Prik Exception───────────────");
        Console.error("│ Call from : " + errorName);
        Console.error("│");
        Console.error("│ " + message);
        Console.error("│");
//        Console.error("| " + LocalDateTime.now());
        Console.error("│ " + new Date());
        Console.error("╰────────────────────────────────────────────");
        System.exit(1);
    }
    
    public PrikException(String errorName, String err1, Object message) {
        Console.error("[" +errorName + "] : " + err1 + message);
        System.exit(0);
    }
}