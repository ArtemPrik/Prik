package prik.lib.modules.prik.lang;

import java.io.UnsupportedEncodingException;
import prik.lib.Arguments;
import prik.lib.ArrayValue;
import prik.lib.Functions;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Value;
import prik.lib.Variables;
import prik.lib.modules.Module;
import prik.util.ModuleUtils;

/**
 *
 * @author Professional
 */
public final class String implements Module {
    @Override
    public void init() {
        MapValue map = new MapValue(16);
        map.set("getBytes", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            final java.lang.String charset = (args.length == 2) ? args[1].asString() : "UTF-8";
            try {
                return ArrayValue.of(args[0].asString().getBytes(charset));
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException(uee);
            }
        });
        map.set("split", (Value... args) -> {
            Arguments.checkOrOr(2, 3, args.length);

            final java.lang.String input = args[0].asString();
            final java.lang.String regex = args[1].asString();
            final int limit = (args.length == 3) ? args[2].asInt() : 0;

            final java.lang.String[] parts = input.split(regex, limit);
            return ArrayValue.of(parts);
        });
        map.set("indexOf", (Value... args) -> {
            Arguments.checkOrOr(2, 3, args.length);
            
            final java.lang.String input = args[0].asString();
            final java.lang.String what = args[1].asString();
            final int index = (args.length == 3) ? args[2].asInt() : 0;
            
            return NumberValue.of(input.indexOf(what, index));
        });
        map.set("lastIndexOf", (Value... args) -> {
            Arguments.checkOrOr(2, 3, args.length);
            
            final java.lang.String input = args[0].asString();
            final java.lang.String what = args[1].asString();
            if (args.length == 2) {
                return NumberValue.of(input.lastIndexOf(what));
            }
            final int index = args[2].asInt();
            return NumberValue.of(input.lastIndexOf(what, index));
        });
        map.set("charAt", (Value... args) -> {
            Arguments.check(2, args.length);
            
            final java.lang.String input = args[0].asString();
            final int index = args[1].asInt();
            
            return NumberValue.of((short)input.charAt(index));
        });
        map.set("toChar", args -> {
            Arguments.check(1, args.length);
            return new StringValue(java.lang.String.valueOf(args[0].asNumber()));
        });
        map.set("substring", (Value... args) -> {
            Arguments.checkOrOr(2, 3, args.length);
        
            final java.lang.String input = args[0].asString();
            final int startIndex = args[1].asInt();

            java.lang.String result;
            if (args.length == 2) {
                result = input.substring(startIndex);
            } else {
                final int endIndex = args[2].asInt();
                result = input.substring(startIndex, endIndex);
            }

            return new StringValue(result);
        });
        map.set("toLowerCase", args -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().toLowerCase());
        });
        map.set("toUpperCase", args -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().toUpperCase());
        });
        map.set("trim", (Value... args) -> {
            Arguments.check(1, args.length);
            return new StringValue(args[0].asString().trim());
        });
        map.set("replace", (Value... args) -> {
            Arguments.check(3, args.length);
            
            final java.lang.String input = args[0].asString();
            final java.lang.String target = args[1].asString();
            final java.lang.String replacement = args[2].asString();
            
            return new StringValue(input.replace(target, replacement));
        });
        map.set("replaceAll", (Value... args) -> {
            Arguments.check(3, args.length);
            
            final java.lang.String input = args[0].asString();
            final java.lang.String regex = args[1].asString();
            final java.lang.String replacement = args[2].asString();
            
            return new StringValue(input.replaceAll(regex, replacement));
        });
        map.set("replaceFirst", (Value... args) -> {
            Arguments.check(3, args.length);
            
            final java.lang.String input = args[0].asString();
            final java.lang.String regex = args[1].asString();
            final java.lang.String replacement = args[2].asString();
            
            return new StringValue(input.replaceFirst(regex, replacement));
        });
        map.set("parseInt", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            final int radix = (args.length == 2) ? args[1].asInt() : 10;
            return NumberValue.of(Integer.parseInt(args[0].asString(), radix));
        });
        map.set("parseLong", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            final int radix = (args.length == 2) ? args[1].asInt() : 10;
            return NumberValue.of(Long.parseLong(args[0].asString(), radix));
        });
        map.set("stripMargin", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            final java.lang.String input = args[0].asString();
            final java.lang.String marginPrefix = (args.length == 2) ? args[1].asString() : "|";
            if (!input.contains(marginPrefix)) {
                return args[0];
            }
            final java.lang.String[] lines = input.split("\\r?\\n");

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
        Variables.define("String", map);
    }
}
