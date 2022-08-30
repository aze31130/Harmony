package users;

import java.security.PublicKey;
import java.time.LocalDate;
import java.util.List;

import items.Item;

public class User {
    public int id;
    public String name;
    public int tag;
    public Privilege permissions;
    public String picture;
    public String email;

    public List<Item> inventory;
    public LocalDate created;
    public LocalDate lastLogin;
    public int level;
	public int experience;
	public int totalExperience;
	public int money;
	public int fame;

    public PublicKey pubKey;
}