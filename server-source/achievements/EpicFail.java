package achievements;

import events.Event;
import users.User;

public class EpicFail implements Achievement {
	
	public EpicFail() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
