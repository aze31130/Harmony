package utils;

import java.util.ArrayList;
import java.util.List;

import achievements.Achievement;
import requests.RequestName;
import server.Server;

public class AchievementsUtils {

    private AchievementsUtils() {
		throw new IllegalStateException("This class is utility and cannot be instanciated !");
	}
    
    /*
     * This function is an optimisation. Because the server will check every trigger of every achievement everytime
     * an event will occur, it is better to only execute the triggers of events that are tagged accordingly to the
     * event that happened.
     */
    public static List<Achievement> filterAchievements(RequestName requestName) {
        List<Achievement> achievements = new ArrayList<Achievement>();

        for (Achievement a : Server.getInstance().achievements) {
            //TODO: Check if the achievement has the given request name and type
            achievements.add(a);
        }

        return achievements;
    }
}
