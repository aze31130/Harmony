package achievements;

import events.Event;
import users.User;

public class WinterIsComing implements Achievement {
	
	public WinterIsComing() {
	}

	@Override
	public Boolean trigger(Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}