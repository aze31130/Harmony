package events;

import json.JSONObject;
import users.User;

public class LoadEvent extends Event {

	public LoadEvent() {
		super("Load");
	}

    /*
     * This is the very first event triggered on the launch of the server.
     * 
	 * Arguments inside data object:
	 * @Integer: Commands_amount
     * @Integer: Plugin_amount
     * @Integer: _amount
     */
	@Override
	public void fire(User user, JSONObject data) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'fire'");
	}
}