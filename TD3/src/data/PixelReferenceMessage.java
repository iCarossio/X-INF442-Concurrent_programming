package data;

import util.PixelBuffer;

public class PixelReferenceMessage extends Message {
    private static final long serialVersionUID = 4145560196315767220L;

    public final PixelBuffer buf;
    public final int x, y;

    public PixelReferenceMessage(String channel, PixelBuffer buf, int x, int y) {
        super(channel);
        this.buf = buf;
        this.x = x;
        this.y = y;
    }

    public int getPixel() {
        return this.buf.getPixel(this.x, this.y);
    }
}