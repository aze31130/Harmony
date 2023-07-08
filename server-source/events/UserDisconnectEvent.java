package events;

import json.JSONObject;
import users.User;

public class UserDisconnectEvent extends Event {

	public UserDisconnectEvent() {
		super("UserDisconnect");
	}

	@Override
	public void fire(User user, JSONObject data) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'fire'");
	}
}