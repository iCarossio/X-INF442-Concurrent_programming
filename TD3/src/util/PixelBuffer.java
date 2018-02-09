package util;

public class PixelBuffer {
    public final int[] pixels;
    public final int width, height;

    public PixelBuffer(int[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        assert (this.pixels.length == this.width * this.height);
    }

    public static int clamp(int val, int min, int max) {
        return Math.min(max, Math.max(min, val));
    }

    public int getPixel(int x, int y) {
        x = clamp(x, 0, this.width - 1);
        y = clamp(y, 0, this.height - 1);
        return this.pixels[y * this.width + x];
    }
}
