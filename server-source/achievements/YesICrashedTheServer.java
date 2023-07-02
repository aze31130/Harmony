package achievements;

import events.Event;
import users.User;

public class YesICrashedTheServer implements Achievement {
	
	public YesICrashedTheServer() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
