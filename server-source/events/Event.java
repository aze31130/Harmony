package events;

import java.util.Date;

import json.JSONObject;
import users.User;

public abstract class Event {
	public EventType type;
	public Date eventTime;

	public Event(EventType type) {
		this.type = type;
		this.eventTime = new Date();
	}

	public abstract void fire(User user, String name, JSONObject data);
}