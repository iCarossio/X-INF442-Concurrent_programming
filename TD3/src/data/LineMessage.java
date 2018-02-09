package data;

public class LineMessage extends Message {
    private static final long serialVersionUID = 5560420984375724183L;

    public final int y;
    public final int[] pixels;

    public LineMessage(String channel, int y, int[] pixels) {
        super(channel);
        this.y = y;
        this.pixels = pixels;
    }
}
