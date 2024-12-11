package prik.modules.prik.util;

import java.io.UnsupportedEncodingException;
import prik.exceptions.ArgumentsMismatchException;
import prik.exceptions.TypeException;
import prik.lib.Arguments;
import prik.lib.ArrayValue;
import prik.lib.Function;
import prik.lib.Functions;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Types;
import prik.lib.Value;
import prik.lib.ValueUtils;
import prik.modules.Module;

/**
 *
 * @author Professional
 */
public final class Arrays implements Module {
    @Override
    public void init() {
        Functions.set("newArray", new Function() {
            @Override
            public Value execute(Value... args) {
                return createArray(args, 0);
            }
            private ArrayValue createArray(Value[] args, int index) {
                final int size = args[index].asInt();
                final int last = args.length - 1;
                ArrayValue array = new ArrayValue(size);
                if (index == last) {
                    for (int i = 0; i < size; i++) {
                        array.set(i, NumberValue.ZERO);
                    }
                } else if (index < last) {
                    for (int i = 0; i < size; i++) {
                        array.set(i, createArray(args, index + 1));
                    }
                }
                return array;
            }
        });
        
        Functions.set("join", new Function() {
            @Override
            public Value execute(Value... args) {
                Arguments.checkRange(1, 4, args.length);
                if (args[0].type() != Types.ARRAY) {
                    throw new TypeException("Array expected in first argument");
                }

                final ArrayValue array = (ArrayValue) args[0];
                switch (args.length) {
                    case 1:
                        return join(array, "", "", "");
                    case 2:
                        return join(array, args[1].asString(), "", "");
                    case 3:
                        return join(array, args[1].asString(), args[2].asString(), args[2].asString());
                    case 4:
                        return join(array, args[1].asString(), args[2].asString(), args[3].asString());
                    default:
                        throw new ArgumentsMismatchException("Wrong number of arguments");
                }
            }

            private static StringValue join(ArrayValue array, String delimiter, String prefix, String suffix) {
                final StringBuilder sb = new StringBuilder();
                for (Value value : array) {
                    if (sb.length() > 0) sb.append(delimiter);
                    else sb.append(prefix);
                    sb.append(value.asString());
                }
                sb.append(suffix);
                return new StringValue(sb.toString());
            }
        });
        Functions.set("sort", (Value... args) -> {
            Arguments.checkAtLeast(1, args.length);
            if (args[0].type() != Types.ARRAY) {
                throw new TypeException("Array expected in first argument");
            }
            final Value[] elements = ((ArrayValue) args[0]).getCopyElements();
            
            switch (args.length) {
                case 1:
                    java.util.Arrays.sort(elements);
                    break;
                case 2:
                    final Function comparator = ValueUtils.consumeFunction(args[1], 1);
                    java.util.Arrays.sort(elements, (o1, o2) -> comparator.execute(o1, o2).asInt());
                    break;
                default:
                    throw new ArgumentsMismatchException("Wrong number of arguments");
            }
            
            return new ArrayValue(elements);
        });
        Functions.set("arrayCombine", (Value... args) -> {
            Arguments.check(2, args.length);
            if (args[0].type() != Types.ARRAY) {
                throw new TypeException("Array expected in first argument");
            }
            if (args[1].type() != Types.ARRAY) {
                throw new TypeException("Array expected in second argument");
            }
            
            final ArrayValue keys = ((ArrayValue) args[0]);
            final ArrayValue values = ((ArrayValue) args[1]);
            final int length = Math.min(keys.size(), values.size());
            
            final MapValue result = new MapValue(length);
            for (int i = 0; i < length; i++) {
                result.set(keys.get(i), values.get(i));
            }
            return result;
        });
        Functions.set("stringFromBytes", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            if (args[0].type() != Types.ARRAY) {
                throw new TypeException("Array expected at first argument");
            }
            final byte[] bytes = ValueUtils.toByteArray((ArrayValue) args[0]);
            final String charset = (args.length == 2) ? args[1].asString() : "UTF-8";
            try {
                return new StringValue(new String(bytes, charset));
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException(uee);
            }
        });
    }
}
