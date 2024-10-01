package prik.lib.modules;

import java.awt.AWTException;
import java.awt.Robot;
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
import prik.lib.*;


/**
 *
 * @author Professional
 */
public final class robot implements Module {
    private static final int CLICK_DELAY = 200;
    private static final int TYPING_DELAY = 50;
    
    private static final Map<Character, Integer> SYMBOL_CODES;
    static {
        SYMBOL_CODES = new HashMap<>(10);
        SYMBOL_CODES.put('_', KeyEvent.VK_MINUS);
        SYMBOL_CODES.put(':', KeyEvent.VK_SEMICOLON);
    }
    
    private static Robot awtRobot;

    public static void initConstants() {
        MapValue keys = new MapValue(60);
        
        keys.set("DOWN", NumberValue.of(KeyEvent.VK_DOWN));
        keys.set("UP", NumberValue.of(KeyEvent.VK_DOWN));
        keys.set("LEFT", NumberValue.of(KeyEvent.VK_LEFT));
        keys.set("RIGHT", NumberValue.of(KeyEvent.VK_RIGHT));
        keys.set("FIRE", NumberValue.of(KeyEvent.VK_ENTER));
        keys.set("ESCAPE", NumberValue.of(KeyEvent.VK_ESCAPE));
        keys.set("TAB", NumberValue.of(KeyEvent.VK_TAB));
        keys.set("ALT", NumberValue.of(KeyEvent.VK_ALT));
        keys.set("SHIFT", NumberValue.of(KeyEvent.VK_SHIFT));
        keys.set("CAPS_LOCK", NumberValue.of(KeyEvent.VK_CAPS_LOCK));
        keys.set("CONTROL", NumberValue.of(KeyEvent.VK_CONTROL));
        keys.set("ENTER", NumberValue.of(KeyEvent.VK_ENTER));
        
        keys.set("=", NumberValue.of(KeyEvent.VK_EQUALS));
        
//        Variables.define("UP", NumberValue.of(KeyEvent.VK_UP));
//        Variables.define("DOWN", NumberValue.of(KeyEvent.VK_DOWN));
//        Variables.define("LEFT", NumberValue.of(KeyEvent.VK_LEFT));
//        Variables.define("RIGHT", NumberValue.of(KeyEvent.VK_RIGHT));

        keys.set("0", NumberValue.of(KeyEvent.VK_0));
        keys.set("1", NumberValue.of(KeyEvent.VK_1));
        keys.set("2", NumberValue.of(KeyEvent.VK_2));
        keys.set("3", NumberValue.of(KeyEvent.VK_3));
        keys.set("4", NumberValue.of(KeyEvent.VK_4));
        keys.set("5", NumberValue.of(KeyEvent.VK_5));
        keys.set("6", NumberValue.of(KeyEvent.VK_6));
        keys.set("7", NumberValue.of(KeyEvent.VK_7));
        keys.set("8", NumberValue.of(KeyEvent.VK_8));
        keys.set("9", NumberValue.of(KeyEvent.VK_9));

        keys.set("A", NumberValue.of(KeyEvent.VK_A));
        keys.set("B", NumberValue.of(KeyEvent.VK_B));
        keys.set("C", NumberValue.of(KeyEvent.VK_C));
        keys.set("D", NumberValue.of(KeyEvent.VK_D));
        keys.set("E", NumberValue.of(KeyEvent.VK_E));
        keys.set("F", NumberValue.of(KeyEvent.VK_F));
        keys.set("G", NumberValue.of(KeyEvent.VK_G));
        keys.set("H", NumberValue.of(KeyEvent.VK_H));
        keys.set("I", NumberValue.of(KeyEvent.VK_I));
        keys.set("J", NumberValue.of(KeyEvent.VK_J));
        keys.set("K", NumberValue.of(KeyEvent.VK_K));
        keys.set("L", NumberValue.of(KeyEvent.VK_L));
        keys.set("M", NumberValue.of(KeyEvent.VK_M));
        keys.set("N", NumberValue.of(KeyEvent.VK_N));
        keys.set("O", NumberValue.of(KeyEvent.VK_O));
        keys.set("P", NumberValue.of(KeyEvent.VK_P));
        keys.set("Q", NumberValue.of(KeyEvent.VK_Q));
        keys.set("R", NumberValue.of(KeyEvent.VK_R));
        keys.set("S", NumberValue.of(KeyEvent.VK_S));
        keys.set("T", NumberValue.of(KeyEvent.VK_T));
        keys.set("U", NumberValue.of(KeyEvent.VK_U));
        keys.set("V", NumberValue.of(KeyEvent.VK_V));
        keys.set("W", NumberValue.of(KeyEvent.VK_W));
        keys.set("X", NumberValue.of(KeyEvent.VK_X));
        keys.set("Y", NumberValue.of(KeyEvent.VK_Y));
        keys.set("Z", NumberValue.of(KeyEvent.VK_Z));

        keys.set("F1", NumberValue.of(KeyEvent.VK_F1));
        keys.set("F2", NumberValue.of(KeyEvent.VK_F2));
        keys.set("F3", NumberValue.of(KeyEvent.VK_F3));
        keys.set("F4", NumberValue.of(KeyEvent.VK_F4));
        keys.set("F5", NumberValue.of(KeyEvent.VK_F5));
        keys.set("F6", NumberValue.of(KeyEvent.VK_F6));
        keys.set("F7", NumberValue.of(KeyEvent.VK_F7));
        keys.set("F8", NumberValue.of(KeyEvent.VK_F8));
        keys.set("F9", NumberValue.of(KeyEvent.VK_F9));
        keys.set("F10", NumberValue.of(KeyEvent.VK_F10));
        keys.set("F11", NumberValue.of(KeyEvent.VK_F11));
        keys.set("F12", NumberValue.of(KeyEvent.VK_F12));
        
        Variables.define("keys", keys);
        
        Variables.define("button1", NumberValue.of(InputEvent.BUTTON1_MASK));
        Variables.define("button2", NumberValue.of(InputEvent.BUTTON2_MASK));
        Variables.define("button3", NumberValue.of(InputEvent.BUTTON3_MASK));
    }

    @Override
    public void init() {
        initConstants();
        boolean isRobotInitialized = false;
        try {
            isRobotInitialized = initialize();
        } catch (AWTException ex) {
            Logger.getLogger(robot.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (isRobotInitialized) {
            Functions.set("click", convertFunction(robot::click));
            Functions.set("delay", convertFunction(awtRobot::delay));
            Functions.set("setAutoDelay", convertFunction(awtRobot::setAutoDelay));
            Functions.set("keyPress", convertFunction(awtRobot::keyPress));
            Functions.set("keyRelease", convertFunction(awtRobot::keyRelease));
            Functions.set("mousePress", convertFunction(awtRobot::mousePress));
            Functions.set("mouseRelease", convertFunction(awtRobot::mouseRelease));
            Functions.set("mouseWheel", convertFunction(awtRobot::mouseWheel));
            Functions.set("mouseMove", (args) -> {
                Arguments.check(2, args.length);
                try {
                    awtRobot.mouseMove(args[0].asInt(), args[1].asInt());
                } catch (IllegalArgumentException iae) { }
                return NumberValue.ZERO;
            });
            Functions.set("typeText", (args) -> {
                Arguments.check(1, args.length);
                try {
                    typeText(args[0].asString());
                } catch (IllegalArgumentException iae) { }
                return NumberValue.ZERO;
            });
            Functions.set("toClipboard", new robot_toclipboard());
            Functions.set("fromClipboard", new robot_fromclipboard());
        }
        Functions.set("execProcess", new robot_exec(robot_exec.Mode.EXEC));
        Functions.set("execProcessAndWait", new robot_exec(robot_exec.Mode.EXEC_AND_WAIT));
    }
    
    private static boolean initialize() throws AWTException {
        awtRobot = new Robot(); //throw new RuntimeException("Unable to create robot instance", awte);
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
