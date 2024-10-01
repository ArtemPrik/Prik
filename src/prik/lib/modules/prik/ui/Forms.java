package prik.lib.modules.prik.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import prik.lib.Arguments;
import prik.lib.ArrayValue;
import static prik.lib.Converters.booleanOptToVoid;
import static prik.lib.Converters.intToVoid;
import static prik.lib.Converters.stringToVoid;
import static prik.lib.Converters.voidToBoolean;
import static prik.lib.Converters.voidToFloat;
import static prik.lib.Converters.voidToInt;
import static prik.lib.Converters.voidToString;
import static prik.lib.Converters.voidToVoid;
import static prik.lib.ValueUtils.consumeFunction;
import prik.lib.Function;
import prik.lib.FunctionValue;
import prik.lib.Functions;
import prik.lib.MapValue;
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
public final class Forms implements Module {
    public static void initConstants() {
        // JFrame constants
        final MapValue frame = new MapValue(4);
        frame.set("DISPOSE_ON_CLOSE", NumberValue.of(JFrame.DISPOSE_ON_CLOSE));
        frame.set("DO_NOTHING_ON_CLOSE", NumberValue.of(JFrame.DO_NOTHING_ON_CLOSE));
        frame.set("EXIT_ON_CLOSE", NumberValue.of(JFrame.EXIT_ON_CLOSE));
        frame.set("HIDE_ON_CLOSE", NumberValue.of(JFrame.HIDE_ON_CLOSE));
        Variables.define("Frame", frame);

        // SwinfConstants
        final MapValue swing = new MapValue(20);
        swing.set("BOTTOM", NumberValue.of(SwingConstants.BOTTOM));
        swing.set("CENTER", NumberValue.of(SwingConstants.CENTER));
        swing.set("EAST", NumberValue.of(SwingConstants.EAST));
        swing.set("HORIZONTAL", NumberValue.of(SwingConstants.HORIZONTAL));
        swing.set("LEADING", NumberValue.of(SwingConstants.LEADING));
        swing.set("LEFT", NumberValue.of(SwingConstants.LEFT));
        swing.set("NEXT", NumberValue.of(SwingConstants.NEXT));
        swing.set("NORTH", NumberValue.of(SwingConstants.NORTH));
        swing.set("NORTH_EAST", NumberValue.of(SwingConstants.NORTH_EAST));
        swing.set("NORTH_WEST", NumberValue.of(SwingConstants.NORTH_WEST));
        swing.set("PREVIOUS", NumberValue.of(SwingConstants.PREVIOUS));
        swing.set("RIGHT", NumberValue.of(SwingConstants.RIGHT));
        swing.set("SOUTH", NumberValue.of(SwingConstants.SOUTH));
        swing.set("SOUTH_EAST", NumberValue.of(SwingConstants.SOUTH_EAST));
        swing.set("SOUTH_WEST", NumberValue.of(SwingConstants.SOUTH_WEST));
        swing.set("TOP", NumberValue.of(SwingConstants.TOP));
        swing.set("TRAILING", NumberValue.of(SwingConstants.TRAILING));
        swing.set("VERTICAL", NumberValue.of(SwingConstants.VERTICAL));
        swing.set("WEST", NumberValue.of(SwingConstants.WEST));
        Variables.define("SwingConstants", swing);

        // LayoutManagers constants
        final MapValue border = new MapValue(13);
        border.set("AFTER_LAST_LINE", new StringValue(BorderLayout.AFTER_LAST_LINE));
        border.set("AFTER_LINE_ENDS", new StringValue(BorderLayout.AFTER_LINE_ENDS));
        border.set("BEFORE_FIRST_LINE", new StringValue(BorderLayout.BEFORE_FIRST_LINE));
        border.set("BEFORE_LINE_BEGINS", new StringValue(BorderLayout.BEFORE_LINE_BEGINS));
        border.set("CENTER", new StringValue(BorderLayout.CENTER));
        border.set("EAST", new StringValue(BorderLayout.EAST));
        border.set("LINE_END", new StringValue(BorderLayout.LINE_END));
        border.set("LINE_START", new StringValue(BorderLayout.LINE_START));
        border.set("NORTH", new StringValue(BorderLayout.NORTH));
        border.set("PAGE_END", new StringValue(BorderLayout.PAGE_END));
        border.set("PAGE_START", new StringValue(BorderLayout.PAGE_START));
        border.set("SOUTH", new StringValue(BorderLayout.SOUTH));
        border.set("WEST", new StringValue(BorderLayout.WEST));
        Variables.define("BorderLayout", border);

        final MapValue box = new MapValue(4);
        box.set("LINE_AXIS", NumberValue.of(BoxLayout.LINE_AXIS));
        box.set("PAGE_AXIS", NumberValue.of(BoxLayout.PAGE_AXIS));
        box.set("X_AXIS", NumberValue.of(BoxLayout.X_AXIS));
        box.set("Y_AXIS", NumberValue.of(BoxLayout.Y_AXIS));
        Variables.define("BoxLayout", box);
    }

    @Override
    public void init() {
        initConstants();
        // Components
        Functions.set("newButton", (Value... args) -> {
            Arguments.checkOrOr(0, 1, args.length);
            String text = (args.length == 1) ? args[0].asString() : "";
            return new JButtonValue(new JButton(text));
        });
        Functions.set("newLabel", (Value... args) -> {
            Arguments.checkRange(0, 2, args.length);
            String text = (args.length >= 1) ? args[0].asString() : "";
            int align = (args.length == 2) ? args[1].asInt() : SwingConstants.LEADING;
            return new JLabelValue(new JLabel(text, align));
        });
        Functions.set("newPanel", (Value... args) -> {
            Arguments.checkOrOr(0, 1, args.length);
            final JPanel panel = new JPanel();
            if (args.length == 1) {
                panel.setLayout( ((LayoutManagerValue) args[0]).layout );
            }
            return new JPanelValue(panel);
        });
        Functions.set("newTextField", (Value... args) -> {
            Arguments.checkOrOr(0, 1, args.length);
            String text = (args.length == 1) ? args[0].asString() : "";
            return new JTextFieldValue(new JTextField(text));
        });
        Functions.set("newWindow", (Value... args) -> {
            Arguments.checkOrOr(0, 1, args.length);
            String title = (args.length == 1) ? args[0].asString() : "";
            final JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            return new JFrameValue(frame);
        });

        // LayoutManagers
        Functions.set("borderLayout", (Value... args) -> {
            Arguments.checkOrOr(0, 2, args.length);
            int hgap = (args.length == 2) ? args[0].asInt() : 0;
            int vgap = (args.length == 2) ? args[1].asInt() : 0;
            return new LayoutManagerValue(
                    new BorderLayout(hgap, vgap)
            );
        });
        Functions.set("boxLayout", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            int axis = (args.length == 2) ? args[1].asInt() : BoxLayout.PAGE_AXIS;
            return new LayoutManagerValue(
                    new BoxLayout(((JPanelValue) args[0]).panel, axis)
            );
        });
        Functions.set("cardLayout", (Value... args) -> {
            Arguments.checkOrOr(0, 2, args.length);
            int hgap = (args.length == 2) ? args[0].asInt() : 0;
            int vgap = (args.length == 2) ? args[1].asInt() : 0;
            return new LayoutManagerValue(
                    new CardLayout(hgap, vgap)
            );
        });
        Functions.set("gridLayout", (Value... args) -> {
            Arguments.checkRange(0, 4, args.length);
            int rows = 1, cols = 0, hgap = 0, vgap = 0;
            switch (args.length) {
                case 1:
                    rows = args[0].asInt();
                    break;
                case 2:
                    rows = args[0].asInt();
                    cols = args[1].asInt();
                    break;
                case 3:
                    rows = args[0].asInt();
                    cols = args[1].asInt();
                    hgap = args[2].asInt();
                    break;
                case 4:
                    rows = args[0].asInt();
                    cols = args[1].asInt();
                    hgap = args[2].asInt();
                    vgap = args[3].asInt();
                    break;
            }   return new LayoutManagerValue(
                    new GridLayout(rows, cols, hgap, vgap)
            );
        });
        Functions.set("flowLayout", (Value... args) -> {
            Arguments.checkRange(0, 3, args.length);
            final int align, hgap, vgap;
            switch (args.length) {
                case 1:
                    align = args[0].asInt();
                    hgap = 5;
                    vgap = 5;
                    break;
                case 2:
                    align = FlowLayout.CENTER;
                    hgap = args[0].asInt();
                    vgap = args[1].asInt();
                    break;
                case 3:
                    align = args[0].asInt();
                    hgap = args[1].asInt();
                    vgap = args[2].asInt();
                    break;
                default:
                    align = FlowLayout.CENTER;
                    hgap = 5;
                    vgap = 5;
                    break;
            }   return new LayoutManagerValue(
                    new FlowLayout(align, hgap, vgap)
            );
        });
    }
    
    public class JButtonValue extends JComponentValue {

        final JButton button;

        public JButtonValue(JButton button) {
            super(2, button);
            this.button = button;
            init();
        }

        private void init() {
            set("onClick", new FunctionValue(this::addActionListener));
            set("addActionListener", new FunctionValue(this::addActionListener));
        }

        private Value addActionListener(Value... args) {
            Arguments.check(1, args.length);
            final Function action = ValueUtils.consumeFunction(args[0], 0);
            button.addActionListener(e -> action.execute());
            return NumberValue.ZERO;
        }
    }
    
    public abstract class JComponentValue extends ContainerValue {

        final JComponent jComponent;

        public JComponentValue(int functionsCount, JComponent jComponent) {
            super(functionsCount + 2, jComponent);
            this.jComponent = jComponent;
            init();
        }

        private void init() {
            set("getToolTipText", voidToString(jComponent::getToolTipText));
            set("setToolTipText", stringToVoid(jComponent::setToolTipText));
        }
    }
    
    public abstract class ContainerValue extends ComponentValue {

        final Container container;

        public ContainerValue(int functionsCount, Container container) {
            super(functionsCount + 10, container);
            this.container = container;
            init();
        }

        private void init() {
            set("add", new FunctionValue(this::add));
            set("remove", new FunctionValue(this::remove));
            set("removeAll", voidToVoid(container::removeAll));
            set("getAlignmentX", voidToFloat(container::getAlignmentX));
            set("getAlignmentY", voidToFloat(container::getAlignmentY));
            set("getComponentCount", voidToInt(container::getComponentCount));
            set("isFocusCycleRoot", voidToBoolean(container::isFocusCycleRoot));
            set("isValidateRoot", voidToBoolean(container::isValidateRoot));
            set("setLayout", new FunctionValue(this::setLayout));
        }

        private Value add(Value... args) {
            Arguments.checkRange(1, 3, args.length);

            final Component newComponent;
            if (args[0] instanceof ComponentValue) {
                newComponent = ((ComponentValue) args[0]).component;
            } else {
                newComponent = new JLabel(args[0].asString());
            }
            switch (args.length) {
                case 1:
                    container.add(newComponent);
                    break;
                case 2:
                    if (args[1].type() == Types.NUMBER) {
                        // add(component, index)
                        container.add(newComponent, args[1].asInt());
                    } else {
                        // add(component, constraints)
                        container.add(newComponent, args[1].raw());
                    }
                    break;
                case 3:
                    // add(component, constraints, index)
                    container.add(newComponent, args[1].raw(), args[2].asInt());
                    break;
            }
            return NumberValue.ZERO;
        }

        private Value remove(Value... args) {
            Arguments.check(1, args.length);
            if (args[0] instanceof JComponentValue) {
                container.remove(((JComponentValue) args[0]).component);
            } else {
                container.remove(args[0].asInt());
            }
            return NumberValue.ZERO;
        }

        private Value setLayout(Value... args) {
            Arguments.check(1, args.length);
            container.setLayout(((LayoutManagerValue) args[0]).layout);
            return NumberValue.ZERO;
        }
    }
    
    public abstract class ComponentValue extends MapValue {

        final Component component;

        public ComponentValue(int functionsCount, Component component) {
            super(functionsCount + 42);
            this.component = component;
            init();
        }

        private void init() {
            set("onKeyAction", new FunctionValue(this::addKeyListener));
            set("addKeyListener", new FunctionValue(this::addKeyListener));
            set("getFocusTraversalKeysEnabled", voidToBoolean(component::getFocusTraversalKeysEnabled));
            set("getHeight", voidToInt(component::getHeight));
            set("getIgnoreRepaint", voidToBoolean(component::getIgnoreRepaint));
            set("getLocation", new FunctionValue(this::getLocation));
            set("getLocationOnScreen", new FunctionValue(this::getLocationOnScreen));
            set("getMinimumSize", dimensionFunction(component::getMinimumSize));
            set("getMaximumSize", dimensionFunction(component::getMaximumSize));
            set("getName", voidToString(component::getName));
            set("getPreferredSize", dimensionFunction(component::getPreferredSize));
            set("getSize", dimensionFunction(component::getSize));
            set("getWidth", voidToInt(component::getWidth));
            set("getX", voidToInt(component::getX));
            set("getY", voidToInt(component::getY));
            set("hasFocus", voidToBoolean(component::hasFocus));
            set("invalidate", voidToVoid(component::invalidate));

            set("isDisplayable", voidToBoolean(component::isDisplayable));
            set("isDoubleBuffered", voidToBoolean(component::isDoubleBuffered));
            set("isEnabled", voidToBoolean(component::isEnabled));
            set("isFocusOwner", voidToBoolean(component::isFocusOwner));
            set("isFocusable", voidToBoolean(component::isFocusable));
            set("isLightweight", voidToBoolean(component::isLightweight));
            set("isOpaque", voidToBoolean(component::isOpaque));
            set("isShowing", voidToBoolean(component::isShowing));
            set("isValid", voidToBoolean(component::isValid));
            set("isVisible", voidToBoolean(component::isVisible));

            set("requestFocus", voidToVoid(component::requestFocus));
            set("requestFocusInWindow", voidToBoolean(component::requestFocusInWindow));
            set("repaint", voidToVoid(component::repaint));
            set("revalidate", voidToVoid(component::revalidate));
            set("setMaximumSize", voidDimensionFunction(component::setMaximumSize));
            set("setMinimumSize", voidDimensionFunction(component::setMinimumSize));
            set("setName", stringToVoid(component::setName));
            set("setPreferredSize", voidDimensionFunction(component::setPreferredSize));
            set("setSize", voidDimensionFunction(component::setSize));
            set("setVisible", booleanOptToVoid(component::setVisible));
            set("setLocation", new FunctionValue(this::setLocation));
            set("validate", voidToVoid(component::validate));
        }

        private Value addKeyListener(Value... args) {
            Arguments.check(1, args.length);
            final Function action = ValueUtils.consumeFunction(args[0], 0);
            component.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                    handleKeyEvent("typed", e);
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    handleKeyEvent("pressed", e);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    handleKeyEvent("released", e);
                }

                private void handleKeyEvent(String type, final KeyEvent e) {
                    final MapValue map = new MapValue(15);
                    map.set("extendedKeyCode", NumberValue.of(e.getExtendedKeyCode()));
                    map.set("keyChar", NumberValue.of(e.getKeyChar()));
                    map.set("keyCode", NumberValue.of(e.getKeyCode()));
                    map.set("keyLocation", NumberValue.of(e.getKeyLocation()));
                    map.set("id", NumberValue.of(e.getID()));
                    map.set("isActionKey", NumberValue.fromBoolean(e.isActionKey()));
                    map.set("isAltDown", NumberValue.fromBoolean(e.isAltDown()));
                    map.set("isAltGraphDown", NumberValue.fromBoolean(e.isAltGraphDown()));
                    map.set("isConsumed", NumberValue.fromBoolean(e.isConsumed()));
                    map.set("isControlDown", NumberValue.fromBoolean(e.isControlDown()));
                    map.set("isMetaDown", NumberValue.fromBoolean(e.isMetaDown()));
                    map.set("isShiftDown", NumberValue.fromBoolean(e.isShiftDown()));
                    map.set("modifiers", NumberValue.of(e.getModifiers()));
                    action.execute(new StringValue(type), map);
                }
            });
            return NumberValue.ZERO;
        }

        private Value getLocation(Value... args) {
            final Point location = component.getLocation();
            final ArrayValue result = new ArrayValue(2);
            result.set(0, NumberValue.of(location.x));
            result.set(1, NumberValue.of(location.y));
            return result;
        }

        private Value getLocationOnScreen(Value... args) {
            final Point location = component.getLocationOnScreen();
            final ArrayValue result = new ArrayValue(2);
            result.set(0, NumberValue.of(location.x));
            result.set(1, NumberValue.of(location.y));
            return result;
        }

        private Value setLocation(Value... args) {
            Arguments.check(2, args.length);
            component.setLocation(args[0].asInt(), args[1].asInt());
            return NumberValue.ZERO;
        }



        protected static FunctionValue dimensionFunction(Supplier<Dimension> s) {
            return new FunctionValue(args -> {
                final Dimension dimension = s.get();
                final ArrayValue result = new ArrayValue(2);
                result.set(0, NumberValue.of(dimension.getWidth()));
                result.set(1, NumberValue.of(dimension.getHeight()));
                return result;
            });
        }

        protected static FunctionValue voidDimensionFunction(Consumer<Dimension> s) {
            return new FunctionValue(args -> {
                Arguments.check(2, args.length);
                s.accept(new Dimension(args[0].asInt(), args[1].asInt()));
                return NumberValue.ZERO;
            });
        }
    }
    
    public class JLabelValue extends JComponentValue {

        final JLabel label;

        public JLabelValue(JLabel label) {
            super(17, label);
            this.label = label;
            init();
        }

        private void init() {
            set("getDisplayedMnemonic", voidToInt(label::getDisplayedMnemonic));
            set("getDisplayedMnemonicIndex", voidToInt(label::getDisplayedMnemonicIndex));
            set("getHorizontalAlignment", voidToInt(label::getHorizontalAlignment));
            set("getHorizontalTextPosition", voidToInt(label::getHorizontalTextPosition));
            set("getIconTextGap", voidToInt(label::getIconTextGap));
            set("getVerticalAlignment", voidToInt(label::getVerticalAlignment));
            set("getVerticalTextPosition", voidToInt(label::getVerticalTextPosition));

            set("getText", voidToString(label::getText));
            set("setDisplayedMnemonic", intToVoid(label::setDisplayedMnemonic));
            set("setDisplayedMnemonicIndex", intToVoid(label::setDisplayedMnemonicIndex));
            set("setHorizontalAlignment", intToVoid(label::setHorizontalAlignment));
            set("setHorizontalTextPosition", intToVoid(label::setHorizontalTextPosition));
            set("setIconTextGap", intToVoid(label::setIconTextGap));
            set("setVerticalAlignment", intToVoid(label::setVerticalAlignment));
            set("setVerticalTextPosition", intToVoid(label::setVerticalTextPosition));
            set("setText", stringToVoid(label::setText));
        }
    }
    
    public class LayoutManagerValue extends MapValue {

        final LayoutManager layout;

        public LayoutManagerValue(LayoutManager layout) {
            super(0);
            this.layout = layout;
        }
    }
    
    public class JPanelValue extends JComponentValue {

        final JPanel panel;

        public JPanelValue(JPanel panel) {
            super(0, panel);
            this.panel = panel;
            init();
        }

        private void init() {
        }
    }
    
    public class JTextFieldValue extends JComponentValue {

        private final JTextField textField;

        public JTextFieldValue(JTextField textField) {
            super(16, textField);
            this.textField = textField;
            init();
        }

        private void init() {
            set("onAction", new FunctionValue(this::addActionListener));
            set("addActionListener", new FunctionValue(this::addActionListener));
            set("getCaretPosition", voidToInt(textField::getCaretPosition));
            set("getColumns", voidToInt(textField::getColumns));
            set("getHorizontalAlignment", voidToInt(textField::getHorizontalAlignment));
            set("getSelectionEnd", voidToInt(textField::getSelectionEnd));
            set("getSelectionStart", voidToInt(textField::getSelectionStart));
            set("getScrollOffset", voidToInt(textField::getScrollOffset));
            set("getText", voidToString(textField::getText));
            set("setCaretPosition", intToVoid(textField::setCaretPosition));
            set("setColumns", intToVoid(textField::setColumns));
            set("setHorizontalAlignment", intToVoid(textField::setHorizontalAlignment));
            set("setScrollOffset", intToVoid(textField::setScrollOffset));
            set("setSelectionEnd", intToVoid(textField::setSelectionEnd));
            set("setSelectionStart", intToVoid(textField::setSelectionStart));
            set("setText", stringToVoid(textField::setText));
        }

        private Value addActionListener(Value... args) {
            Arguments.check(1, args.length);
            Function action = consumeFunction(args[0], 1);
            textField.addActionListener(e -> action.execute());
            return NumberValue.ZERO;
        }
    }
    
    public class JFrameValue extends ContainerValue {

        final JFrame frame;

        public JFrameValue(JFrame frame) {
            super(9, frame);
            this.frame = frame;
            init();
        }

        private void init() {
            set("dispose", voidToVoid(frame::dispose));
            set("getTitle", voidToString(frame::getTitle));
            set("getDefaultCloseOperation", voidToInt(frame::getDefaultCloseOperation));
            set("pack", voidToVoid(frame::pack));
            set("setAlwaysOnTop", booleanOptToVoid(frame::setAlwaysOnTop));
            set("setDefaultCloseOperation", intToVoid(frame::setDefaultCloseOperation));
            set("setLocationByPlatform", booleanOptToVoid(frame::setLocationByPlatform));
            set("setResizable", booleanOptToVoid(frame::setResizable));
            set("setTitle", stringToVoid(frame::setTitle));
        }
    }
}
