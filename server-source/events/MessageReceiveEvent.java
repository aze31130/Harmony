package events;

import channels.Message;
import users.User;

public class MessageReceiveEvent extends Event {
    public User sender;
    public Message message;

    public MessageReceiveEvent(User sender, Message message) {
        this.sender = sender;
        this.message = message;
    }
}