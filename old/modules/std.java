package prik.lib.modules;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import prik.Console;
import prik.exceptions.ArgumentsMismatchException;
import prik.exceptions.TypeException;
import prik.lib.*;
import prik.util.ModuleUtils;


/**
 *
 * @author Professional
 */
public final class std implements Module {
    @Override
    public void init() {
        MapValue std = new MapValue(4);
        
        Scanner sc = new Scanner(System.in);
        
        Functions.set("rand", new Rand());
        Functions.set("sleep", args -> {
            Arguments.check(1, args.length);
            if (args.length == 1) {
                try {
                    Thread.sleep((long) args[0].asNumber());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            return NumberValue.ZERO;
        });
        Functions.set("thread", args -> {
            if (args.length == 1) {
                new Thread(() -> {
                    Functions.get(args[0].asString()).execute();
                }).start();
            }
            return NumberValue.ZERO;
        });
        Functions.set("length", (Value... args) -> {
            Arguments.check(1, args.length);
            
            final Value val = args[0];
            final int length;
            switch (val.type()) {
                case Types.ARRAY:
                    length = ((ArrayValue) val).size();
                    break;
                case Types.MAP:
                    length = ((MapValue) val).size();
                    break;
                case Types.STRING:
                    length = ((StringValue) val).length();
                    break;
                case Types.FUNCTION:
                    final Function func = ((FunctionValue) val).getValue();
                    if (func instanceof UserDefinedFunction) {
                        length = ((UserDefinedFunction) func).getArgsCount();
                    } else {
                        length = 0;
                    }
                    break;
                default:
                    length = 0;
                    
            }
            return NumberValue.of(length);
        });
        // <editor-fold defaultstate="collapsed" desc="Console">
        MapValue console = new MapValue(8);
//        Functions.set("echo", args -> {
//            for (Value arg : args) {
//                Console.print(arg.asString());
//                Console.print(" ");
//            }
//            Console.println();
//            return NumberValue.ZERO;
//        });
        Functions.set("error", args -> {
            Arguments.check(1, args.length);
            Console.error(args[0].asString());
            return NumberValue.ZERO;
        });
        Functions.set("readln", (Function) (Value... args) -> {
            Arguments.check(0, args.length);
            return new StringValue(sc.nextLine());
        });
        Functions.set("toLowerCase", args -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().toLowerCase());
        });
        Functions.set("toUpperCase", args -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().toUpperCase());
        });
        Functions.set("toChar", args -> {
            Arguments.check(1, args.length);
            return new StringValue(String.valueOf(args[0].asNumber()));
        });
        Functions.set("charAt", (Value... args) -> {
            Arguments.check(2, args.length);
            
            final String input = args[0].asString();
            final int index = args[1].asInt();
            
            return NumberValue.of((short)input.charAt(index));
        });
        Variables.define("console", console);
        // </editor-fold>
        
        MapValue numbers = new MapValue(1);
        // <editor-fold defaultstate="collapsed" desc="Numbers">
        Functions.set("toHexString", (Value... args) -> {
            Arguments.check(1, args.length);
            long value;
            if (args[0].type() == Types.NUMBER) {
                value = ((NumberValue) args[0]).asLong();
            } else {
                value = (long) args[0].asNumber();
            }
            return new StringValue(Long.toHexString(value));
        });
        // </editor-fold>
        Variables.define("numbers", numbers);
        
        MapValue string = new MapValue(17);
        // <editor-fold defaultstate="collapsed" desc="String">
        Functions.set("getBytes", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            final String charset = (args.length == 2) ? args[1].asString() : "UTF-8";
            try {
                return ArrayValue.of(args[0].asString().getBytes(charset));
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException(uee);
            }
        });
        Functions.set("split", (Value... args) -> {
            Arguments.checkOrOr(2, 3, args.length);

            final String input = args[0].asString();
            final String regex = args[1].asString();
            final int limit = (args.length == 3) ? args[2].asInt() : 0;

            final String[] parts = input.split(regex, limit);
            return ArrayValue.of(parts);
        });
        Functions.set("indexOf", (Value... args) -> {
            Arguments.checkOrOr(2, 3, args.length);
            
            final String input = args[0].asString();
            final String what = args[1].asString();
            final int index = (args.length == 3) ? args[2].asInt() : 0;
            
            return NumberValue.of(input.indexOf(what, index));
        });
        Functions.set("lastIndexOf", (Value... args) -> {
            Arguments.checkOrOr(2, 3, args.length);
            
            final String input = args[0].asString();
            final String what = args[1].asString();
            if (args.length == 2) {
                return NumberValue.of(input.lastIndexOf(what));
            }
            final int index = args[2].asInt();
            return NumberValue.of(input.lastIndexOf(what, index));
        });
        string.set("charAt", (Value... args) -> {
            Arguments.check(2, args.length);
            
            final String input = args[0].asString();
            final int index = args[1].asInt();
            
            return NumberValue.of((short)input.charAt(index));
        });
        Functions.set("toChar", args -> {
            Arguments.check(1, args.length);
            return new StringValue(String.valueOf(args[0].asNumber()));
        });
        Functions.set("substring", (Value... args) -> {
            Arguments.checkOrOr(2, 3, args.length);
        
            final String input = args[0].asString();
            final int startIndex = args[1].asInt();

            String result;
            if (args.length == 2) {
                result = input.substring(startIndex);
            } else {
                final int endIndex = args[2].asInt();
                result = input.substring(startIndex, endIndex);
            }

            return new StringValue(result);
        });
        Functions.set("toLowerCase", args -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().toLowerCase());
        });
        Functions.set("toUpperCase", args -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().toUpperCase());
        });
        Functions.set("trim", (Value... args) -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().trim());
        });
        Functions.set("replace", (Value... args) -> {
            Arguments.check(3, args.length);
            
            final String input = args[0].asString();
            final String target = args[1].asString();
            final String replacement = args[2].asString();
            
            return new StringValue(input.replace(target, replacement));
        });
        Functions.set("replaceAll", (Value... args) -> {
            Arguments.check(3, args.length);
            
            final String input = args[0].asString();
            final String regex = args[1].asString();
            final String replacement = args[2].asString();
            
            return new StringValue(input.replaceAll(regex, replacement));
        });
        Functions.set("replaceFirst", (Value... args) -> {
            Arguments.check(3, args.length);
            
            final String input = args[0].asString();
            final String regex = args[1].asString();
            final String replacement = args[2].asString();
            
            return new StringValue(input.replaceFirst(regex, replacement));
        });
        Functions.set("parseInt", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            final int radix = (args.length == 2) ? args[1].asInt() : 10;
            return NumberValue.of(Integer.parseInt(args[0].asString(), radix));
        });
        Functions.set("parseLong", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            final int radix = (args.length == 2) ? args[1].asInt() : 10;
            return NumberValue.of(Long.parseLong(args[0].asString(), radix));
        });
        Functions.set("stripMargin", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            final String input = args[0].asString();
            final String marginPrefix = (args.length == 2) ? args[1].asString() : "|";
            if (!input.contains(marginPrefix)) {
                return args[0];
            }
            final String[] lines = input.split("\\r?\\n");

            // First blank line is omitted
            final StringBuilder sb = new StringBuilder();
            final int firstLineIndex = (ModuleUtils.isBlank(lines[0])) ? 1 : 0;
            final int lastLineIndex = lines.length - 1;
            int index = firstLineIndex;
            while (true) {
                sb.append(ModuleUtils.strip(lines[index], marginPrefix));
                if (++index >= lastLineIndex) break;
                sb.append('\n');
            }
            // Process last line
            if (lastLineIndex >= (firstLineIndex + 1) && !ModuleUtils.isBlank(lines[lastLineIndex])) {
                sb.append('\n').append(ModuleUtils.strip(lines[lastLineIndex], marginPrefix));
            }
        return new StringValue(sb.toString());
        });
        // </editor-fold>
        Variables.define("string", string);
        
        MapValue arraysAndMaps = new MapValue(10);
        // <editor-fold defaultstate="collapsed" desc="Arrays And Maps">
        Functions.set("newArray", new Function() {
            @Override
            public Value execute(Value... args) {
                return createArray(args, 0);
            }
            private ArrayValue createArray(Value[] args, int index) {
                final int size = (int) args[index].asNumber();
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
                    Arrays.sort(elements);
                    break;
                case 2:
                    final Function comparator = ValueUtils.consumeFunction(args[1], 1);
                    Arrays.sort(elements, (o1, o2) -> comparator.execute(o1, o2).asInt());
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
        Functions.set("arrayKeyExists", (Value... args) -> {
            Arguments.check(2, args.length);
            if (args[1].type() != Types.MAP) {
                throw new TypeException("Map expected in second argument");
            }
            final MapValue map = ((MapValue) args[1]);
            return NumberValue.fromBoolean(map.containsKey(args[0]));
        });
        Functions.set("arrayKeys", (Value... args) -> {
            Arguments.check(1, args.length);
            if (args[0].type() != Types.MAP) {
                throw new TypeException("Map expected in first argument");
            }
            final MapValue map = ((MapValue) args[0]);
            final List<Value> keys = new ArrayList<>(map.size());
            for (Map.Entry<Value, Value> entry : map) {
                keys.add(entry.getKey());
            }
            return new ArrayValue(keys);
        });
        Functions.set("arrayValues", (Value... args) -> {
            Arguments.check(1, args.length);
            if (args[0].type() != Types.MAP) {
                throw new TypeException("Map expected in first argument");
            }
            final MapValue map = ((MapValue) args[0]);
            final List<Value> values = new ArrayList<>(map.size());
            for (Map.Entry<Value, Value> entry : map) {
                values.add(entry.getValue());
            }
            return new ArrayValue(values);
        });
        Functions.set("arraySplice", (Value... args) -> {
            Arguments.checkRange(2, 4, args.length);
            if (args[0].type() != Types.ARRAY) {
                throw new TypeException("Array expected at first argument");
            }
            final Value[] input = ((ArrayValue) args[0]).getCopyElements();
            final int size = input.length;

            int start = args[1].asInt();
            if (start < 0) start = size - Math.abs(start);
            start = Math.max(0, Math.min(size, start));

            final int deleteCount = (args.length >= 3)
                    ? Math.max(0, Math.min(size - start, args[2].asInt())) // [0..size - start)
                    : (size - start);

            final Value[] additions;
            if (args.length != 4) {
                additions = new Value[0];
            } else if (args[3].type() == Types.ARRAY) {
                additions = ((ArrayValue) args[3]).getCopyElements();
            } else {
                throw new TypeException("Array expected at fourth argument");
            }

            Value[] result = new Value[start + (size - start - deleteCount) + additions.length];
            System.arraycopy(input, 0, result, 0, start); // [0, start)
            System.arraycopy(additions, 0, result, start, additions.length); // insert new
            System.arraycopy(input, start + deleteCount, result, start + additions.length, size - start - deleteCount);
            return new ArrayValue(result);
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
        // </editor-fold>
        Variables.define("arraysAndMaps", arraysAndMaps);
        
        std.set("arraysAndMaps", arraysAndMaps);
        std.set("console", console);
        std.set("numbers", numbers);
        std.set("string", string);
        Variables.define("std", std);
    }

    private static class Rand implements Function {
        private static final Random RND = new Random();

        @Override
        public Value execute(Value... args) {
            if (args.length == 0) return new NumberValue(RND.nextDouble());
            
            int from = 0;
            int to = 100;
            if (args.length == 1) {
                to = (int) args[0].asNumber();
            } else if (args.length == 2) {
                from = (int) args[0].asNumber();
                to = (int) args[1].asNumber();
            }
            return new NumberValue(RND.nextInt(to - from) + from);
        }
    }
}
