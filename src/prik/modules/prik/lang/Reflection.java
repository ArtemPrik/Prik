package prik.modules.prik.lang;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import prik.exceptions.PrikException;
import prik.lib.*;
import prik.modules.Module;

/**
 *
 * @author Professional
 */
public final class Reflection implements Module {
    private static final Value NULL = new NullValue();

    @Override
    public void init() {
        Variables.define("boolean.class", new ClassValue(boolean.class));
        Variables.define("boolean[].class", new ClassValue(boolean[].class));
        Variables.define("boolean[][].class", new ClassValue(boolean[][].class));
        Variables.define("byte.class", new ClassValue(byte.class));
        Variables.define("byte[].class", new ClassValue(byte[].class));
        Variables.define("byte[][].class", new ClassValue(byte[][].class));
        Variables.define("short.class", new ClassValue(short.class));
        Variables.define("short[].class", new ClassValue(short[].class));
        Variables.define("short[][].class", new ClassValue(short[][].class));
        Variables.define("char.class", new ClassValue(char.class));
        Variables.define("char[].class", new ClassValue(char[].class));
        Variables.define("char[][].class", new ClassValue(char[][].class));
        Variables.define("int.class", new ClassValue(int.class));
        Variables.define("int[].class", new ClassValue(int[].class));
        Variables.define("int[][].class", new ClassValue(int[][].class));
        Variables.define("long.class", new ClassValue(long.class));
        Variables.define("long[].class", new ClassValue(long[].class));
        Variables.define("long[][].class", new ClassValue(long[][].class));
        Variables.define("float.class", new ClassValue(float.class));
        Variables.define("float[].class", new ClassValue(float[].class));
        Variables.define("float[][].class", new ClassValue(float[][].class));
        Variables.define("double.class", new ClassValue(double.class));
        Variables.define("double[].class", new ClassValue(double[].class));
        Variables.define("double[][].class", new ClassValue(double[][].class));
        Variables.define("String.class", new ClassValue(String.class));
        Variables.define("String[].class", new ClassValue(String[].class));
        Variables.define("String[][].class", new ClassValue(String[][].class));
        Variables.define("Object.class", new ClassValue(Object.class));
        Variables.define("Object[].class", new ClassValue(Object[].class));
        Variables.define("Object[][].class", new ClassValue(Object[][].class));

        Functions.set("isNull", this::isNull);
        Functions.set("JavaClass", this::newClass);
        Functions.set("toObject", this::toObject);
        Functions.set("toValue", this::toValue);
    }

    //<editor-fold defaultstate="collapsed" desc="Values">

    private static class ClassValue extends MapValue implements Instantiable {

        public static Value classOrNull(Class<?> clazz) {
            if (clazz == null) return NULL;
            return new ClassValue(clazz);
        }

        private final Class<?> clazz;

        public ClassValue(Class<?> clazz) {
            super(25);
            this.clazz = clazz;
            init(clazz);
        }

        private void init(Class<?> clazz) {
            set("isAnnotation", new BooleanValue(clazz.isAnnotation()));
            set("isAnonymousClass", new BooleanValue(clazz.isAnonymousClass()));
            set("isArray", new BooleanValue(clazz.isArray()));
            set("isEnum", new BooleanValue(clazz.isEnum()));
            set("isInterface", new BooleanValue(clazz.isInterface()));
            set("isLocalClass", new BooleanValue(clazz.isLocalClass()));
            set("isMemberClass", new BooleanValue(clazz.isMemberClass()));
            set("isPrimitive", new BooleanValue(clazz.isPrimitive()));
            set("isSynthetic", new BooleanValue(clazz.isSynthetic()));

            set("modifiers", NumberValue.of(clazz.getModifiers()));

            set("canonicalName", new StringValue(clazz.getCanonicalName()));
            set("name", new StringValue(clazz.getName()));
            set("simpleName", new StringValue(clazz.getSimpleName()));
            set("typeName", new StringValue(clazz.getTypeName()));
            set("genericString", new StringValue(clazz.toGenericString()));

            set("getComponentType", new FunctionValue(v -> classOrNull(clazz.getComponentType()) ));
            set("getDeclaringClass", new FunctionValue(v -> classOrNull(clazz.getDeclaringClass()) ));
            set("getEnclosingClass", new FunctionValue(v -> classOrNull(clazz.getEnclosingClass()) ));
            set("getSuperclass", new FunctionValue(v -> new ClassValue(clazz.getSuperclass()) ));

            set("getClasses", new FunctionValue(v -> array(clazz.getClasses()) ));
            set("getDeclaredClasses", new FunctionValue(v -> array(clazz.getDeclaredClasses()) ));
            set("getInterfaces", new FunctionValue(v -> array(clazz.getInterfaces()) ));

            set("asSubclass", new FunctionValue(this::asSubclass));
            set("isAssignableFrom", new FunctionValue(this::isAssignableFrom));
            set("new", new FunctionValue(this::newInstance));
            set("cast", new FunctionValue(this::cast));
        }

        private Value asSubclass(Value... args) {
            Arguments.check(1, args.length);
            return new ClassValue(clazz.asSubclass( ((ClassValue)args[0]).clazz ));
        }

        private Value isAssignableFrom(Value[] args) {
            Arguments.check(1, args.length);
            return NumberValue.fromBoolean(clazz.isAssignableFrom( ((ClassValue)args[0]).clazz ));
        }

        @Override
        public Value newInstance(Value[] args) {
            return findConstructorAndInstantiate(args, clazz.getConstructors());
        }

        private Value cast(Value... args) {
            Arguments.check(1, args.length);
            return objectToValue(clazz, clazz.cast(((ObjectValue)args[0]).object));
        }

        @Override
        public boolean containsKey(Value key) {
            return getValue(clazz, null, key.asString()) != null;
        }

        @Override
        public Value get(Value key) {
            if (super.containsKey(key)) {
                return super.get(key);
            }
            return getValue(clazz, null, key.asString());
        }

        @Override
        public java.lang.String toString() {
            return "ClassValue " + clazz.toString();
        }
    }

    private static class ObjectValue extends MapValue {

        public static Value objectOrNull(Object object) {
            if (object == null) return NULL;
            return new ObjectValue(object);
        }

        private final Object object;

        public ObjectValue(Object object) {
            super(2);
            this.object = object;
        }

        @Override
        public boolean containsKey(Value key) {
            return getValue(object.getClass(), object, key.asString()) != null;
        }

        @Override
        public Value get(Value key) {
            return getValue(object.getClass(), object, key.asString());
        }

        @Override
        public java.lang.String asString() {
            return object.toString();
        }

        @Override
        public java.lang.String toString() {
            return "ObjectValue " + asString();
        }
    }
//</editor-fold>

    private Value isNull(Value... args) {
        Arguments.checkAtLeast(1, args.length);
        for (Value arg : args) {
            if (arg.raw() == null) return NumberValue.ONE;
        }
        return NumberValue.ZERO;
    }

    private Value newClass(Value... args) {
        Arguments.check(1, args.length);

        final java.lang.String className = args[0].asString();
        try {
            return new ClassValue(Class.forName(className));
        } catch (ClassNotFoundException ce) {
            return NULL;
        }
    }

    private Value toObject(Value... args) {
        Arguments.check(1, args.length);
        if (args[0] == NULL) return NULL;
        return new ObjectValue(valueToObject(args[0]));
    }

    private Value toValue(Value... args) {
        Arguments.check(1, args.length);
        if (args[0] instanceof ObjectValue) {
            return objectToValue( ((ObjectValue) args[0]).object );
        }
        return NULL;
    }


    //<editor-fold defaultstate="collapsed" desc="Helpers">
    private static Value getValue(Class<?> clazz, Object object, java.lang.String key) {
        // Trying to get field
        try {
            final Field field = clazz.getField(key);
            return objectToValue(field.getType(), field.get(object));
        } catch (NoSuchFieldException | SecurityException |
                IllegalArgumentException | IllegalAccessException ex) {
            // ignore and go to the next step
        }

        // Trying to invoke method
        try {
            final Method[] allMethods = clazz.getMethods();
            final List<Method> methods = new ArrayList<>();
            for (Method method : allMethods) {
                if (method.getName().equals(key)) {
                    methods.add(method);
                }
            }
            if (methods.isEmpty()) {
                return FunctionValue.EMPTY;
            }
            return new FunctionValue(methodsToFunction(object, methods));
        } catch (SecurityException ex) {
            // ignore and go to the next step
        }

        return NULL;
    }
    
    private static Value findConstructorAndInstantiate(Value[] args, Constructor<?>[] ctors) {
        for (Constructor<?> ctor : ctors) {
            if (ctor.getParameterCount() != args.length) continue;
            if (!isMatch(args, ctor.getParameterTypes())) continue;
            try {
                final Object result = ctor.newInstance(valuesToObjects(args));
                return new ObjectValue(result);
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException ex) {
                // skip
            }
        }
        return null;
    }

    private static Function methodsToFunction(Object object, List<Method> methods) {
        return (args) -> {
            for (Method method : methods) {
                if (method.getParameterCount() != args.length) continue;
                if (!isMatch(args, method.getParameterTypes())) continue;
                try {
                    final Object result = method.invoke(object, valuesToObjects(args));
                    if (method.getReturnType() != void.class) {
                        return objectToValue(result);
                    }
                    return NumberValue.ONE;
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    // skip
                }
            }
            return null;
        };
    }
    
    private static boolean isMatch(Value[] args, Class<?>[] types) {
        for (int i = 0; i < args.length; i++) {
            final Value arg = args[i];
            final Class<?> clazz = types[i];

            if (arg == NULL) continue;

            final Class<?> unboxed = unboxed(clazz);
            boolean assignable = unboxed != null;
            final Object object = valueToObject(arg);
            assignable = assignable && (object != null);
            assignable = assignable && (unboxed.isAssignableFrom(object.getClass()));
            if (assignable) continue;

            return false;
        }
        return true;
    }

    private static Class<?> unboxed(Class<?> clazz) {
        if (clazz == null) return null;
        if (clazz.isPrimitive()) {
            if (int.class == clazz) return Integer.class;
            if (boolean.class == clazz) return Boolean.class;
            if (double.class == clazz) return Double.class;
            if (float.class == clazz) return Float.class;
            if (long.class == clazz) return Long.class;
            if (byte.class == clazz) return Byte.class;
            if (char.class == clazz) return Character.class;
            if (short.class == clazz) return Short.class;
            if (void.class == clazz) return Void.class;
        }
        return clazz;
    }

    private static ArrayValue array(Class<?>[] classes) {
        final ArrayValue result = new ArrayValue(classes.length);
        for (int i = 0; i < classes.length; i++) {
            result.set(i, ClassValue.classOrNull(classes[i]));
        }
        return result;
    }

    private static Value objectToValue(Object o) {
        if (o == null) return NULL;
        return objectToValue(o.getClass(), o);
    }

    private static Value objectToValue(Class<?> clazz, Object o) {
        if (o == null || o == NULL) return NULL;
        if (clazz.isPrimitive()) {
            if (int.class.isAssignableFrom(clazz))
                return NumberValue.of((int) o);
            if (boolean.class.isAssignableFrom(clazz))
                return NumberValue.fromBoolean((boolean) o);
            if (double.class.isAssignableFrom(clazz))
                return NumberValue.of((double) o);
            if (float.class.isAssignableFrom(clazz))
                return NumberValue.of((float) o);
            if (long.class.isAssignableFrom(clazz))
                return NumberValue.of((long) o);
            if (byte.class.isAssignableFrom(clazz))
                return NumberValue.of((byte) o);
            if (char.class.isAssignableFrom(clazz))
                return NumberValue.of((char) o);
            if (short.class.isAssignableFrom(clazz))
                return NumberValue.of((short) o);
        }
        if (Number.class.isAssignableFrom(clazz)) {
            return NumberValue.of((Number) o);
        }
        if (String.class.isAssignableFrom(clazz)) {
            return new StringValue((java.lang.String) o);
        }
        if (CharSequence.class.isAssignableFrom(clazz)) {
            return new StringValue( ((CharSequence) o).toString() );
        }
        if (o instanceof Value) {
            return (Value) o;
        }
        if (clazz.isArray()) {
            return arrayToValue(clazz, o);
        }
        final Class<?> componentType = clazz.getComponentType();
        if (componentType != null) {
            return objectToValue(componentType, o);
        }
        return new ObjectValue(o);
    }

    private static Value arrayToValue(Class<?> clazz, Object o) {
        final int length = Array.getLength(o);
        final ArrayValue result = new ArrayValue(length);
        final Class<?> componentType = clazz.getComponentType();
        int i = 0;
        if (boolean.class.isAssignableFrom(componentType)) {
            for (boolean element : (boolean[]) o) {
                result.set(i++, NumberValue.fromBoolean(element));
            }
        } else if (byte.class.isAssignableFrom(componentType)) {
            for (byte element : (byte[]) o) {
                result.set(i++, NumberValue.of(element));
            }
        } else if (char.class.isAssignableFrom(componentType)) {
            for (char element : (char[]) o) {
                result.set(i++, NumberValue.of(element));
            }
        } else if (double.class.isAssignableFrom(componentType)) {
            for (double element : (double[]) o) {
                result.set(i++, NumberValue.of(element));
            }
        } else if (float.class.isAssignableFrom(componentType)) {
            for (float element : (float[]) o) {
                result.set(i++, NumberValue.of(element));
            }
        } else if (int.class.isAssignableFrom(componentType)) {
            for (int element : (int[]) o) {
                result.set(i++, NumberValue.of(element));
            }
        } else if (long.class.isAssignableFrom(componentType)) {
            for (long element : (long[]) o) {
                result.set(i++, NumberValue.of(element));
            }
        } else if (short.class.isAssignableFrom(componentType)) {
            for (short element : (short[]) o) {
                result.set(i++, NumberValue.of(element));
            }
        } else {
            for (Object element : (Object[]) o) {
                result.set(i++, objectToValue(element));
            }
        }
        return result;
    }

    private static Object[] valuesToObjects(Value[] args) {
        Object[] result = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = valueToObject(args[i]);
        }
        return result;
    }

    private static Object valueToObject(Value value) {
        if (value == NULL) return null;
        switch (value.type()) {
            case Types.NUMBER:
                return value.raw();
            case Types.STRING:
                return value.asString();
            case Types.ARRAY:
                return arrayToObject((ArrayValue) value);
        }
        if (value instanceof ObjectValue) {
            return ((ObjectValue) value).object;
        }
        if (value instanceof ClassValue) {
            return ((ClassValue) value).clazz;
        }
        return value.raw();
    }

    private static Object arrayToObject(ArrayValue value) {
        final int size = value.size();
        final Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = valueToObject(value.get(i));
        }
        return result;
    }
//</editor-fold>
}
