package prik.parser;

import prik.Main;
import prik.exceptions.PrikException;

/**
 *
 * @author Professional
 */
public final class LexerException extends PrikException {
    public LexerException() {
        super();
    }
    
    public LexerException(String message) {
        super("LexerError", message);
    }
    
//    public LexerException(int row, int col, String message) {
//        super("LexerError", "\n"
//                + "\t[ " + row + " : " + col + " ] " + message + "\n"
//                + "\tat: \n"
//                    + "\t\s\s\s" + Main.file);
//    }
    
    public LexerException(int row, int col, String message) {
        super("LexerError", "\n"
                + "\t" + message + "\n"
                + "\tat: \n"
                    + "\t\s\s\s" + Main.file + " [ " + row + " : " + col + " ]");
    }
}
