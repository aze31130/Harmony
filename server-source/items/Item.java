package items;

import java.util.Date;

import users.User;

public abstract class Item {
	public String name;
	public String description;
	public int level;
	public int requiredLevel;
	public String lore;
	public Date created;
	public Boolean isCrafted;
	public String crafterName;
	public Boolean isTradable;
	public Rarity rarity;

	public Boolean usable;

	public Item(String name, String description, int level, int requiredLevel, String lore) {
		this.name = name;
		this.description = description;
		this.level = level;
		this.requiredLevel = requiredLevel;
		this.lore = lore;
	}

	public abstract void use(User user);
}
