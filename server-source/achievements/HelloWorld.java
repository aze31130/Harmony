package achievements;

import events.Event;
import events.MessageReceiveEvent;
import items.WelcomeApple;
import users.User;

public class HelloWorld implements Achievement {

	public HelloWorld() {
	}

	@Override
	public Boolean trigger(Event event) {
		MessageReceiveEvent messageEvent = (MessageReceiveEvent) event;
		return messageEvent.message.content.equalsIgnoreCase("Hello World !");
	}

	@Override
	public void reward(User user) {
		//Instanciate the reward item
		WelcomeApple reward = new WelcomeApple();
		user.inventory.add(reward);

		user.addExp(10);
		user.addMoney(1);
	}
}
