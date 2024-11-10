package prik.lib;

import java.util.Locale;
import java.util.Objects;
import prik.exceptions.UnknownException;

/**
 *
 * @author Professional
 */
public final class StringValue implements Value {
    public static final StringValue EMPTY = new StringValue("");
    private final String value;

    public StringValue(String value) {
        this.value = value;
    }
    
    public Value access(Value propertyValue) {
        final String prop = propertyValue.asString();
        switch (propertyValue.type()) {
            case Types.STRING:
                switch (prop) {
                    // Properties
                    case "length":
                        return NumberValue.of(length());
                    case "lower":
                        return new StringValue(value.toLowerCase());
                    case "upper":
                        return new StringValue(value.toUpperCase());
                    case "chars": {
                        final Value[] chars = new Value[length()];
                        int i = 0;
                        for (char ch : value.toCharArray()) {
                            chars[i++] = NumberValue.of((int) ch);
                        }
                        return new ArrayValue(chars);
                    }

                    // Functions
                    case "repeat":
                        return new FunctionValue((Value... args) -> {
                            Arguments.check(1, args.length);
                            StringBuilder buffer = new StringBuilder();
                            for (int i = 1; i < args[0].asInt() +1; i++) {
                                buffer.append(value);
                            }
                            return new StringValue(buffer.toString());
                        });
                    case "trim":
                        return Converters.voidToString(value::trim);
                    case "startsWith":
                        return new FunctionValue(args -> {
                            Arguments.checkOrOr(1, 2, args.length);
                            int offset = (args.length == 2) ? args[1].asInt() : 0;
                            return NumberValue.fromBoolean(value.startsWith(args[0].asString(), offset));
                        });
                    case "endsWith":
                        return Converters.stringToBoolean(value::endsWith);
                    case "matches":
                        return Converters.stringToBoolean(value::matches);
                    case "contains":
                        return Converters.stringToBoolean(value::contains);
                    case "equalsIgnoreCase":
                        return Converters.stringToBoolean(value::equalsIgnoreCase);
                    case "isEmpty":
                        return Converters.voidToBoolean(value::isEmpty);

                    default:
                        if (Functions.isExists(prop)) {
                            final Function f = Functions.get(prop);
                            return new FunctionValue(args -> {
                                final Value[] newArgs = new Value[args.length + 1];
                                newArgs[0] = this;
                                System.arraycopy(args, 0, newArgs, 1, args.length);
                                return f.execute(newArgs);
                            });
                        }
                        break;
                }
            case Types.NUMBER:
                return get(propertyValue.asInt());
        }
        throw new RuntimeException("Unkown property: " + prop);
    }
    
    public Value get(int index) {
        return new StringValue(Character.toString(value.charAt(index)));
    }
    
    public int length() {
        return value.length();
    }
    
    public static StringValue fromBoolean(boolean b) {
        return b ? new StringValue("true") : new StringValue("false");
    }
    
    @Override
    public double asNumber() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String asString() {
        return value;
    }
    
    @Override
    public int asInt() {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    @Override
    public Object raw() {
        return value;
    }

    @Override
    public int type() {
        return Types.STRING;
    }

    @Override
    public int compareTo(Value o) {
        if (o.type() == Types.STRING) {
            return value.compareTo(((StringValue) o).value);
        }
        return asString().compareTo(o.asString());
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.value);
        return hash;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final StringValue other = (StringValue) obj;
        return Objects.equals(this.value, other.value);
    }
    
    @Override
    public String toString() {
        return asString();
    }
}
