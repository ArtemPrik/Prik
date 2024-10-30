package prik.lib.modules.org.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;
import prik.lib.*;
import prik.lib.modules.Module;

/**
 *
 * @author Professional
 */
public class Json implements Module {
    @Override
    public void init() {
        MapValue map = new MapValue(2);
        map.set("encode", new json_encode());
        map.set("decode", new json_decode());
        Variables.define("Json", map);
    }
    
    public final class json_encode implements Function {

        @Override
        public Value execute(Value[] args) {
            Arguments.checkOrOr(1, 2, args.length);
            try {
                final int indent;
                if (args.length == 2) {
                    indent = args[1].asInt();
                } else {
                    indent = 0;
                }

                final String jsonRaw;
                if (indent > 0) {
                    jsonRaw = format(args[0], indent);
                } else {
                    final Object root = ValueUtils.toObject(args[0]);
                    jsonRaw = JSONWriter.valueToString(root);
                }
                return new StringValue(jsonRaw);

            } catch (JSONException ex) {
                throw new RuntimeException("Error while creating json", ex);
            }
        }

        private String format(Value val, int indent) {
            switch (val.type()) {
                case Types.ARRAY:
                    return ValueUtils.toObject((ArrayValue) val).toString(indent);
                case Types.MAP:
                    return ValueUtils.toObject((MapValue) val).toString(indent);
                case Types.NUMBER:
                    return JSONWriter.valueToString(val.raw());
                case Types.STRING:
                    return JSONWriter.valueToString(val.asString());
                default:
                    return JSONWriter.valueToString(JSONObject.NULL);
            }
        }
    }
    
    public final class json_decode implements Function {

        @Override
        public Value execute(Value... args) {
            Arguments.check(1, args.length);
            try {
                final String jsonRaw = args[0].asString();
                final Object root = new JSONTokener(jsonRaw).nextValue();
                return ValueUtils.toValue(root);
            } catch (JSONException ex) {
                throw new RuntimeException("Error while parsing json", ex);
            }
        }
    }
}
