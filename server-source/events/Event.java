package events;

import java.util.Date;

import json.JSONObject;
import requests.RequestName;
import users.User;

public abstract class Event {
	public RequestName[] name;
	public Date eventTime;

	public Event(RequestName[] name) {
		this.name = name;
		this.eventTime = new Date();
	}

	public abstract void fire(User user, String name, JSONObject data);
}