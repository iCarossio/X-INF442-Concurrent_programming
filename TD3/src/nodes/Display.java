package nodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import util.ImageBuffer;
import data.LineMessage;
import data.Message;
import data.PixelMessage;
import data.PixelReferenceMessage;
import data.TileMessage;

public final class Display extends Node {

    private final JFrame frame;

    private final int[] pixels;
    private final ImageBuffer buffer;
    private final int width, height;
    private final Image image;

    private static int count = 0;

    public Display(String title, int w, int h) {
        super(title);
        frame = new JFrame("Java::" + title);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setLocation(count * 100, count * 50);
        ++count;

        width = w;
        height = h;
        pixels = new int[width * height];
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = Color.magenta.getRGB();
        buffer = new ImageBuffer(width, height, pixels, title);
        image = buffer.createImage();

        frame.add(new DrawPanel(image));
        JButton exit = new JButton("Exit");
        frame.add("South", exit);
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    // Node implementation

    @Override
    public void putInQueue(Message msg) {
        if (msg instanceof LineMessage)
            this.handleLine((LineMessage) msg);
        else if (msg instanceof PixelMessage)
            this.handlePixel((PixelMessage) msg);
        else if (msg instanceof PixelReferenceMessage)
            this.handlePixel((PixelReferenceMessage) msg);
        else if (msg instanceof TileMessage)
            this.handleTile((TileMessage) msg);
        else {
            String errMsg = String.format("Cannot handle %s messages",
                                          msg.getClass().toString());
            throw new UnsupportedOperationException(errMsg);
        }
    }

    private void handleLine(LineMessage lineMsg) {
        buffer.setLine(lineMsg.y, lineMsg.pixels);
    }

    private void handlePixel(PixelMessage msg) {
        buffer.setPixel(msg.x, msg.y, msg.pixel);
    }

    private void handlePixel(PixelReferenceMessage msg) {
        buffer.setPixel(msg.x, msg.y, msg.getPixel());
    }

    private void handleTile(TileMessage msg) {
        buffer.setTile(msg.x, msg.y, msg.w, msg.h, msg.buf.pixels);
    }

    @Override
    public void addConnectionTo(Node v) {
        throw new UnsupportedOperationException("input only");
    }

    @Override
    public void run() {
        // The graphical UI thread runs this as needed
        return;
    }

    @Override
    public void init() {
        // This runs in the context of the graphical UI thread
        return;
    }

    @SuppressWarnings("serial")
    private static class DrawPanel extends JComponent {
        private final Image image;

        DrawPanel(Image image) {
            this.image = image;
            setOpaque(true);
            setBackground(Color.white);
            setPreferredSize(new Dimension(this.image.getWidth(this),
                                           this.image.getHeight(this)));
        }

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(this.image, 0, 0, this);
        }
    }
}
