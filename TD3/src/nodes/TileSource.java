package nodes;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.util.Hashtable;

import util.PixelBuffer;
import data.Message;
import data.TileMessage;

public class TileSource extends Source {
    public TileSource(String fileName, String chan, boolean loop) {
        super(fileName, chan, false);
    }

    @Override
    public void run() {
        buffer = new PixelBuffer(new int[buffer.width * buffer.height],
                            buffer.width, buffer.height);
        final TileSource parent = this;
        this.image.getSource().startProduction(new ImageConsumer() {
            @Override
            public void setDimensions(int width, int height) {}

            @Override
            public void setProperties(Hashtable<?, ?> props) {}

            @Override
            public void setColorModel(ColorModel model) {}

            @Override
            public void setHints(int hintflags) {}

            @Override
            public void setPixels(int x, int y, int w, int h, ColorModel model,
                                  byte[] pixels, int off, int scansize) {
                for (int i = 0; i < pixels.length; i++)
                    parent.buffer.pixels[y * w + x + i] = model.getRGB(pixels[i]);
            }

            @Override
            public void setPixels(int x, int y, int w, int h, ColorModel model,
                                  int[] pixels, int off, int scansize) {
                for (int i = 0; i < pixels.length; i++)
                    parent.buffer.pixels[y * w + x + i] = model.getRGB(pixels[i]);
            }

            @Override
            public void imageComplete(int status) {
                if (status == STATICIMAGEDONE || status == SINGLEFRAMEDONE) {
                    Message msg = new TileMessage(parent.channel,
                                                  parent.buffer, 0, 0,
                                                  parent.buffer.width,
                                                  parent.buffer.height);
                    parent.forward(msg);
                }
            }
        });
    }

    @Override
    public void sendData() {}
}
