package events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import achievements.Achievement;
import commands.Command;
import json.JSONObject;
import plugins.Plugin;
import requests.Request;
import requests.RequestName;
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
		this.events.add(new UserConnectEvent());
	}

	public void triggerEvent(User user, RequestName requestName, JSONObject data) {
		//Fires the event depending on the given request
		for (Event event : this.events) {
			/*
			 * TODO, Still need to figure out what kind of selector is the best for events (string, enum, combo of both ?)
			 */
			//if (event.type == requestName.toString()) {
				//Fire the event
				event.fire(user, event.type.toString(), data);

				/*
				 * Check if the event unlocked an achievement
				 */
				for (Achievement a : AchievementsUtils.filterAchievements(requestName))
					if (a.trigger(user, event))
						a.reward(user);
				
				/*
				 * Check for plugin triggers
				 * WARNING, loading malicious plugins may result in a total server compromision.
				 * Be aware that using reflection here is only meant to use trusted method from
				 * known friends.
				 */
				for(Plugin p : Server.getInstance().plugins) {
					try {
						Object o = p.pluginClass.getDeclaredConstructor().newInstance();
						Method m = p.pluginClass.getMethod(event.type.toString(), String.class);
						m.invoke(o, "args");
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				return;
			// }
		}
	}

	public void triggerEventByName(String eventName, JSONObject data) {
		//TODO
	}
}
