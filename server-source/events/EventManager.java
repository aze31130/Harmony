package events;

import java.util.ArrayList;
import java.util.List;

import json.JSONObject;
import requests.RequestName;
import requests.RequestType;

public class EventManager {

	public List<Event> events;

	public EventManager() {
		//Instanciate all events
		this.events = new ArrayList<Event>();
	}

	public void triggerEvent(RequestType requestType, RequestName requestName, JSONObject data) {
		for (Event event : this.events) {
			if (event.type == requestType && event.name.equals(requestName)) {
				event.fire(data);
				return;
			}
		}
	}

	public void triggerEventByName(String eventName, JSONObject data) {
		//TODO
	}
}
