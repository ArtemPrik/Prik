package prik.modules.prik.ui;

import prik.modules.Module;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import prik.lib.ArrayValue;
import prik.lib.Function;
import prik.lib.Functions;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Value;
import prik.lib.Variables;

/**
 *
 * @author Professional
 */
public final class Canvas implements Module {
    private static final NumberValue MINUS_ONE = new NumberValue(-1);
    
    private static JFrame frame;
    private static CanvasPanel panel;
    private static Graphics2D graphics;
    private static BufferedImage img;
    
    private static NumberValue lastKey;
    private static ArrayValue mouseHover;

    @Override
    public void init() {
        Functions.set("window", new CreateWindow());
        Functions.set("prompt", new Prompt());
        Functions.set("keypressed", new KeyPressed());
        Functions.set("mousehover", new MouseHover());
        Functions.set("line", intConsumer4Convert(Canvas::line));
        Functions.set("oval", intConsumer4Convert(Canvas::oval));
        Functions.set("foval", intConsumer4Convert(Canvas::foval));
        Functions.set("rect", intConsumer4Convert(Canvas::rect));
        Functions.set("frect", intConsumer4Convert(Canvas::frect));
        Functions.set("clip", intConsumer4Convert(Canvas::clip));
        Functions.set("drawstring", new DrawString());
        Functions.set("color", new SetColor());
        Functions.set("repaint", new Repaint());
        
        lastKey = MINUS_ONE;
        mouseHover = new ArrayValue(new Value[] { NumberValue.ZERO, NumberValue.ZERO });
    }
    
    @FunctionalInterface
    private interface IntConsumer4 {
        void accept(int i1, int i2, int i3, int i4);
    }
    
    private static void line(int x1, int y1, int x2, int y2) {
        graphics.drawLine(x1, y1, x2, y2);
    }
    
    private static void oval(int x, int y, int w, int h) {
        graphics.drawOval(x, y, w, h);
    }
    
    private static void foval(int x, int y, int w, int h) {
        graphics.fillOval(x, y, w, h);
    }
    
    private static void rect(int x, int y, int w, int h) {
        graphics.drawRect(x, y, w, h);
    }
    
    private static void frect(int x, int y, int w, int h) {
        graphics.fillRect(x, y, w, h);
    }
    
    private static void clip(int x, int y, int w, int h) {
        graphics.setClip(x, y, w, h);
    }
    
    private static Function intConsumer4Convert(IntConsumer4 consumer) {
        return args -> {
            if (args.length != 4) throw new RuntimeException("Four args expected");
            int x = (int) args[0].asNumber();
            int y = (int) args[1].asNumber();
            int w = (int) args[2].asNumber();
            int h = (int) args[3].asNumber();
            consumer.accept(x, y, w, h);
            return NumberValue.ZERO;
        };
    }

    private static class CanvasPanel extends JPanel {

        public CanvasPanel(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            graphics = img.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            setFocusable(true);
            requestFocus();
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    lastKey = new NumberValue(e.getKeyCode());
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    lastKey = MINUS_ONE;
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    mouseHover.set(0, new NumberValue(e.getX()));
                    mouseHover.set(1, new NumberValue(e.getY()));
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, null);
        }
    }
    
    private static class CreateWindow implements Function {

        @Override
        public Value execute(Value... args) {
            String title = "";
            int width = 640;
            int height = 480;
            switch (args.length) {
                case 1:
                    title = args[0].asString();
                    break;
                case 2:
                    width = (int) args[0].asNumber();
                    height = (int) args[1].asNumber();
                    break;
                case 3:
                    title = args[0].asString();
                    width = (int) args[1].asNumber();
                    height = (int) args[2].asNumber();
                    break;
            }
            panel = new CanvasPanel(width, height);
            
            frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack();
            frame.setVisible(true);
            return NumberValue.ZERO;
        }
    }
    
    private static class KeyPressed implements Function {
        
        @Override
        public Value execute(Value... args) {
            return lastKey;
        }
    }
    
    private static class MouseHover implements Function {
        
        @Override
        public Value execute(Value... args) {
            return mouseHover;
        }
    }
    
    private static class DrawString implements Function {
        
        @Override
        public Value execute(Value... args) {
            if (args.length != 3) throw new RuntimeException("Three args expected");
            int x = (int) args[1].asNumber();
            int y = (int) args[2].asNumber();
            graphics.drawString(args[0].asString(), x, y);
            return NumberValue.ZERO;
        }
    }
    
    private static class Prompt implements Function {
        
        @Override
        public Value execute(Value... args) {
            final String v = JOptionPane.showInputDialog(args[0].asString());
            return new StringValue(v == null ? "0" : v);
        }
    }
    
    private static class Repaint implements Function {

        @Override
        public Value execute(Value... args) {
            panel.invalidate();
            panel.repaint();
            return NumberValue.ZERO;
        }
    }

    private static class SetColor implements Function {

        @Override
        public Value execute(Value... args) {
            if (args.length == 1) {
                graphics.setColor(new Color(args[0].asInt()));
                return NumberValue.ZERO;
            }
            int r = args[0].asInt();
            int g = args[1].asInt();
            int b = args[2].asInt();
            graphics.setColor(new Color(r, g, b));
            return NumberValue.ZERO;
        }

    }
}
