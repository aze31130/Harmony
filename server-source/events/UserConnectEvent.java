package events;

import json.JSONObject;
import users.User;

public class UserConnectEvent extends Event {

	public UserConnectEvent() {
		super(EventType.UserConnect);
	}

	@Override
	public void fire(User user, String name, JSONObject data) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'fire'");
	}
}