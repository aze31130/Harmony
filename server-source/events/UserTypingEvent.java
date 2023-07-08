package events;

import json.JSONObject;
import users.User;

public class UserTypingEvent extends Event {

	public UserTypingEvent() {
		super("UserTyping");
	}

	@Override
	public void fire(User user, JSONObject data) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'fire'");
	}
}