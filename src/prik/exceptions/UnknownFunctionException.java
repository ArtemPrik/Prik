package prik.exceptions;

/**
 *
 * @author Professional
 */
public final class UnknownFunctionException extends PrikException {
    private final String functionName;

    public UnknownFunctionException(String name) {
//        Console.error("Unknown variable \"" + name + "\"\n"
//                + "\t\tin \"" + Main.file + "\"");
//        System.exit(0);
        super("prik.errors.UnknownFunctionError" ,"Function \"" + name + "\" is not found!");        
        this.functionName = name;
    }

    public String getFunctionName() {
        return functionName;
    }
}
