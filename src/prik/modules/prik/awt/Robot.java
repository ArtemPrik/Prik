package prik.modules.prik.awt;

import java.awt.AWTException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import prik.lib.Arguments;
import prik.lib.ArrayValue;
import prik.lib.Function;
import prik.lib.Functions;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Types;
import prik.lib.Value;
import prik.lib.Variables;

/**
 *
 * @author Professional
 */
public final class Robot implements prik.modules.Module {
    private static final int CLICK_DELAY = 200;
    private static final int TYPING_DELAY = 50;
    
    private static final Map<Character, Integer> SYMBOL_CODES;
    static {
        SYMBOL_CODES = new HashMap<>(10);
        SYMBOL_CODES.put('_', KeyEvent.VK_MINUS);
        SYMBOL_CODES.put(':', KeyEvent.VK_SEMICOLON);
    }
    
    private static java.awt.Robot awtRobot;

    @Override
    public void init() {
        MapValue map = new MapValue(20);
        
        boolean isRobotInitialized = false;
        try {
            isRobotInitialized = initialize();
        } catch (AWTException ex) {
            Logger.getLogger(prik.modules.prik.awt.Robot.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (isRobotInitialized) {
            map.set("click", convertFunction(prik.modules.prik.awt.Robot::click));
            map.set("delay", convertFunction(awtRobot::delay));
            map.set("setAutoDelay", convertFunction(awtRobot::setAutoDelay));
            map.set("keyPress", convertFunction(awtRobot::keyPress));
            map.set("keyRelease", convertFunction(awtRobot::keyRelease));
            map.set("mousePress", convertFunction(awtRobot::mousePress));
            map.set("mouseRelease", convertFunction(awtRobot::mouseRelease));
            map.set("mouseWheel", convertFunction(awtRobot::mouseWheel));
            map.set("mouseMove", (args) -> {
                Arguments.check(2, args.length);
                try {
                    awtRobot.mouseMove(args[0].asInt(), args[1].asInt());
                } catch (IllegalArgumentException iae) { }
                return NumberValue.ZERO;
            });
            map.set("typeText", (args) -> {
                Arguments.check(1, args.length);
                try {
                    typeText(args[0].asString());
                } catch (IllegalArgumentException iae) { }
                return NumberValue.ZERO;
            });
            map.set("toClipboard", new robot_toclipboard());
            map.set("fromClipboard", new robot_fromclipboard());
        }
        map.set("execProcess", new robot_exec(robot_exec.Mode.EXEC));
        map.set("execProcessAndWait", new robot_exec(robot_exec.Mode.EXEC_AND_WAIT));
        
        Variables.define("Robot", map);
        
        Variables.define("button1", NumberValue.of(InputEvent.BUTTON1_MASK));
        Variables.define("button2", NumberValue.of(InputEvent.BUTTON2_MASK));
        Variables.define("button3", NumberValue.of(InputEvent.BUTTON3_MASK));
    }
    
    private static boolean initialize() throws AWTException {
        awtRobot = new java.awt.Robot(); //throw new RuntimeException("Unable to create robot instance", awte);
        return true;
    }
    
    private static Function convertFunction(IntConsumer consumer) {
        return args -> {
            Arguments.check(1, args.length);
            try {
                consumer.accept(args[0].asInt());
            } catch (IllegalArgumentException iae) { }
            return NumberValue.ZERO;
        };
    }
    
    private static synchronized void click(int buttons) {
        awtRobot.mousePress(buttons);
        awtRobot.delay(CLICK_DELAY);
        awtRobot.mouseRelease(buttons);
    }
 
    private static synchronized void typeText(String text) {
        for (char ch : text.toCharArray()) {
            typeSymbol(ch);
        }
    }

    private static void typeSymbol(char ch) {
        int code = KeyEvent.getExtendedKeyCodeForChar(ch);

        boolean isUpperCase = Character.isLetter(ch) && Character.isUpperCase(ch);
        boolean needPressShift = isUpperCase;
        if (!isUpperCase) {
            final int symbolIndex = "!@#$%^&*()".indexOf(ch);
            if (symbolIndex != -1) {
                needPressShift = true;
                code = '1' + symbolIndex;
            } else if (SYMBOL_CODES.containsKey(ch)) {
                needPressShift = true;
                code = SYMBOL_CODES.get(ch);
            }
        }

        if (code == KeyEvent.VK_UNDEFINED) return;

        if (needPressShift) {
            // press shift
            awtRobot.keyPress(KeyEvent.VK_SHIFT);
            awtRobot.delay(TYPING_DELAY);
        }

        awtRobot.keyPress(code);
        awtRobot.delay(TYPING_DELAY);
        awtRobot.keyRelease(code);

        if (needPressShift) {
            // release shift
            awtRobot.delay(TYPING_DELAY);
            awtRobot.keyRelease(KeyEvent.VK_SHIFT);
            awtRobot.delay(TYPING_DELAY);
        }
    }
    
    // exec
    public final class robot_exec implements Function {
        public enum Mode { EXEC, EXEC_AND_WAIT }
    
        private final Mode mode;

        public robot_exec(Mode mode) {
            this.mode = mode;
        }

        @Override
        public Value execute(Value... args) {
            Arguments.checkAtLeast(1, args.length);

            try {
                final Process process;
                if (args.length > 1) {
                    process = Runtime.getRuntime().exec(toStringArray(args));
                } else switch (args[0].type()) {
                    case Types.ARRAY:
                        final ArrayValue array = (ArrayValue) args[0];
                        process = Runtime.getRuntime().exec(toStringArray(array.getCopyElements()));
                        break;

                    default:
                        process = Runtime.getRuntime().exec(args[0].asString());
                }

                switch (mode) {
                    case EXEC_AND_WAIT:
                        return NumberValue.of(process.waitFor());
                    case EXEC:
                    default:
                        return NumberValue.ZERO;
                }
            } catch (Exception ex) {
                return NumberValue.ZERO;
            }
        }

        private static String[] toStringArray(Value[] values) {
            final String[] strings = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                strings[i] = values[i].asString();
            }
            return strings;
        }
    }
    
    // to clip
    public final class robot_toclipboard implements Function {
        @Override
        public Value execute(Value... args) {
            Arguments.check(1, args.length);
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(args[0].asString()), null);
            return NumberValue.ZERO;
        }
    }
    
    // from clip
    public final class robot_fromclipboard implements Function {
        @Override
        public Value execute(Value... args) {
            try {
                Object data = Toolkit.getDefaultToolkit().getSystemClipboard()
                        .getData(DataFlavor.stringFlavor);
                return new StringValue(data.toString());
            } catch (Exception ex) {
                return StringValue.EMPTY;
            }
        }
    }
}
