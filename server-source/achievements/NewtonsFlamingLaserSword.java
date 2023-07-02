package achievements;

import events.Event;
import users.User;

public class NewtonsFlamingLaserSword implements Achievement {
	
	public NewtonsFlamingLaserSword() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
