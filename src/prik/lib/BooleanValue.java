package prik.lib;


/**
 *
 * @author Professional
 */
public final class BooleanValue implements Value {
    private final boolean bool_value;

    public BooleanValue(boolean bool_value) {
        this.bool_value = bool_value;
    }
    
    @Override
    public double asNumber() {
        if (bool_value == true) return 1;
        else return 0;
    }

    @Override
    public String asString() {
        if (bool_value == true) return "true";
        else return "false";
    }

    @Override
    public int asInt() {
        if (bool_value == true) return 1;
        else return 0;
    }

    @Override
    public int type() {
        return Types.BOOLEAN;
    }

    @Override
    public Object raw() {
        return bool_value;
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
