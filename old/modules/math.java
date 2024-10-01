package prik.lib.modules;

import prik.lib.*;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;

/**
 *
 * @author Professional
 */
public class math implements Module {
    
    private static final DoubleFunction<NumberValue> doubleToNumber = v -> new NumberValue(v);

    @Override
    public void init() {
        Variables.set("PI", new NumberValue(Math.PI));
        Variables.set("TAU", new NumberValue(Math.TAU));
        Variables.set("E", new NumberValue(Math.E));
        
        Functions.set("abs", functionConvert(Math::abs));
        Functions.set("acos", functionConvert(Math::acos));
        Functions.set("cos", functionConvert(Math::cos));
        Functions.set("asin", functionConvert(Math::asin));
        Functions.set("sin", functionConvert(Math::sin));
        Functions.set("log", functionConvert(Math::log));
        
        Functions.set("sqrt", functionConvert(Math::sqrt));
        Functions.set("toDegrees", functionConvert(Math::toDegrees));
        Functions.set("toRadians", functionConvert(Math::toRadians));
        
        Functions.set("pow", biFunctionConvert(Math::pow));
        Functions.set("atan", functionConvert(Math::atan));
        Functions.set("atan2", biFunctionConvert(Math::atan2));
        
//        Functions.set("sum", args -> NumberValue.of(args[0].asNumber() + args[1].asNumber()));
//        Functions.set("diff", args -> NumberValue.of(args[0].asNumber() - args[1].asNumber()));
//        Functions.set("mul", args -> NumberValue.of(args[0].asNumber() * args[1].asNumber()));
//        Functions.set("div", args -> NumberValue.of(args[0].asNumber() / args[1].asNumber()));
        
        Functions.set("summary", (Value... args) -> {
            double summary = 0;
            for (Value arg : args) {
                summary += arg.asNumber();
            }
//            for (int i = 0; i < args.length; i++) {
//                summary += args[i].asNumber();
//            }
            return new NumberValue(summary);
        });
        Functions.set("substract", (Value... args) -> {
            double diffine = args[0].asNumber();
            for (int i = 1; i < args.length; i++) {
                diffine -= args[i].asNumber();
            }
            return new NumberValue(diffine);
        });
        Functions.set("multiply", (Value... args) -> {
            double multiply = args[0].asNumber();
            for (int i = 1; i < args.length; i++) {
                multiply *= args[i].asNumber();
            }
            return new NumberValue(multiply);
        });
        Functions.set("divide", (Value... args) -> {
            double divide = args[0].asNumber();
            for (int i = 1; i < args.length; i++) {
                divide /= args[i].asNumber();
            }
            return new NumberValue(divide);
        });
    }
    
    private static Function functionConvert(DoubleUnaryOperator op) {
        return args -> {
            if (args.length != 1) throw new RuntimeException("One arg expected");
            return doubleToNumber.apply(op.applyAsDouble(args[0].asNumber()));
        };
    }
    
    private static Function biFunctionConvert(DoubleBinaryOperator op) {
        return args -> {
            if (args.length != 2) throw new RuntimeException("Two args expected");
            return doubleToNumber.apply(op.applyAsDouble(args[0].asNumber(), args[1].asNumber()));
        };
    }
}
