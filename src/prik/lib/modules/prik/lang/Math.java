package prik.lib.modules.prik.lang;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;
import prik.lib.Function;
import prik.lib.Functions;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.Value;
import prik.lib.Variables;
import prik.lib.modules.Module;

/**
 *
 * @author Professional
 */
public final class Math implements Module {
    private static final DoubleFunction<NumberValue> doubleToNumber = v -> new NumberValue(v);

    @Override
    public void init() {
        
        Variables.set("PI", new NumberValue(java.lang.Math.PI));
        Variables.set("TAU", new NumberValue(java.lang.Math.TAU));
        Variables.set("E", new NumberValue(java.lang.Math.E));
        
        Functions.set("abs", functionConvert(java.lang.Math::abs));
        Functions.set("acos", functionConvert(java.lang.Math::acos));
        Functions.set("cos", functionConvert(java.lang.Math::cos));
        Functions.set("asin", functionConvert(java.lang.Math::asin));
        Functions.set("sin", functionConvert(java.lang.Math::sin));
        Functions.set("log", functionConvert(java.lang.Math::log));
        Functions.set("tan", functionConvert(java.lang.Math::tan));
        
        Functions.set("sqrt", functionConvert(java.lang.Math::sqrt));
        Functions.set("toDegrees", functionConvert(java.lang.Math::toDegrees));
        Functions.set("toRadians", functionConvert(java.lang.Math::toRadians));
        
        Functions.set("pow", biFunctionConvert(java.lang.Math::pow));
        Functions.set("atan", functionConvert(java.lang.Math::atan));
        Functions.set("atan2", biFunctionConvert(java.lang.Math::atan2));
        
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
