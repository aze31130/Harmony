package achievements;

import events.Event;
import users.User;

public class TheFantasyWeaponIsMine implements Achievement {
	
	public TheFantasyWeaponIsMine() {
	}

	@Override
	public Boolean trigger(User user, Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
