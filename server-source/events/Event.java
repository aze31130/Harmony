package events;

import java.util.Date;

import json.JSONObject;
import requests.RequestName;
import requests.RequestType;
import users.User;

public abstract class Event {
	public RequestType type;
	public RequestName name;
	public Date eventTime;

	public Event(RequestType type, RequestName name) {
		this.type = type;
		this.name = name;
		this.eventTime = new Date();
	}

	public abstract void fire(User user, JSONObject arguments);
}