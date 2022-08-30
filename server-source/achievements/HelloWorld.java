package achievements;

import events.Event;
import events.MessageReceiveEvent;
import items.WelcomeApple;
import users.User;

public class HelloWorld extends Achievement {

    public HelloWorld() {
        super();
    }

    @Override
    public Boolean trigger(Event event) {
        MessageReceiveEvent messageEvent = (MessageReceiveEvent) event;
        return messageEvent.message.content.equalsIgnoreCase("Hello World !");
    }

    @Override
    public void reward(User user) {
        //Instanciate the item
        WelcomeApple reward = new WelcomeApple();
        user.inventory.add(reward);
    }
}
