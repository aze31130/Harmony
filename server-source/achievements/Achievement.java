package achievements;

import events.Event;
import users.User;

public abstract class Achievement {
    public abstract Boolean trigger(Event event);
    public abstract void reward(User user);
}
