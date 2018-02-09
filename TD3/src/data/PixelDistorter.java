package data;

import util.Convolutions;
import util.PixelBuffer;

public abstract class PixelDistorter implements MessageProcessor {
    // Prevent client code from constructing distorters
    private PixelDistorter() {}

    protected abstract int process(PixelBuffer buf, int x, int y);

    @Override
    public final Message process(Message msg) {
        if (msg instanceof PixelReferenceMessage) {
            PixelReferenceMessage pixMsg = (PixelReferenceMessage) msg;
            int newPix = this.process(pixMsg.buf, pixMsg.x, pixMsg.y);
            return new PixelMessage(pixMsg.channel, newPix, pixMsg.x, pixMsg.y);
        }
        return null;
    }

    /**
     * Apply a 3x3 Gaussian blur convolution
     */
    public static final PixelDistorter gaussianBlur = new PixelDistorter() {
        @Override
        public int process(PixelBuffer buf, int x, int y) {
            return Convolutions.blur(buf, x, y);
        }
    };

    /**
     * Apply the 1-pass 3x3 "edge" (intensity gradient estimation) convolution
     */
    public static final PixelDistorter edgeDetect = new PixelDistorter() {
        @Override
        public int process(PixelBuffer buf, int x, int y) {
            return Convolutions.edgeDetect(buf, x, y);
        }
    };

    /**
     * Apply the 1-pass 3x3 "sharpen" convolution
     */
    public static final PixelDistorter sharpen = new PixelDistorter() {
        @Override
        public int process(PixelBuffer buf, int x, int y) {
            return Convolutions.sharpen(buf, x, y);
        }
    };

    /**
     * Apply the 1-pass "emboss" convolution.
     */
    public static PixelDistorter emboss = new PixelDistorter() {
        @Override
        public int process(PixelBuffer buf, int x, int y) {
            return Convolutions.emboss(buf, x, y);
        }
    };

    /**
     * Returns the source pixel unmodified.
     */
    public static PixelDistorter identity = new PixelDistorter() {
        @Override
        public int process(PixelBuffer buf, int x, int y) {
            return buf.getPixel(x, y);
        }
    };

    /**
     * Compute the luminosity of the image
     */
    public static PixelDistorter luminosity = new PixelDistorter() {
        @Override
        public int process(PixelBuffer buf, int x, int y) {
            int pixel = buf.getPixel(x, y);
            double r = Convolutions.getRed(pixel) / 256f;
            double g = Convolutions.getGreen(pixel) / 256f;
            double b = Convolutions.getBlue(pixel) / 256f;
            double lum = Math.min(1f, Math.max(0f, r * 0.21 + g * 0.72 + b * 0.7));
            int lumpix = (int) Math.round(lum * 0xFF);
            return 0xFF000000 | (lumpix * 0x010101);
        }

    };
}
