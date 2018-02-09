package data;

public class Identity implements MessageProcessor {

    public Message process(Message message) {
        return message;
    }

}
