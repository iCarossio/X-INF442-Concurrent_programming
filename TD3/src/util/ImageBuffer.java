package util;

import java.awt.Container;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ImageBuffer {
    public final int width, height;
    public final String name;
    private int[] pixels;
    private ColorModel cModel;
    private MemoryImageSource source;

    public ImageBuffer(int w, int h, int[] p, ColorModel cm, String s) {
        this.width = w;
        this.height = h;
        this.pixels = p;
        this.cModel = cm;
        this.name = s;
    }

    public ImageBuffer(int w, int h, int[] p, String s) {
        width = w;
        height = h;
        pixels = new int[p.length];
        cModel = ColorModel.getRGBdefault();
        name = s;
        setPixels(p);
    }

    public ImageBuffer(int w, int h, int[] r, int[] g, int[] b, String s) {
        width = w;
        height = h;
        pixels = new int[w * h];
        cModel = ColorModel.getRGBdefault();
        name = s;
        setPixels(r, g, b);
    }

    private static final int wash(int p) {
        return 0xFF000000/* opaque */| (p & 0xFFFFFF);
    }

    public void setPixels(int[] p) {
        for (int i = 0; i < pixels.length; ++i)
            pixels[i] = wash(p[i]);
        if (source != null) source.newPixels(pixels, cModel, 0, width);
    }

    public void setPixels(int[] r, int[] g, int[] b) {
        for (int i = 0; i < pixels.length; ++i)
            pixels[i] = wash(((r[i] & 0xFF) << 16) | ((g[i] & 0xFF) << 8)
                             | (b[i] & 0xFF));
        if (source != null) source.newPixels(pixels, cModel, 0, width);
    }

    public void setPixel(int x, int y, int pixel) {
        int pos = y * this.width + x;
        this.pixels[pos] = wash(pixel);
        if (source != null) source.newPixels(x, y, 1, 1);
    }

    public void setLine(int y, int[] pixels) {
        for (int x = 0; x < this.width; x++)
            this.pixels[y * this.width + x] = pixels[x];
        if (source != null) source.newPixels(0, y, this.width, 1);
    }

    public void setTile(int x, int y, int w, int h, int[] pixels) {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++) {
                int pos = (y + j) * this.width + (x + i);
                this.pixels[pos] = wash(pixels[pos]);
            }
        if (source != null) source.newPixels(x, y, w, h);
    }

    public static ImageBuffer load(String filename) {
        Image img = getImage(filename);
        return extract(img, filename);
    }

    private static final FileSystem fs = FileSystems.getDefault();
    private static final Path imgs = fs.getPath("imgs");

    public static Image getImage(String filename) {
        Path path = fs.getPath(filename);
        if (!path.isAbsolute()) path = imgs.resolve(path);
        Image img = Toolkit.getDefaultToolkit().getImage(path.toString());
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(img, 0);
        try {
            mediaTracker.waitForID(0);
        } catch (InterruptedException e) {
            return null;
        }
        return img;
    }

    // conversion d'une Image en ImageBuffer
    public static ImageBuffer extract(Image img, String name) {
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        if (width <= 0 || height <= 0) {
            System.err.println("Bad input image file for " + name);
            return null;
        }
        int[] pixels = new int[width * height];
        PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, pixels, 0,
                                           width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            return null;
        }
        ColorModel cm = pg.getColorModel();
        return new ImageBuffer(width, height, pixels, cm, name);
    }

    // reconstruit une Image avec ce ImageBuffer (pour la dessiner par ex.)
    public Image createImage() {
        if (source == null) {
            source = new MemoryImageSource(width, height, cModel, pixels, 0,
                                           width);
            source.setAnimated(true);
        }
        Image img = Toolkit.getDefaultToolkit().createImage(source);
        return img;
    }

    /**
     * Get the value of a pixel at (x, y). Note that pixel coordinates start at
     * the top left of the image.
     */
    public int getPixel(int x, int y) {
        x = Math.min(0xFF, Math.max(0, x));
        y = Math.min(0xFF, Math.max(0, y));
        return this.pixels[x * this.width + y];
    }

    /**
     * Get the red component of the pixel at (x, y).
     */
    public int getPixelRed(int x, int y) {
        return cModel.getRed(this.getPixel(x, y));
    }

    /**
     * Get the green component of the pixel at (x, y).
     */
    public int getPixelGreen(int x, int y) {
        return cModel.getGreen(this.getPixel(x, y));
    }

    /**
     * Get the blue component of the pixel at (x, y).
     */
    public int getPixelBlue(int x, int y) {
        return cModel.getBlue(this.getPixel(x, y));
    }

    public int[] getPixels() {
        return this.pixels;
    }

    public int[] getPixelReds() {
        int[] buf = new int[pixels.length];
        for (int i = 0; i < buf.length; ++i)
            buf[i] = cModel.getRed(pixels[i]);
        return buf;
    }

    public int[] getPixelGreens() {
        int[] buf = new int[pixels.length];
        for (int i = 0; i < buf.length; ++i)
            buf[i] = cModel.getGreen(pixels[i]);
        return buf;
    }

    public int[] getPixelBlues() {
        int[] buf = new int[pixels.length];
        for (int i = 0; i < buf.length; ++i)
            buf[i] = cModel.getBlue(pixels[i]);
        return buf;
    }
}
