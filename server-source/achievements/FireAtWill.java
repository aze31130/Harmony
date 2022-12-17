package achievements;

import events.Event;
import users.User;

public class FireAtWill implements Achievement {
	
	public FireAtWill() {
	}

	@Override
	public Boolean trigger(Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
