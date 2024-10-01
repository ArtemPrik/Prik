package prik.lib.modules.prik.util;

import java.io.UnsupportedEncodingException;
import prik.lib.Arguments;
import prik.lib.ArrayValue;
import prik.lib.Functions;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Types;
import prik.lib.Value;
import prik.lib.ValueUtils;
import prik.lib.Variables;
import prik.lib.modules.Module;

/**
 *
 * @author Professional
 */
public final class Base64 implements Module {
    private static final int TYPE_URL_SAFE = 8;

    public static void initConstants() {
//        Variables.define("BASE64_URL_SAFE", NumberValue.of(TYPE_URL_SAFE));
        Variables.define("base64_url_safe", NumberValue.of(TYPE_URL_SAFE));
    }

    @Override
    public void init() {
        initConstants();
        Functions.set("base64encode", this::base64encode);
        Functions.set("base64decode", this::base64decode);
        Functions.set("base64encodeToString", this::base64encodeToString);
    }

    private Value base64encode(Value... args) {
        Arguments.checkOrOr(1, 2, args.length);
        return ArrayValue.of(getEncoder(args).encode(getInputToEncode(args)));
    }

    private Value base64encodeToString(Value... args) {
        Arguments.checkOrOr(1, 2, args.length);
        return new StringValue(getEncoder(args).encodeToString(getInputToEncode(args)));
    }

    private Value base64decode(Value... args) {
        Arguments.checkOrOr(1, 2, args.length);
        final java.util.Base64.Decoder decoder = getDecoder(args);
        final byte[] result;
        if (args[0].type() == Types.ARRAY) {
            result = decoder.decode(ValueUtils.toByteArray((ArrayValue) args[0]));
        } else {
            result = decoder.decode(args[0].asString());
        }
        return ArrayValue.of(result);
    }


    private byte[] getInputToEncode(Value[] args) {
        byte[] input;
        if (args[0].type() == Types.ARRAY) {
            input = ValueUtils.toByteArray((ArrayValue) args[0]);
        } else {
            try {
                input = args[0].asString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException ex) {
                input = args[0].asString().getBytes();
            }
        }
        return input;
    }

    private java.util.Base64.Encoder getEncoder(Value[] args) {
        if (args.length == 2 && args[1].asInt() == TYPE_URL_SAFE) {
            return java.util.Base64.getUrlEncoder();
        }
        return java.util.Base64.getEncoder();
    }

    private java.util.Base64.Decoder getDecoder(Value[] args) {
        if (args.length == 2 && args[1].asInt() == TYPE_URL_SAFE) {
            return java.util.Base64.getUrlDecoder();
        }
        return java.util.Base64.getDecoder();
    }
}
