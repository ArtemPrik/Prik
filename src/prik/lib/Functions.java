package prik.lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import prik.Console;
import prik.exceptions.UnknownFunctionException;
import prik.lib.functions.Range;

/**
 *
 * @author Professional
 */
public final class Functions {
    private static final Map<String, Function> functions;
    static {
        functions = new HashMap<>();
        
//        functions.put("echo", args -> {
//            for (Value arg : args) {
//                Console.print(arg.asString());
//                Console.print(" ");
//            }
//            Console.println();
//            return NumberValue.ZERO;
//        });
        functions.put("echo", (Function) (Value... args) -> {
            final StringBuilder sb = new StringBuilder();
            for (Value arg : args) {
                sb.append(arg.asString());
                sb.append(" ");
            }
            Console.println(sb.toString());
            return NumberValue.ZERO;
        });
        
        functions.put("equals", (Function) (Value... args) -> {
            Arguments.check(2, args.length);
            boolean check = args[0].equals(args[1]);
            return new BooleanValue(check);
        });
        
        functions.put("sprintf", (Value... args) -> {
            Arguments.checkAtLeast(1, args.length);
            
            final String format = args[0].asString();
            final Object[] values = new Object[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                values[i - 1] = (args[i].type() == Types.NUMBER)
                        ? args[i].raw()
                        : args[i].asString();
            }
            return new StringValue(String.format(format, values));
        });
        
        functions.put("range", new Range());
        
    }
    
    public static void clear() {
        functions.clear();
    }

    public static Map<String, Function> getFunctions() {
        return functions;
    }
    
    public static boolean isExists(String key) {
        return functions.containsKey(key);
    }
    
    public static Function get(String key) {
        if (!isExists(key)) throw new UnknownFunctionException(key);
        return functions.get(key);
    }
    
    public static void set(String key, Function function) {
        functions.put(key, function);
    }
}
