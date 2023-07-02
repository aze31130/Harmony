package achievements;

import events.Event;
import users.User;

public class YourDoorWasLocked implements Achievement {
	
	public YourDoorWasLocked() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
