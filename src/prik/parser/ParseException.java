package prik.parser;

import prik.PrikException;


/**
 *
 * @author Professional
 */
public final class ParseException extends PrikException {
    public ParseException() {
        super();
    }
    
    public ParseException(String string) {
        super("ParseError" , string);
    }
}
