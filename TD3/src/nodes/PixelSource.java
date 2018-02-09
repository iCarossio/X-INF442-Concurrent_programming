package nodes;

import data.Message;
import data.PixelReferenceMessage;

public class PixelSource extends Source {

    public PixelSource(String fileName, String chan, boolean loop) {
        super(fileName, chan, loop);
    }

    @Override
    public void sendData() {
        for (int y = 0; y < this.buffer.height; y++)
            for (int x = 0; x < this.buffer.width; x++) {
                Message msg = new PixelReferenceMessage(channel, this.buffer,
                                                        x, y);
                this.forward(msg);
            }
    }

}
