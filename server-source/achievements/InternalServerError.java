package achievements;

import events.Event;
import users.User;

public class InternalServerError implements Achievement {
	
	public InternalServerError() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
