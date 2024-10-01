package prik;

/**
 *
 * @author Professional
 */
public class PrikException extends RuntimeException {
    public PrikException() {
    }
    
    public PrikException(String errorName, Object message) {
        Console.error("[" +errorName + "] : " + message);
        System.exit(0);
    }
    
    public PrikException(String errorName, String err1, Object message) {
        Console.error("[" +errorName + "] : " + err1 + message);
        System.exit(0);
    }
}