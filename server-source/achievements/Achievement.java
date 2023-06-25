package achievements;

import events.Event;
import users.User;

public interface Achievement {
	public abstract Boolean trigger(User user, Event event);
	public abstract void reward(User user);
}
