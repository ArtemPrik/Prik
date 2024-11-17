package prik.lib;

import java.util.Objects;

/**
 *
 * @author Professional
 */
public final class NumberValue implements Value {
    private static final int CACHE_MIN = -128;
    private static final int CACHE_MAX = 127;

    public static final NumberValue MINUS_ONE;
    public static final NumberValue ZERO;
    public static final NumberValue ONE;

    private static final NumberValue[] NUMBER_CACHE;
    static {
        final int length = CACHE_MAX - CACHE_MIN + 1;
        NUMBER_CACHE = new NumberValue[length];
        int value = CACHE_MIN;
        for (int i = 0; i < length; i++) {
            NUMBER_CACHE[i] = new NumberValue(value++);
        }

        final int zeroIndex = -CACHE_MIN;
        MINUS_ONE = NUMBER_CACHE[zeroIndex - 1];
        ZERO = NUMBER_CACHE[zeroIndex];
        ONE = NUMBER_CACHE[zeroIndex + 1];
    }
    
    public static NumberValue fromBoolean(boolean b) {
        return b ? ONE : ZERO;
    }
    
    public static NumberValue of(int value) {
        if (CACHE_MIN <= value && value <= CACHE_MAX) {
            return NUMBER_CACHE[-CACHE_MIN + value];
        }
        return new NumberValue(value);
    }

    public static NumberValue of(Number value) {
        return new NumberValue(value);
    }
    
    public NumberValue(Number value) {
        this.value = value;
    }
    
    @Override
    public double asNumber() {
        return value.doubleValue();
    }

    @Override
    public String asString() {
        return value.toString();
    }
    
    private final Number value;
    
    @Override
    public Number raw() {
        return value;
    }

    @Override
    public int asInt() {
        return value.intValue();
    }
    
    public long asLong() {
        return value.longValue();
    }
    
    public float asFloat() {
        return value.floatValue();
    }

    @Override
    public int type() {
        return Types.NUMBER;
    }
    
    @Override
    public int compareTo(Value o) {
        if (o.type() == Types.NUMBER) {
            final Number other = ((NumberValue) o).value;
            if (value instanceof Double || other instanceof Double) {
                return Double.compare(value.doubleValue(), other.doubleValue());
            }
            if (value instanceof Float || other instanceof Float) {
                return Float.compare(value.floatValue(), other.floatValue());
            }
            if (value instanceof Long || other instanceof Long) {
                return Long.compare(value.longValue(), other.longValue());
            }
            return Integer.compare(value.intValue(), other.intValue());
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
        final NumberValue other = (NumberValue) obj;
        return Objects.equals(this.value, other.value);
    }
    
    @Override
    public String toString() {
        return asString();
    }
}
