package data;

import util.PixelBuffer;

public class TileMessage extends Message {
    private static final long serialVersionUID = 7647569047713721140L;

    public final PixelBuffer buf;
    // the rectangular area owned by the TileMessage
    // is between (x, y) and (x + w - 1, y + h - 1),
    // edges inclusive.
    public final int x, y;
    public final int w, h;

    public TileMessage(String channel, PixelBuffer buf, int x, int y, int w, int h) {
        super(channel);
        this.buf = buf;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
