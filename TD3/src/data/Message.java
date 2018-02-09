package data;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private static final long serialVersionUID = -4852989804436565044L;

    public static volatile int __lastMsgNum = 0;

    public final String channel;
    protected transient final int seq;

    protected Message(String channel) {
        this.channel = channel;
        this.seq = ++__lastMsgNum;
    }

    // INFO force all messages to have a uniform string representation
    @Override
    public final String toString() {
        return String.format("%s[%d]", this.channel, this.seq);
    }
}
