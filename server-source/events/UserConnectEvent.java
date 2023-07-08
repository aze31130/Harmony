package events;

import json.JSONObject;
import users.User;

public class UserConnectEvent extends Event {

	public UserConnectEvent() {
		super("UserConnect");
	}

	@Override
	public void fire(User user, JSONObject data) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'fire'");
	}
}