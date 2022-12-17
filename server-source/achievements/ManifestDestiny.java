package achievements;

import events.Event;
import users.User;

public class ManifestDestiny implements Achievement {
	
	public ManifestDestiny() {
	}

	@Override
	public Boolean trigger(Event event) {
		return false;
	}

	@Override
	public void reward(User user) {
	}
}
