package achievements;

import events.Event;
import users.User;

public class OHKO implements Achievement {
	
	public OHKO() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
