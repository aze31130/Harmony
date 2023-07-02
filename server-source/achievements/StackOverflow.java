package achievements;

import events.Event;
import users.User;

public class StackOverflow implements Achievement {
	
	public StackOverflow() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
