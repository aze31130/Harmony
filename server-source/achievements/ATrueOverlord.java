package achievements;

import events.Event;
import users.User;

public class ATrueOverlord implements Achievement {
	
	public ATrueOverlord() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
