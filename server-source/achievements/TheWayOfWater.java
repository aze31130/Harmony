package achievements;

import events.Event;
import users.User;

public class TheWayOfWater implements Achievement {
	
	public TheWayOfWater() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
