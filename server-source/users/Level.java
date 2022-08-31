package users;

public class Level {
	public static int getRequiredExperience(int level) {
		int baseExperience = 10;
		double exponent = 4;
		return (int)(baseExperience * (Math.pow((level + 1), exponent)));
	}

	public static String getExperiencePercentage(int level, long experience) {
		return (experience * 100 / getRequiredExperience(level)) + "%";
	}
}
