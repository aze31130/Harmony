package achievements;

import events.Event;
import users.User;

public class TheMistake implements Achievement {
	
	public TheMistake() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
