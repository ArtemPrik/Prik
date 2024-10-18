package prik.lib;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import prik.exceptions.TypeException;

/**
 *
 * @author Professional
 */
public final class ArrayValue implements Value, Iterable<Value> {
    private final Value[] elements;

    public ArrayValue(int size) {
        this.elements = new Value[size];
    }

    public ArrayValue(Value[] elements) {
        this.elements = new Value[elements.length];
        System.arraycopy(elements, 0, this.elements, 0, elements.length);
    }
    
    public ArrayValue(List<Value> values) {
        final int size = values.size();
        this.elements = values.toArray(new Value[size]);
    }
    
    public ArrayValue(ArrayValue array) {
        this(array.elements);
    }
    
    public static ArrayValue of(byte[] array) {
        final int size = array.length;
        final ArrayValue result = new ArrayValue(size);
        for (int i = 0; i < size; i++) {
            result.set(i, NumberValue.of(array[i]));
        }
        return result;
    }

    public static ArrayValue of(String[] array) {
        final int size = array.length;
        final ArrayValue result = new ArrayValue(size);
        for (int i = 0; i < size; i++) {
            result.set(i, new StringValue(array[i]));
        }
        return result;
    }
    
    public Value[] getCopyElements() {
        final Value[] result = new Value[elements.length];
        System.arraycopy(elements, 0, result, 0, elements.length);
        return result;
    }
    
    public int size() {
        return elements.length;
    }
    
    public Value get(int index) {
        return elements[index];
    }

    public Value get(Value index) {
        final String prop = index.asString();
        return switch (prop) {
            case "length" -> NumberValue.of(size());
            case "isEmpty" -> new BooleanValue(size() == 0);
            default -> get(index.asInt());
        };
    }
    
    public void set(int index, Value value) {
        elements[index] = value;
    }
    
    @Override
    public double asNumber() {
        throw new TypeException("Cannot cast array to number");
    }

    @Override
    public String asString() {
        return Arrays.toString(elements);
    }

    @Override
    public String toString() {
        return asString();
    }
    
    @Override
    public Object raw() {
        return elements;
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast array to integer");
    }


    @Override
    public int type() {
        return Types.ARRAY;
    }
    
    @Override
    public int compareTo(Value o) {
        if (o.type() == Types.ARRAY) {
            final int lengthCompare = Integer.compare(size(), ((ArrayValue) o).size());
            if (lengthCompare != 0) return lengthCompare;
        }
        return asString().compareTo(o.asString());
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Arrays.deepHashCode(this.elements);
        return hash;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final ArrayValue other = (ArrayValue) obj;
        return Arrays.deepEquals(this.elements, other.elements);
    }

    @Override
    public Iterator<Value> iterator() {
        return Arrays.asList(elements).iterator();
    }
}
