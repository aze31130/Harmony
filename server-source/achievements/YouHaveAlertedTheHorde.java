package achievements;

import events.Event;
import users.User;

public class YouHaveAlertedTheHorde implements Achievement {
	
	public YouHaveAlertedTheHorde() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
