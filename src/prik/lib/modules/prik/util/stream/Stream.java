package prik.lib.modules.prik.util.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import prik.exceptions.*;
import prik.lib.Arguments;
import prik.lib.ArrayValue;
import prik.lib.Function;
import prik.lib.FunctionValue;
import prik.lib.Functions;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Types;
import prik.lib.UserDefinedFunction;
import prik.lib.Value;
import prik.lib.ValueUtils;
import prik.lib.modules.Module;

/**
 *
 * @author Professional
 */
public class Stream implements Module {
    @Override
    public void init() {
        Functions.set("stream", (Value... args) -> {
            Arguments.checkAtLeast(1, args.length);
            
            if (args.length > 1) {
                return new StreamValue(new ArrayValue(args));
            }
            
            final Value value = args[0];
            switch (value.type()) {
                case Types.MAP:
                    return new StreamValue(((MapValue) value).toPairs());
                case Types.ARRAY:
                    return new StreamValue((ArrayValue) value);
                default:
                    throw new TypeException("Invalid argument. Array or map expected");
            }
        });
    }
    
    private static class StreamValue extends MapValue {

        private final ArrayValue container;

        public StreamValue(ArrayValue container) {
            super(16);
            this.container = container;
            init();
        }

        private void init() {
            set("filter", wrapIntermediate(new functional_filter(false)));
            set("map", wrapIntermediate(new functional_map()));
            set("flatMap", wrapIntermediate(new functional_flatmap()));
            set("sorted", this::sorted);
            set("sortBy", wrapIntermediate(new functional_sortby()));
            set("takeWhile", wrapIntermediate(new functional_filter(true)));
            set("dropWhile", wrapIntermediate(new functional_dropwhile()));
            set("peek", wrapIntermediate(new functional_foreach()));
            set("skip", this::skip);
            set("limit", this::limit);
            set("custom", this::custom);

            set("reduce", wrapTerminal(new functional_reduce()));
            set("forEach", wrapTerminal(new functional_foreach()));
            set("toArray", args -> container);
            set("count", args -> NumberValue.of(container.size()));
        }

        private Value skip(Value... args) {
            Arguments.check(1, args.length);

            final int skipCount = args[0].asInt();
            final int size = container.size();

            if (skipCount <= 0) return this;
            if (skipCount >= size) {
                return new StreamValue(new ArrayValue(0));
            }

            final Value[] result = new Value[size - skipCount];
            System.arraycopy(container.getCopyElements(), skipCount, result, 0, result.length);
            return new StreamValue(new ArrayValue(result));
        }

        private Value limit(Value... args) {
            Arguments.check(1, args.length);

            final int limitCount = args[0].asInt();
            final int size = container.size();

            if (limitCount >= size) return this;
            if (limitCount <= 0) {
                return new StreamValue(new ArrayValue(0));
            }

            final Value[] result = new Value[limitCount];
            System.arraycopy(container.getCopyElements(), 0, result, 0, limitCount);
            return new StreamValue(new ArrayValue(result));
        }

        private Value sorted(Value... args) {
            Arguments.checkOrOr(0, 1, args.length);
            final Value[] elements = container.getCopyElements();

            switch (args.length) {
                case 0:
                    Arrays.sort(elements);
                    break;
                case 1:
                    final Function comparator = ValueUtils.consumeFunction(args[0], 0);
                    Arrays.sort(elements, (o1, o2) -> comparator.execute(o1, o2).asInt());
                    break;
                default:
                    throw new ArgumentsMismatchException("Wrong number of arguments");
            }

            return new StreamValue(new ArrayValue(elements));
        }

        private Value custom(Value... args) {
            Arguments.check(1, args.length);
            final Function f = ValueUtils.consumeFunction(args[0], 0);
            final Value result = f.execute(container);
            if (result.type() == Types.ARRAY) {
                return new StreamValue((ArrayValue) result);
            }
            return result;
        }

        private FunctionValue wrapIntermediate(Function f) {
            return wrap(f, true);
        }

        private FunctionValue wrapTerminal(Function f) {
            return wrap(f, false);
        }

        private FunctionValue wrap(Function f, boolean intermediate) {
            return new FunctionValue(args -> {
                final Value[] newArgs = new Value[args.length + 1];
                System.arraycopy(args, 0, newArgs, 1, args.length);
                newArgs[0] = container;
                final Value result = f.execute(newArgs);
                if (intermediate && result.type() == Types.ARRAY) {
                    return new StreamValue((ArrayValue) result);
                }
                return result;
            });
        }
    }
    
    public static class functional_filter implements Function {

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
    
    public static class functional_foreach implements Function {

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
    
    public static class functional_map implements Function {

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
    
    public static class functional_flatmap implements Function {

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
    
    public static class functional_sortby implements Function {

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
    
    public static class functional_dropwhile implements Function {

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
    
    public static class functional_reduce implements Function {

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
}
