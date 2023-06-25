package achievements;

import events.Event;
import events.MessageReceiveEvent;
import items.WelcomeApple;
import requests.RequestName;
import requests.RequestType;
import users.User;

public class HelloWorld implements Achievement {
	/*
	 * This achievement is an example of how to implement achievements in Harmony.
	 * 
	 * Awards when a user sends in any channel "Hello World !"
	 */

	public final RequestType[] triggerType = {
		RequestType.COMMAND
	};
	public final RequestName[] triggerName = {
		RequestName.CREATE_MESSAGE
	};

	public HelloWorld() {}

	@Override
	public Boolean trigger(User user, Event event) {
		//Security check for safe downcast
		if (!(event instanceof MessageReceiveEvent)) {
			//Logs an error
			System.err.println("Unable to downcast event !");
			return false;
		}

		MessageReceiveEvent messageEvent = (MessageReceiveEvent) event;

		return messageEvent.message.content.equalsIgnoreCase("Hello World !");
	}

	@Override
	public void reward(User user) {
		//Add the current achievement to the user inventory
		user.achievements.add(this);

		//Instanciate the reward item
		WelcomeApple reward = new WelcomeApple();
		user.inventory.add(reward);

		user.addExp(10);
		user.addMoney(1);
	}
}
