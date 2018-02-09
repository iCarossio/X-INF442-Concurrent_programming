package data;

import util.PixelBuffer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class TileDistorter implements MessageProcessor {
    private final PixelDistorter distorter;
    private final int threshold;
    private final ForkJoinPool pool;
    
    private class Job extends RecursiveAction {
        private final PixelBuffer src;
        private final int[] dest;
        private final int rx, ry, rw, rh;

        public Job(PixelBuffer src, int[] dest, int rx, int ry, int rw, int rh) {
            this.src = src;
            this.dest = dest;
            this.rx = rx;
            this.ry = ry;
            this.rw = rw;
            this.rh = rh;
        }

        @Override
        public void compute() {
            if (this.rw * this.rh <= threshold) {
                for (int i = this.rx; i < this.rx + this.rw; i++)
                    for (int j = this.ry; j < this.ry + this.rh; j++) {
                        int newPixel = distorter.process(this.src, i, j);
                        this.dest[j * this.src.width + i] = newPixel;
                    }
                return;
            }
            List<Job> subs = new LinkedList<>();
            subs.add(new Job(this.src, this.dest,
                             this.rx, this.ry,
                             this.rw / 2, this.rh / 2));
            subs.add(new Job(this.src, this.dest,
                             this.rx + this.rw / 2, this.ry,
                             this.rw - this.rw / 2, this.rh / 2));
            subs.add(new Job(this.src, this.dest,
                             this.rx, this.ry + this.rh / 2,
                             this.rw / 2, this.rh - this.rh / 2));
            subs.add(new Job(this.src, this.dest,
                             this.rx + this.rw / 2, this.ry + this.rh / 2,
                             this.rw - this.rw / 2, this.rh - this.rh / 2));
            invokeAll(subs);
        }
    }

    public TileDistorter(int parallelism, PixelDistorter distorter, int threshold) {
    		this.pool = new ForkJoinPool(parallelism);
        this.distorter = distorter;
        this.threshold = threshold;
    }

    public TileDistorter(int parallelism, PixelDistorter distorter) {
        this(parallelism, distorter, 64);
    }

    private Message processTile(TileMessage msg) {
        int[] dest = msg.buf.pixels.clone();
        pool.invoke(new Job(msg.buf, dest, msg.x, msg.y, msg.w, msg.h));
        return new TileMessage(msg.channel,
                               new PixelBuffer(dest, msg.buf.width, msg.buf.height),
                               msg.x, msg.y, msg.w, msg.h);
    }

    @Override
    public Message process(Message msg) {
        if (msg instanceof TileMessage)
            return this.processTile((TileMessage) msg);
        else {
            throw new IllegalArgumentException("TileDistorter: invalid message");
        }
    }

}
