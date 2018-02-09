package nodes;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.util.Hashtable;

import util.PixelBuffer;

public class AnimatedLineSource extends LineSource implements ImageConsumer {

    public AnimatedLineSource(String fileName, String channel) {
        super(fileName, channel, false);
    }

    @Override
    public void run() {
        buffer = new PixelBuffer(new int[buffer.width * buffer.height],
                            buffer.width, buffer.height);
        image.getSource().startProduction(this);
    }

    // ImageConsumer interface

    public void setDimensions(int w, int h) { // ignored
    }

    public void setProperties(Hashtable<?, ?> properties) { // ignored
    }

    public void setColorModel(ColorModel cModel) { // ignored
    }

    public void setHints(int hintFlags) { // ignored
    }

    public void imageComplete(int status) {
        if (status == STATICIMAGEDONE || status == SINGLEFRAMEDONE)
            this.sendData();
    }

    public void setPixels(int x, int y, int w, int h, ColorModel model,
                          byte[] pixels, int off, int scanSize) {
        for (int i = 0; i < pixels.length; i++) {
            buffer.pixels[x + y * w + i] = model.getRGB(pixels[i]);
        }
    }

    public void setPixels(int x, int y, int w, int h, ColorModel model,
                          int[] pixels, int off, int scanSize) {
        for (int i = 0; i < pixels.length; i++)
            buffer.pixels[x + y * w + i] = model.getRGB(pixels[i]);
    }

}
