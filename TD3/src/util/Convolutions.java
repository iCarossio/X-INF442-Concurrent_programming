package util;

public final class Convolutions {

    public static int getRed(int pixel) {
        return (pixel >> 16) & 0xFF;
    }

    public static int getGreen(int pixel) {
        return (pixel >> 8) & 0xFF;
    }

    public static int getBlue(int pixel) {
        return pixel & 0xFF;
    }

    public static int assemble(int r, int g, int b) {
        return 0xFF000000 | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static int convolve(PixelBuffer buf, int x, int y, int[][] kernel,
                               int div, int bias) {
        int outR = 0, outG = 0, outB = 0;
        for (int i = 0; i < kernel.length; i++)
            for (int j = 0; j < kernel[i].length; j++) {
                int srcX = i + (x - kernel.length / 2);
                int srcY = j + (y - kernel[i].length / 2);
                int k = kernel[i][j];
                int pixel = buf.getPixel(srcX, srcY);
                outR += k * getRed(pixel);
                outG += k * getGreen(pixel);
                outB += k * getBlue(pixel);
            }
        outR = PixelBuffer.clamp(outR / div + bias, 0, 0xFF);
        outG = PixelBuffer.clamp(outG / div + bias, 0, 0xFF);
        outB = PixelBuffer.clamp(outB / div + bias, 0, 0xFF);
        return assemble(outR, outG, outB);
    }

    private static final int[][] blurKernel = { {1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
    private static final int blurDiv = 16;
    private static final int blurBias = 0;

    public static int blur(PixelBuffer buf, int x, int y) {
        return convolve(buf, x, y, blurKernel, blurDiv, blurBias);
    }

    private static final int[][] edgeKernel = { {-1, -1, -1}, {-1, 8, -1},
                                               {-1, -1, -1}};
    private static final int edgeDiv = 1;
    private static final int edgeBias = 0;

    public static int edgeDetect(PixelBuffer buf, int x, int y) {
        return convolve(buf, x, y, edgeKernel, edgeDiv, edgeBias);
    }

    private static final int[][] sharpenKernel = { {-1, -1, -1}, {-1, 9, -1},
                                                  {-1, -1, -1}};
    private static final int sharpenDiv = 1;
    private static final int sharpenBias = 0;

    public static int sharpen(PixelBuffer buf, int x, int y) {
        return convolve(buf, x, y, sharpenKernel, sharpenDiv, sharpenBias);
    }

    private static final int[][] embossKernel = { {-2, 0, 0}, {0, 1, 0},
                                                 {0, 0, 2}};
    private static final int embossDiv = 1;
    private static final int embossBias = 0;

    public static int emboss(PixelBuffer buf, int x, int y) {
        return convolve(buf, x, y, embossKernel, embossDiv, embossBias);
    }
}
