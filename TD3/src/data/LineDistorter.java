package data;

import java.util.Random;

public abstract class LineDistorter implements MessageProcessor {
    private LineDistorter() {}

    private static final Random __random = new Random();

    protected abstract void mutateLine(int[] line);

    @Override
    public final Message process(Message msg) {
        if (msg instanceof LineMessage) {
            LineMessage lineMsg = (LineMessage) msg;
            int[] pix = lineMsg.pixels;
            assert (pix.length > 0);
            this.mutateLine(pix);
            return new LineMessage(lineMsg.channel, lineMsg.y, pix);
        }
        return null;
    }

    public static final LineDistorter rotate1 = new LineDistorter() {
        @Override
        protected void mutateLine(int[] line) {
            int last = line[0];
            for (int i = 1; i < line.length; i++) {
                int cur = line[i];
                line[i] = last;
                last = cur;
            }
            line[0] = last;
        }
    };

    public static final LineDistorter rotateN(int n) {
        final int nRounds = n;
        return new LineDistorter() {
            @Override
            protected void mutateLine(int[] line) {
                for (int round = 0; round < nRounds; round++) {
                    int last = line[0];
                    for (int i = 1; i < line.length; i++) {
                        int cur = line[i];
                        line[i] = last;
                        last = cur;
                    }
                    line[0] = last;
                }
            }
        };
    }

    public static LineDistorter randomize = new LineDistorter() {
        @Override
        protected void mutateLine(int[] line) {
            // Fisher-Yates shuffle
            for (int i = 0; i < line.length; i++) {
                int cand = __random.nextInt(i + 1);
                if (i != cand) {
                    int tmp = line[i];
                    line[i] = line[cand];
                    line[cand] = tmp;
                }
            }
        }
    };
}
