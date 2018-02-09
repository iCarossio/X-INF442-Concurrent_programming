package data;

public class ChannelSelector implements MessageProcessor {
    private final String channel;

    public ChannelSelector(String channel) {
        this.channel = channel;
    }

    @Override
    public Message process(Message message) {
        if (this.channel.equals(message.channel)) return message;
        return null;
    }

}
