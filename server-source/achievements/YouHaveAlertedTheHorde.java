package achievements;

import events.Event;
import users.User;

public class YouHaveAlertedTheHorde implements Achievement {
	
	public YouHaveAlertedTheHorde() {
	}

	@Override
	public Boolean trigger(Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}