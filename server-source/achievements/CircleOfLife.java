package achievements;

import events.Event;
import users.User;

public class CircleOfLife implements Achievement {
	
	public CircleOfLife() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
