package achievements;

import events.Event;
import users.User;

public class Stormtrooper implements Achievement {
	
	public Stormtrooper() {
	}

	@Override
	public Boolean trigger(Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}