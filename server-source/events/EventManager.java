package events;

import java.util.ArrayList;
import java.util.List;

import achievements.Achievement;
import json.JSONObject;
import requests.Request;
import requests.RequestName;
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

	public void triggerEvent(User user, RequestName requestName, JSONObject data) {
		//Fires the event depending on the given request
		for (Event event : this.events) {
			for (RequestName rn : event.name) {
				if (rn == requestName) {
					//Fire the event
					event.fire(user, rn.toString(), data);
	
					//Check if the event unlocked an achievement
					for (Achievement a : AchievementsUtils.filterAchievements(requestName))
						if (a.trigger(user, event))
							a.reward(user);
					
					// Check for plugin triggers
					// TODO
					return;
				}
			}
		}
	}

	public void triggerEventByName(String eventName, JSONObject data) {
		//TODO
	}
}
