package achievements;

import events.Event;
import users.User;

public class ClimateChange implements Achievement {
	
	public ClimateChange() {
	}

	@Override
	public Boolean trigger(Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}