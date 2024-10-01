package prik.lib.modules;

import java.util.Scanner;
import prik.Console;
import prik.lib.*;


/**
 *
 * @author Professional
 */
public final class console implements Module {
    @Override
    public void init() {
        MapValue console = new MapValue(7);
        
        console.set("echo", args -> {
            for (Value arg : args) {
                Console.print(arg.asString());
                Console.print(" ");
            }
            Console.println();
            return NumberValue.ZERO;
        });
        
        console.set("error", args -> {
            Arguments.check(1, args.length);
            Console.error(args[0].asString());
            return NumberValue.ZERO;
        });
        
        console.set("readln", (Function) (Value... args) -> {
            try (Scanner sc = new Scanner(System.in)) {
                return new StringValue(sc.nextLine());
            }
        });
        /*Functions.set("input", args -> {
            Arguments.checkOrOr(0, 1, args.length);
            if(args.length == 1) Console.print(args[0].asString());
            try (Scanner sc = new Scanner(System.in)) {
                return new StringValue(sc.nextLine());
            }
        });*/
        
        console.set("toLowerCase", args -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().toLowerCase());
        });
        console.set("toUpperCase", args -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().toUpperCase());
        });
        
        console.set("toChar", args -> {
            Arguments.check(1, args.length);
            return new StringValue(String.valueOf(args[0].asNumber()));
        });
        console.set("charAt", (Value... args) -> {
            Arguments.check(2, args.length);
            
            final String input = args[0].asString();
            final int index = args[1].asInt();
            
            return NumberValue.of((short)input.charAt(index));
        });
        
        Variables.define("console", console);
    }
}
