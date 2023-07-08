package events;

import java.util.Date;

import json.JSONObject;
import users.User;

public abstract class Event {
	/*
	 * We are not using an enumeration for the EventType to allow plugins to
	 * easily manipulate and add custom events.
	 */
	public String type;
	public Date eventTime;

	public Event(String type) {
		this.type = type;
		this.eventTime = new Date();
	}

	public abstract void fire(User user, JSONObject data);
}