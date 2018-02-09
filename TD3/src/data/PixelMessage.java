package data;

public class PixelMessage extends Message {
    private static final long serialVersionUID = 4145560196315767220L;

    public final int pixel;
    public final int x, y;

    public PixelMessage(String channel, int pixel, int x, int y) {
        super(channel);
        this.pixel = pixel;
        this.x = x;
        this.y = y;
    }
}
