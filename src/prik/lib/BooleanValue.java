package prik.lib;


/**
 *
 * @author Professional
 */
public final class BooleanValue implements Value {
    private final boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }
    
    @Override
    public double asNumber() {
        return (value == true) ? 1 : 0;
    }

    @Override
    public String asString() {
        return (value == true) ? "true" : "false";
    }

    @Override
    public int asInt() {
        return (value == true) ? 1 : 0;
    }

    @Override
    public int type() {
        return Types.BOOLEAN;
    }

    @Override
    public Object raw() {
        return value;
    }

    @Override
    public int compareTo(Value o) {
        return o.compareTo(o);
    }

    @Override
    public String toString() {
        return asString();
    }
}
