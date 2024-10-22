package prik.parser;

import prik.exceptions.PrikException;


/**
 *
 * @author Professional
 */
public final class ParseException extends RuntimeException {
    public ParseException() {
        super();
    }
    
    public ParseException(String string) {
        super(string);
    }
}
