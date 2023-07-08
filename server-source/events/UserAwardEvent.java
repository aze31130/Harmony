package events;

import json.JSONObject;
import users.User;

public class UserAwardEvent extends Event {

	public UserAwardEvent() {
		super("UserAward");
	}

	@Override
	public void fire(User user, JSONObject data) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'fire'");
	}
}