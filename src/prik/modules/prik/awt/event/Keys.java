package prik.modules.prik.awt.event;

import java.awt.event.KeyEvent;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.Variables;
import prik.modules.Module;

/**
 *
 * @author Professional
 */
public final class Keys implements Module {
    @Override
    public void init() {
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
        
        keys.set("EQUALS", NumberValue.of(KeyEvent.VK_EQUALS));
        Variables.set("=", NumberValue.of(KeyEvent.VK_EQUALS));

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
        
        Variables.define("Keys", keys);
    }
}
