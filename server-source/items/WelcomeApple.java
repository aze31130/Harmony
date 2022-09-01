package items;

import users.User;

public class WelcomeApple extends Item implements UsableItem {

	public WelcomeApple() {
		super("Welcome Apple",
		"An apple gave to people that wrote 'Hello World !' in the chat.",
		1,
		3,
		"This item is said to have felt from the sky. How strange, you can barely see sir I.N. written on it.");
	}

	@Override
	public void use(User user) {
		user.addExp(3);
	}
}
