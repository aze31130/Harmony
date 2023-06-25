package events;

import java.util.ArrayList;
import java.util.List;

import achievements.Achievement;
import json.JSONObject;
import requests.RequestName;
import requests.RequestType;
import server.Server;
import users.User;
import utils.AchievementsUtils;

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
		//Fires the event depending on the given request
		for (Event event : this.events) {
			if (event.type == requestType && event.name == requestName) {
				//Fire the event
				event.fire(user, data);

				//Check if the event unlocked an achievement
				for (Achievement a : AchievementsUtils.filterAchievements(requestType, requestName))
					if (a.trigger(user, event))
						a.reward(user);
				
				return;
			}
		}
	}

	public void triggerEventByName(String eventName, JSONObject data) {
		//TODO
	}
}
