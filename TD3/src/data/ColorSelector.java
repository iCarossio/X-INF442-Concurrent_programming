package data;

import util.PixelBuffer;

public abstract class ColorSelector implements MessageProcessor {

    // Prevent construction from outside
    private ColorSelector() {}

    private static final ColorSelector makeWithMask(int mask) {
        final int opaqueMask = (0xFF000000 | mask) & 0xFFFFFFFF;
        return new ColorSelector() {
            @Override
            public Message process(Message msg) {
                if (msg instanceof PixelMessage) {
                    PixelMessage pix = (PixelMessage) msg;
                    int newPix = pix.pixel & opaqueMask;
                    return new PixelMessage(pix.channel, newPix, pix.x, pix.y);
                } else if (msg instanceof PixelReferenceMessage) {
                    PixelReferenceMessage pixRef = (PixelReferenceMessage) msg;
                    int newPix = pixRef.getPixel() & opaqueMask;
                    return new PixelMessage(pixRef.channel, newPix, pixRef.x,
                                            pixRef.y);
                } else if (msg instanceof TileMessage) {
                    TileMessage tile = (TileMessage) msg;
                    int[] newPixels = new int[tile.buf.pixels.length];
                    for (int i = 0; i < newPixels.length; i++)
                        newPixels[i] = tile.buf.pixels[i] & opaqueMask;
                    PixelBuffer newBuf = new PixelBuffer(newPixels, tile.buf.width,
                                               tile.buf.height);
                    return new TileMessage(tile.channel, newBuf, tile.x,
                                           tile.y, tile.w, tile.h);
                } else if (msg instanceof LineMessage) {
                    LineMessage line = (LineMessage) msg;
                    int[] newContent = new int[line.pixels.length];
                    for (int i = 0; i < newContent.length; i++)
                        newContent[i] = line.pixels[i] & opaqueMask;
                    return new LineMessage(line.channel, line.y, newContent);
                }
                return null;
            }
        };
    }

    public static final ColorSelector red = makeWithMask(0xFF0000);
    public static final ColorSelector green = makeWithMask(0xFF00);
    public static final ColorSelector blue = makeWithMask(0xFF);
}
