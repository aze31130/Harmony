package achievements;

import events.Event;
import users.User;

public class DoIt implements Achievement {
	
	public DoIt() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
