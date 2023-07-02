package achievements;

import events.Event;
import users.User;

public class TenThousandHours implements Achievement {
	
	public TenThousandHours() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
