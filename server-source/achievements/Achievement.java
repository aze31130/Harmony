package achievements;

import events.Event;
import users.User;

public interface Achievement {
	public abstract Boolean trigger(Event event);
	public abstract void reward(User user);
}
