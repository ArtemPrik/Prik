package prik.lib;


/**
 *
 * @author Professional
 */
public final class NullValue implements Value {
    @Override
    public Object raw() {
        return null;
    }

    @Override
    public int asInt() {
        return 0;
    }

    @Override
    public double asNumber() {
        return 0;
    }

    @Override
    public String asString() {
//        return "null";
        return "";
    }

    @Override
    public int type() {
        return 482862660;
    }

    @Override
    public int compareTo(Value o) {
        if (o.raw() == null) return 0;
        return -1;
    }

    @Override
    public String toString() {
        return asString();
    }
}
