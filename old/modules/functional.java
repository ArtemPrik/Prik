package prik.lib.modules;

import java.util.*;
import prik.exceptions.ArgumentsMismatchException;
import prik.exceptions.TypeException;
import prik.lib.*;


/**
 *
 * @author Professional
 */
public final class functional implements Module {
    public static void initConstants() {
        Variables.define("IDENTITY", new FunctionValue(args -> args[0]));
    }

    @Override
    public void init() {
        initConstants();
        Functions.set("foreach", new functional_foreach());
        Functions.set("map", new functional_map());
        Functions.set("flatmap", new functional_flatmap());
        Functions.set("reduce", new functional_reduce());
        Functions.set("filter", new functional_filter(false));
        Functions.set("sortBy", new functional_sortby());
        Functions.set("takewhile", new functional_filter(true));
        Functions.set("dropwhile", new functional_dropwhile());

        Functions.set("chain", new functional_chain());
//        Functions.set("stream", new functional_stream());
        Functions.set("combine", new functional_combine());
    }
    
    public final class functional_chain implements Function {
        @Override
        public Value execute(Value... args) {
            Arguments.checkAtLeast(2, args.length);

            Value result = args[0];
            for (int i = 1; i < args.length; i += 2) {
            final Function function = ValueUtils.consumeFunction(args[i], i);
                result = function.execute(result, args[i+1]);
            }
            return result;
        }
    }
    
    public final class functional_combine implements Function {
        @Override
        public Value execute(Value... args) {
            Arguments.checkAtLeast(1, args.length);
            Function result = null;
            for (Value arg : args) {
                if (arg.type() != Types.FUNCTION) {
                    throw new TypeException(arg.toString() + " is not a function");
                }
                final Function current = result;
                final Function next = ((FunctionValue) arg).getValue();
                result = fArgs -> {
                    if (current == null) return next.execute(fArgs);
                    return next.execute(current.execute(fArgs));
                };
            }
            return new FunctionValue(result);
        }
    }
    
    public final class functional_dropwhile implements Function {
        @Override
        public Value execute(Value... args) {
            Arguments.check(2, args.length);
            if (args[0].type() != Types.ARRAY) {
                throw new TypeException("Array expected in first argument");
            }
            final Value container = args[0];
            final Function predicate = ValueUtils.consumeFunction(args[1], 1);
            return dropWhileArray((ArrayValue) container, predicate);
        }

        private Value dropWhileArray(ArrayValue array, Function predicate) {
            int skipCount = 0;
            for (Value value : array) {
                if (predicate.execute(value) != NumberValue.ZERO)
                    skipCount++;
                else break;
            }
            
            final int size = array.size();
            final Value[] result = new Value[size - skipCount];
            System.arraycopy(array.getCopyElements(), skipCount, result, 0, result.length);
            return new ArrayValue(result);
        }
    }
    
    public final class functional_filter implements Function {

        private final boolean takeWhile;

        public functional_filter(boolean takeWhile) {
            this.takeWhile = takeWhile;
        }

        @Override
        public Value execute(Value... args) {
            Arguments.check(2, args.length);
            final Value container = args[0];
            final Function predicate = ValueUtils.consumeFunction(args[1], 1);
            if (container.type() == Types.ARRAY) {
                return filterArray((ArrayValue) container, predicate, takeWhile);
            }

            if (container.type() == Types.MAP) {
                return filterMap((MapValue) container, predicate, takeWhile);
            }

            throw new TypeException("Invalid first argument. Array or map expected");
        }

        private Value filterArray(ArrayValue array, Function predicate, boolean takeWhile) {
            final int size = array.size();
            final List<Value> values = new ArrayList<>(size);
            for (Value value : array) {
                if (predicate.execute(value) != NumberValue.ZERO) {
                    values.add(value);
                } else if (takeWhile) break;
            }
            final int newSize = values.size();
            return new ArrayValue(values.toArray(new Value[newSize]));
        }

        private Value filterMap(MapValue map, Function predicate, boolean takeWhile) {
            final MapValue result = new MapValue(map.size());
            for (Map.Entry<Value, Value> element : map) {
                if (predicate.execute(element.getKey(), element.getValue()) != NumberValue.ZERO) {
                    result.set(element.getKey(), element.getValue());
                } else if (takeWhile) break;
            }
            return result;
        }
    }
    
    public final class functional_flatmap implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.check(2, args.length);
            if (args[0].type() != Types.ARRAY) {
                throw new TypeException("Array expected in first argument");
            }
            final Function mapper = ValueUtils.consumeFunction(args[1], 1);
            return flatMapArray((ArrayValue) args[0], mapper);
        }

        private Value flatMapArray(ArrayValue array, Function mapper) {
            final List<Value> values = new ArrayList<>();
            final int size = array.size();
            for (int i = 0; i < size; i++) {
                final Value inner = mapper.execute(array.get(i));
                if (inner.type() != Types.ARRAY) {
                    throw new TypeException("Array expected " + inner);
                }
                for (Value value : (ArrayValue) inner) {
                    values.add(value);
                }
            }
            return new ArrayValue(values);
        }
    }
    
    public final class functional_foreach implements Function {

        private static final int UNKNOWN = -1;

        @Override
        public Value execute(Value... args) {
            Arguments.check(2, args.length);
            final Value container = args[0];
            final Function consumer = ValueUtils.consumeFunction(args[1], 1);
            final int argsCount;
            if (consumer instanceof UserDefinedFunction) {
                argsCount = ((UserDefinedFunction) consumer).getArgsCount();
            } else {
                argsCount = UNKNOWN;
            }

            switch (container.type()) {
                case Types.STRING:
                    final StringValue string = (StringValue) container;
                    if (argsCount == 2) {
                        for (char ch : string.asString().toCharArray()) {
                            consumer.execute(new StringValue(String.valueOf(ch)), NumberValue.of(ch));
                        }
                    } else {
                        for (char ch : string.asString().toCharArray()) {
                            consumer.execute(new StringValue(String.valueOf(ch)));
                        }
                    }
                    return string;

                case Types.ARRAY:
                    final ArrayValue array = (ArrayValue) container;
                    if (argsCount == 2) {
                        int index = 0;
                        for (Value element : array) {
                            consumer.execute(element, NumberValue.of(index++));
                        }
                    } else {
                        for (Value element : array) {
                            consumer.execute(element);
                        }
                    }
                    return array;

                case Types.MAP:
                    final MapValue map = (MapValue) container;
                    for (Map.Entry<Value, Value> element : map) {
                        consumer.execute(element.getKey(), element.getValue());
                    }
                    return map;

                default:
                    throw new TypeException("Cannot iterate " + Types.typeToString(container.type()));
            }
        }
    }
    
    public final class functional_map implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.checkOrOr(2, 3, args.length);

            final Value container = args[0];
            if (container.type() == Types.ARRAY) {
                final Function mapper = ValueUtils.consumeFunction(args[1], 1);
                return mapArray((ArrayValue) container, mapper);
            }

            if (container.type() == Types.MAP) {
                final Function keyMapper = ValueUtils.consumeFunction(args[1], 1);
                final Function valueMapper = ValueUtils.consumeFunction(args[2], 2);
                return mapMap((MapValue) container, keyMapper, valueMapper);
            }

            throw new TypeException("Invalid first argument. Array or map expected");
        }

        private Value mapArray(ArrayValue array, Function mapper) {
            final int size = array.size();
            final ArrayValue result = new ArrayValue(size);
            for (int i = 0; i < size; i++) {
                result.set(i, mapper.execute(array.get(i)));
            }
            return result;
        }

        private Value mapMap(MapValue map, Function keyMapper, Function valueMapper) {
            final MapValue result = new MapValue(map.size());
            for (Map.Entry<Value, Value> element : map) {
                final Value newKey = keyMapper.execute(element.getKey());
                final Value newValue = valueMapper.execute(element.getValue());
                result.set(newKey, newValue);
            }
            return result;
        }
    }
    
    public final class functional_reduce implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.check(3, args.length);

            final Value container = args[0];
            final Value identity = args[1];
            final Function accumulator = ValueUtils.consumeFunction(args[2], 2);
            if (container.type() == Types.ARRAY) {
                Value result = identity;
                final ArrayValue array = (ArrayValue) container;
                for (Value element : array) {
                    result = accumulator.execute(result, element);
                }
                return result;
            }
            if (container.type() == Types.MAP) {
                Value result = identity;
                final MapValue map = (MapValue) container;
                for (Map.Entry<Value, Value> element : map) {
                    result = accumulator.execute(result, element.getKey(), element.getValue());
                }
                return result;
            }
            throw new TypeException("Invalid first argument. Array or map expected");
        }
    }
    
    public final class functional_sortby implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.check(2, args.length);
            if (args[0].type() != Types.ARRAY) {
                throw new TypeException("Array expected at first argument");
            }

            final Value[] elements = ((ArrayValue) args[0]).getCopyElements();
            final Function function = ValueUtils.consumeFunction(args[1], 1);
            Arrays.sort(elements, (o1, o2) -> function.execute(o1).compareTo(function.execute(o2)));
            return new ArrayValue(elements);
        }
    }
}