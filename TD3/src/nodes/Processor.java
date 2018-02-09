package nodes;

import data.Message;
import data.MessageProcessor;
import data.MessageQueue;

public class Processor extends Node {

    protected final MessageQueue queue;
    protected final MessageProcessor processor;

    public Processor(MessageQueue q, MessageProcessor p, String name) {
        super(name);
        queue = q;
        processor = p;
    }

    @Override
    public final void putInQueue(Message message) {
        while (!this.queue.add(message))
            sleepUninterruptibly(10);
    }

    protected final void processMessage(Message msg) {
        if (null == msg) throw new IllegalArgumentException();
        msg = this.processor.process(msg);
        if (null != msg) this.forward(msg);
    }

    @Override
    public void run() {
        while (true) {
            Message msg = this.queue.remove();
            processMessage(msg);
        }
    }

}
