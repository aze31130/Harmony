package achievements;

import events.Event;
import users.User;

public class Stormtrooper implements Achievement {
	
	public Stormtrooper() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
