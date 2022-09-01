package achievements;

import java.util.ArrayList;
import java.util.List;

public class AchievementFactory {
	/*
	 * This methods returns a list of instanciated achievements
	 */
	public static List<Achievement> instanciateAchievements() {
		List<Achievement> achievements = new ArrayList<Achievement>();
		
		achievements.add(new HelloWorld());
		achievements.add(new DoIt());
		achievements.add(new CircleOfLife());
		achievements.add(new WinterIsComing());

		return achievements;
	}
}
