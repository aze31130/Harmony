package achievements;

import events.Event;
import users.User;

public class FatalError implements Achievement {
	
	public FatalError() {
	}

	@Override
	public Boolean trigger(Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
