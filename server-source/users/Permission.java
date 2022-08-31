package users;

public class Permission {
	public static Boolean canExecute(Privilege privilege, User executor) {
		return executor.permissions.level >= privilege.level;
	}
}
