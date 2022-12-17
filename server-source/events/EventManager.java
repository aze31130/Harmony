package events;

import java.util.ArrayList;
import java.util.List;

import json.JSONObject;
import requests.RequestName;
import requests.RequestType;
import users.User;

public class EventManager {

	public List<Event> events;

	public EventManager() {
		/*
		 * Instanciate all events
		 */
		this.events = new ArrayList<Event>();

		this.events.add(new MessageReceiveEvent());
	}

	public void triggerEvent(User user, RequestType requestType, RequestName requestName, JSONObject data) {
		for (Event event : this.events) {
			if (event.type == requestType && event.name == requestName) {
				event.fire(user, data);
				return;
			}
		}
	}

	public void triggerEventByName(String eventName, JSONObject data) {
		//TODO
	}
}
