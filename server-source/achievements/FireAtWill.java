package achievements;

import events.Event;
import users.User;

public class FireAtWill implements Achievement {
	
	public FireAtWill() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
