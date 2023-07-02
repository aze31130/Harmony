package achievements;

import events.Event;
import users.User;

public class Overkill implements Achievement {
	
	public Overkill() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
