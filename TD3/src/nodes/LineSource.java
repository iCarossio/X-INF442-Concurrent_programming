package nodes;

import data.LineMessage;

public class LineSource extends Source {

    public LineSource(String file, String channel, boolean loop) {
        super(file, channel, loop);
    }

    @Override
    public void sendData() {
        int[] line = new int[buffer.width];
        int k = 0;
        for (int i = 0; i < buffer.height; ++i) {
            for (int j = 0; j < buffer.width; ++j, ++k)
                line[j] = buffer.pixels[k];
            this.forward(new LineMessage(this.channel, i, line.clone()));
        }
    }

}
